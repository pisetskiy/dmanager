package by.bsuir.ksis.dmanager.logic;

import by.bsuir.ksis.dmanager.domain.Download;
import by.bsuir.ksis.dmanager.domain.File;
import by.bsuir.ksis.dmanager.domain.Status;
import by.bsuir.ksis.dmanager.persistence.DownloadDAO;
import by.bsuir.ksis.dmanager.persistence.FileDAO;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class DownloadsExecutor {

    private DownloadDAO downloadDAO;
    private FileDAO fileDAO;
    private DownloadsViewModel viewModel;
    private FTPClient ftpClient;

    private Map<Integer, Thread> currentExecution = new HashMap<>();

    @Autowired
    public DownloadsExecutor(
        DownloadDAO downloadDAO,
        FileDAO fileDAO,
        DownloadsViewModel viewModel,
        FTPClient ftpClient
    ) {
        this.downloadDAO = downloadDAO;
        this.fileDAO = fileDAO;
        this.viewModel = viewModel;
        this.ftpClient = ftpClient;
    }

    public void stopDownloadExecution(Download download) {
        if (currentExecution.containsKey(download.getId())) {
            currentExecution.get(download.getId()).interrupt();
        }
    }

    @Scheduled(initialDelay = 1000, fixedRate = 5000)
    public void executeDownload() {
        Download download = downloadDAO.getDownloadForExecution();
        if (download == null) return;
        Result destinationCreationResult = createDestinationFolder(download);
        if (destinationCreationResult.isFail()) {
            downloadDAO.update(error(download, destinationCreationResult.getMessage()));
            viewModel.emitDownloadsListChange();
            return;
        }

        currentExecution.clear();
        currentExecution.put(download.getId(), Thread.currentThread());

        final java.io.File destination = new java.io.File(download.getDestination(), download.getName());
        List<File> files = fileDAO.getForFilesExecution(download.getId());
        int filesWithErrorsCount = 0;
        LinkedList<File> filesExecutionQueue = new LinkedList<>(files);
        while (!filesExecutionQueue.isEmpty()) {

            File file = filesExecutionQueue.getFirst();
//            filesExecutionQueue.remove(file);
            file.setStatus(Status.END);
            fileDAO.update(file);

//            Result<ConnectionParams> paramsResult = getConnectionParams(file.getLink());
//            if (paramsResult.isFail()) {
//                filesExecutionQueue.remove(file);
//                fileDAO.update(error(file, paramsResult.getMessage()));
//                continue;
//            }
//
//            ConnectionParams params = paramsResult.getValue();
//            try {
//                ftpClient.connect(params.getAddress(), params.getPort());
//
//                if (!ftpClient.login(file.getUsername(), file.getPassword())) {
//                    filesExecutionQueue.remove(file);
//                    fileDAO.update(error(file, "Указан неверный логин или пароль"));
//                    continue;
//                }
//
//                ftpClient.enterLocalPassiveMode();
//
//                if (!ftpClient.changeWorkingDirectory(params.getWorkingDirectory())) {
//                    filesExecutionQueue.remove(file);
//                    fileDAO.update(error(file, "Указан неверный путь к файлу на сервере"));
//                    continue;
//                }
//
//                ftpClient.retrieveFile();
//
//                ftpClient.enterLocalPassiveMode();
//            } catch (IOException e) {
//                e.printStackTrace();
//
//                filesExecutionQueue.remove(file);
//                fileDAO.update(error(file, "Не удалось подключиться к указонному серверу"));
//                continue;
//            }
            System.out.println("Download execution");
        }

        if (filesWithErrorsCount == 0) {
            download.setStatus(Status.END);
        } else {
            download.setStatus(Status.ERROR);
            download.setMessage("Некоторые файлы не заружены из-за ошибок");
        }

        downloadDAO.update(download);
    }

    private Result createDestinationFolder(Download download) {
        java.io.File folder = new java.io.File(download.getDestination());
        if (!folder.exists()) {
            return Result.fail("Папки для сохранения файлов не существует");
        }

        if (!folder.isDirectory()) {
            return Result.fail("Папка для сохранения файлов не является папкой");
        }

        if (!folder.canWrite()) {
            return Result.fail("Папка для сохранения недоступна для записи");
        }

        java.io.File subfolder = new java.io.File(folder, download.getName());
        if (!subfolder.exists() && !subfolder.mkdir()) {
            return Result.fail("Не удалось создать подпапку для сохраения файлов");
        }

        return Result.success();
    }

    private Download error(Download download, String error) {
        download.setStatus(Status.ERROR);
        download.setMessage(error);

        return download;
    }

    private Result<ConnectionParams> getConnectionParams(String link) {
        ConnectionParams params = new ConnectionParams();

        int firstSeparatorInd = link.indexOf("/");
        firstSeparatorInd = firstSeparatorInd < 0 ? 0 : firstSeparatorInd;
        int lastSeparatorInd = link.lastIndexOf("/");
        lastSeparatorInd = lastSeparatorInd < 0 ? 0 : lastSeparatorInd;
        String address = link.substring(0, firstSeparatorInd);
        if (address.trim().isEmpty()) {
            return Result.fail("Не удалось определить адрес сервера");
        }

        String portStr = FTPClient.DEFAULT_PORT + "";
        int portSeparatorIndex = address.lastIndexOf(":");
        if (portSeparatorIndex > 0) {
            portStr = address.substring(portSeparatorIndex);
            address = address.substring(0, portSeparatorIndex);
        }

        try {
            params.setAddress(InetAddress.getByName(address));
        } catch (UnknownHostException e) {
            e.printStackTrace();

            return Result.fail("Неверный адрес серевера");
        }

        try {
            params.setPort(Integer.parseInt(portStr));
        } catch (NumberFormatException e) {
            return Result.fail("Неверный порт сервера");
        }

        params.setWorkingDirectory(link.substring(firstSeparatorInd, lastSeparatorInd));
        if (firstSeparatorInd != lastSeparatorInd &&
            (params.getWorkingDirectory() == null || params.getWorkingDirectory().trim().isEmpty())) {
            return Result.fail("Неверно задан путь к файлу на сервере");
        }

        params.setFileName(link.substring(lastSeparatorInd));
        if (params.getFileName() == null || params.getFileName().trim().isEmpty()) {
            return Result.fail("Не указано имя файла на сервере");
        }

        return new Result<>(params, true, "");
    }

    private File error(File file, String error) {
        file.setStatus(Status.ERROR);
        file.setMessage(error);

        return file;
    }
}
