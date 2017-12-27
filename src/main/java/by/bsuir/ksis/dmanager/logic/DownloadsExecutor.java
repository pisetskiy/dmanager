package by.bsuir.ksis.dmanager.logic;

import by.bsuir.ksis.dmanager.domain.Download;
import by.bsuir.ksis.dmanager.domain.File;
import by.bsuir.ksis.dmanager.domain.Status;
import by.bsuir.ksis.dmanager.persistence.DownloadDAO;
import by.bsuir.ksis.dmanager.persistence.FileDAO;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class DownloadsExecutor {
    
    @Autowired
    private DownloadDAO downloadDAO;
    @Autowired
    private FileDAO fileDAO;
    @Autowired
    private DownloadsViewModel viewModel;
    @Autowired
    private FTPClient ftpClient;
    @Autowired
    private SimpleAsyncTaskExecutor executor;
    
    private Map<Integer, Future> currentExecution = new HashMap<>();

    public void stopDownloadExecution(final Integer downloadId) {
        if (currentExecution.containsKey(downloadId)) {
            Future execution = currentExecution.get(downloadId);
            execution.cancel(true);
            currentExecution.remove(downloadId);
        }
    }
    
    @Scheduled(fixedDelay = 5000)
    public void executeDownload() {
        Download download = downloadDAO.getDownloadForExecution();
        
        if (download != null) {
            Future excution = executor.submit(() -> retrieveFilesFor(download));
            currentExecution.put(download.getId(),excution);
            Status status = null;
            try {
                excution.get();
            } catch (ExecutionException e) {
                status = Status.ERROR;
            } catch (CancellationException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (status == null) {
                status = fileDAO.hasErrorFiles(download) ? Status.ERROR : Status.END;
            }
            download.setStatus(status);
            downloadDAO.update(download);
            currentExecution.remove(download.getId());
            viewModel.emitDownloadsListChange();
        }
    }
    
    private void retrieveFilesFor(final Download download) {
        java.io.File folder = getDestinationFolder(download);
        List<File> files = fileDAO.getForFilesExecution(download.getId());
        for (File file : files) {
            file.setStatus(Status.END);
            java.io.File localFile = new java.io.File(folder, file.getName());
            
            boolean hasLocalFile = false;
            if (!(hasLocalFile = createLocalFile(localFile))) {
                error(file, "Не удалось создать файл на компьютере");
            }
            
            boolean connected = false;
            if (hasLocalFile && !(connected = connect(file.getLink()))) {
                error(file, "Не удалось подключиться к серверу");
            }
            
            boolean loggedIn = false;
            if (connected && !(loggedIn = login(file.getUsername(), file.getPassword()))) {
                error(file, "Не удалось пройти аторизацию, неврные логин или пароль");
            }
            
            boolean retrieved = false;
            if (loggedIn && !(retrieved = retrieve(file.getLink(), localFile))) {
                error(file, "Не удалось загрузить файл");
            }

            if (retrieved && !rename(file.getLink(), localFile)) {
                error(file, "Не удалось перименовать файл");
            }
            
            fileDAO.update(file);
        }
    }

    private java.io.File getDestinationFolder(Download download) {
        java.io.File folder = new java.io.File(download.getDestination());
        if (!folder.exists()) {
            throw  new RuntimeException("Папки для сохранения файлов не существует");
        }

        if (!folder.isDirectory()) {
            throw  new RuntimeException("Папка для сохранения файлов не является папкой");
        }

        if (!folder.canWrite()) {
            throw  new RuntimeException("Папка для сохранения недоступна для записи");
        }

        java.io.File subfolder = new java.io.File(folder, download.getName());
        if (!subfolder.exists() && !subfolder.mkdir()) {
            throw  new RuntimeException("Не удалось создать подпапку для сохраения файлов");
        }

        return subfolder;
    }

    private boolean createLocalFile(java.io.File localFile) {
        try {
            if (localFile.exists() && localFile.canWrite()) {
                return true;
            }

            if (!localFile.exists() && localFile.createNewFile()) {
                return true;
            }

            return false;
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }

    }

    private boolean connect(String link) {
        String host = "";
        if (link.contains("//")) {
            int index = link.indexOf("//");
            link = link.substring(index + 2);
        }

        host = link;
        if (link.contains("/")){
            int index = link.indexOf("/");
            index = index < 0 ? link.length() - 1 : index;
            host = link.substring(0, index);
        }

        int port = FTPClient.DEFAULT_PORT;
        if (host.contains(":")) {
            int index= host.indexOf(":");
            try {
                port = Integer.parseInt(host.substring(index));
            } catch (NumberFormatException e) {}
        }

        try {
            ftpClient.connect(host, port);
            ftpClient.enterLocalPassiveMode();

            return true;
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }

    }

    private boolean login(String username, String password) {
        try {
            return ftpClient.login(username, password);
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }
    }

    private boolean retrieve(String link, java.io.File localFile) {
        String directory = link;
        int index = 0;

        if ((index = directory.indexOf("//")) != -1) {
            directory = link.substring(index + 2);
        }

        if ((index = directory.indexOf("/")) > 0) {
            directory = directory.substring(index + 1);
        }

        String file = "";
        if ((index = directory.lastIndexOf("/")) != -1) {
            file = directory.substring(index + 1);
            directory = directory.substring(0, index);
        } else {
            file = directory;
            directory = "/";
        }

        try (OutputStream out = new FileOutputStream(localFile)){

            return ftpClient.changeWorkingDirectory(directory) && ftpClient.retrieveFile(file, out);
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }

    }

    private boolean rename(String link, java.io.File localFile) {
        int index = link.lastIndexOf("/");
        String file = link.substring(index + 1);
        java.io.File result = new java.io.File(localFile.getParentFile(), file);
        return localFile.renameTo(result);
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

    private void error(File file, String error) {
        file.setStatus(Status.ERROR);
        file.setMessage(error);
    }
}
