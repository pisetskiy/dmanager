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
            try {
                excution.get();
            } catch (ExecutionException e) {
            
            } catch (CancellationException e) {
        
            } catch (InterruptedException e) {}
            currentExecution.remove(download.getId());
        }
    }
    
    private void retrieveFilesFor(final Download download) {
        java.io.File folder = getDestinationFolder(download);
        List<File> files = fileDAO.getForFilesExecution(download.getId());
        for (File file : files) {
            java.io.File localFile = new java.io.File(folder, file.getName());
            
            boolean hasNoLocalFile = true;
            if ((hasNoLocalFile = !createLocalFile())) {
            
            }
            
            boolean notConnected = true;
            if (notConnected = (!hasNoLocalFile && !connect(file.getLink()))) {
            
            }
            
            boolean notLogged = true;
            if (notLogged = (!notConnected && !login(file.getUsername(), file.getPassword()))) {
            
            }
            
            boolean notRetrieved = true;
            if (notRetrieved = (!notLogged && !retrieve(file.getLink(), localFile))) {
            
            }
            
            boolean notRenamed = true;
            if (notRenamed = (!notRetrieved && !rename(file.getLink(), localFile))) {
            
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
