package by.bsuir.ksis.dmanager.logic;

import by.bsuir.ksis.dmanager.domain.Download;
import by.bsuir.ksis.dmanager.domain.Status;
import by.bsuir.ksis.dmanager.persistence.DownloadDAO;
import by.bsuir.ksis.dmanager.persistence.FileDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static by.bsuir.ksis.dmanager.ui.Util.getRandomString;

/**
 * @author Vladislav Piseckij
 */
@Service
@Transactional
public class DownloadsService {
    
    private final DownloadDAO downloadDAO;
    private final FileDAO fileDAO;
    private final DownloadsViewModel downloadsViewModel;
    private final DownloadsExecutor executor;

    @Autowired
    public DownloadsService(
        DownloadDAO downloadDAO,
        FileDAO fileDAO,
        DownloadsViewModel downloadsViewModel,
        DownloadsExecutor executor
    ) {
        this.downloadDAO = downloadDAO;
        this.fileDAO = fileDAO;
        this.downloadsViewModel = downloadsViewModel;
        this.executor = executor;
    }

    public Result create(final Download download) {

        Result validationResult = validateDownload(download, true);
        if (!validationResult.isSuccess()) {
            return validationResult;
        }

        download.setStatus(Status.READY);
        download.setCreated(LocalDateTime.now());
        downloadDAO.create(download);
        download.getFiles().stream()
        .peek(f -> f.setDownloadId(download.getId()))
        .peek(f -> f.setStatus(Status.READY))
        .peek(f -> f.setName(getRandomString(10)))
        .forEach(fileDAO::create);

        downloadsViewModel.emitDownloadsListChange();
        
        return Result.success();
    }

    public Result startDownloads(List<Download> downloads) {
        long updatedCount = downloads.stream()
            .filter(d -> Status.RUN != d.getStatus())
            .peek(d -> d.setStatus(Status.RUN))
            .peek(downloadDAO::update)
            .count();

        if (updatedCount > 0) {
            downloadsViewModel.emitDownloadsListChange();
        }

        return Result.success();
    }

    public Result stopDownloads(List<Download> downloads) {
        long stoppedCount = downloads.stream()
            .filter(d -> Status.RUN == d.getStatus())
            .peek(d -> executor.stopDownloadExecution(d.getId()))
            .peek(d -> d.setStatus(Status.READY))
            .peek(downloadDAO::update)
            .count();

        if (stoppedCount > 0) {
            downloadsViewModel.emitDownloadsListChange();
        }

        return Result.success();
    }

    public Result updateDownload(Download download) {
        Result validationResult = validateDownload(download, false);
        if (!validationResult.isSuccess()) {
            return validationResult;
        }

        download.setStatus(Status.READY);
        downloadDAO.update(download);
        download.getFiles().stream()
            .filter(f -> f.getDownloadId() != null)
            .forEach(fileDAO::update);
        download.getFiles().stream()
            .filter(f -> f.getDownloadId() == null)
            .peek(f -> f.setDownloadId(download.getId()))
            .peek(f -> f.setStatus(Status.READY))
            .peek(f -> f.setName(getRandomString(10)))
            .forEach(fileDAO::create);

        downloadsViewModel.emitDownloadsListChange();

        return Result.success();
    }

    public Result deleteDownloads(List<Download> downloads) {
        long deletedCount = downloads.stream()
            .peek(d -> executor.stopDownloadExecution(d.getId()))
            .peek(d -> fileDAO.deleteByDownload(d.getId()))
            .peek(d -> downloadDAO.delete(d.getId()))
            .count();

        if (deletedCount > 0) {
            downloadsViewModel.emitDownloadsListChange();
        }

        return Result.success();
    }

    private Result validateDownload(Download download, boolean nameUniques) {
        if (download.getName() == null) return Result.fail("Имя загрузки не задано");
        if (nameUniques) {
            if (!checkDownloadNameUnique(download)) return Result.fail("Загрузка с такими менем уже существует");
        }
        if (download.getDestination() == null || download.getDestination().isEmpty()) return Result.fail("Не задана пака для сохранения загрузки");
        java.io.File destination = new java.io.File(download.getDestination());
        if (!destination.exists()) return Result.fail("Выбранная папка для сохранения не существует");
        if (!destination.isDirectory()) return Result.fail("Необходимо указать папку, ан не файл");
        if (!destination.canWrite()) return Result.fail("Программа не может записывать данные в указанную папку");
        if (download.getFiles() == null || download.getFiles().isEmpty()) return Result.fail("Нет файлов для загрузки");

        return Result.success();
    }

    private boolean checkDownloadNameUnique(Download download) {
        return downloadDAO.findByName(download.getName()) == null;
    }
}
