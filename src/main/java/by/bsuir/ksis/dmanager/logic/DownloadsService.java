package by.bsuir.ksis.dmanager.logic;

import by.bsuir.ksis.dmanager.domain.Download;
import by.bsuir.ksis.dmanager.domain.Item;
import by.bsuir.ksis.dmanager.persistence.DownloadDAO;
import by.bsuir.ksis.dmanager.persistence.ItemDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Vladislav Piseckij
 */
@Service
@Transactional
public class DownloadsService {
    
    private final DownloadDAO downloadDAO;
    
    private final ItemDAO itemDAO;
    
    @Autowired
    public DownloadsService(DownloadDAO downloadDAO, ItemDAO itemDAO) {
        this.downloadDAO = downloadDAO;
        this.itemDAO = itemDAO;
    }
    
    public Result create(NewDownload newDownload) {
        Download download = Download.builder()
            .name(newDownload.getName())
            .priority(newDownload.getPriority())
            .username(Objects.toString(newDownload.getUsername(), "ANONYMOUS"))
            .password(newDownload.getPassword())
            .status(Download.Status.WAIT)
            .created(LocalDateTime.now())
            .build()
            ;
        if (!checkDownloadNameUnique(download)) {
            return Result.fail("Закачка с таким именем уже существует");
        }

        String itemsDestination = newDownload.getDestination() + File.pathSeparator + download.getName();


        List<Item> files = newDownload.getLinks().stream()
            .map(link -> Item.builder()
                .link(link)
                .destination(itemsDestination)
                .status(Item.Status.WAIT)
                .build()
            )
            .collect(Collectors.toList());
        download = downloadDAO.create(download);
        final Integer downloadId = download.getId();
        files.stream().peek(file -> file.setDownloadId(downloadId)).forEach(itemDAO::create);
        
        //TODO: show new download on UI
        //TODO: start download if user select "start after creation"
        
        return Result.success();
    }

    private boolean checkDownloadNameUnique(Download download) {
        return downloadDAO.findByName(download.getName()) == null;
    }
}
