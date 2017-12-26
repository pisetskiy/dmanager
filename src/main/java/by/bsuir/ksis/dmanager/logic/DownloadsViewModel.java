package by.bsuir.ksis.dmanager.logic;

import by.bsuir.ksis.dmanager.domain.Download;
import by.bsuir.ksis.dmanager.persistence.DownloadDAO;
import by.bsuir.ksis.dmanager.persistence.FileDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Service
public class DownloadsViewModel {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private DownloadDAO downloadDAO;
    private FileDAO fileDAO;

    @Autowired
    public DownloadsViewModel(DownloadDAO downloadDAO, FileDAO fileDAO) {
        this.downloadDAO = downloadDAO;
        this.fileDAO = fileDAO;
    }

    private final Set<Consumer<List<Download>>> downloadsListListeners = new HashSet<>();

    public void subscribeOnDownloadsListChange(Consumer<List<Download>> listener) {
        if (listener != null) {
            downloadsListListeners.add(listener);
        }
    }

    public void emitDownloadsListChange() {
        executor.submit(() -> {
            List<Download> downloads = Collections.unmodifiableList(downloadDAO.list());
            downloadsListListeners.forEach(listener -> listener.accept(downloads));
        });
    }
}
