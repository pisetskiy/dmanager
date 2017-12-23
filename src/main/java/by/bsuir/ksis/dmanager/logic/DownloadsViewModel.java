package by.bsuir.ksis.dmanager.logic;

import by.bsuir.ksis.dmanager.domain.Download;
import by.bsuir.ksis.dmanager.domain.ItemsInfo;
import by.bsuir.ksis.dmanager.persistence.DownloadDAO;
import by.bsuir.ksis.dmanager.persistence.ItemDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Service
public class DownloadsViewModel {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private DownloadDAO downloadDAO;
    private ItemDAO itemDAO;

    @Autowired
    public DownloadsViewModel(DownloadDAO downloadDAO, ItemDAO itemDAO) {
        this.downloadDAO = downloadDAO;
        this.itemDAO = itemDAO;
    }

    private final Set<Consumer<List<Download>>> downloadsListListeners = new HashSet<>();

    public void submitOnDownloadsListChange(Consumer<List<Download>> listener) {
        if (listener != null) {
            downloadsListListeners.add(listener);
        }
    }

    public void emitDownloadsListChange() {
        executor.submit(() -> {
            List<Download> downloads = Collections.unmodifiableList(downloadDAO.list());
            Map<Integer, ItemsInfo> downloadsItemsInfo = itemDAO.itemsInfo();
            downloads.forEach(d -> {
                ItemsInfo i = downloadsItemsInfo.get(d.getId());
                if (i == null) return;
                d.setAllFilesCount(i.getCount());
                d.setLoadedBytes(i.getLoadedBytes());
                d.setTotalBytes(i.getTotalBytes());
            });
            downloadsListListeners.forEach(listener -> listener.accept(downloads));
        });
    }
}
