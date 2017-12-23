package by.bsuir.ksis.dmanager.api.service;

import by.bsuir.ksis.dmanager.api.data.DownloadNewDTO;
import by.bsuir.ksis.dmanager.domain.Download;
import by.bsuir.ksis.dmanager.domain.DownloadStatus;
import by.bsuir.ksis.dmanager.domain.Item;
import by.bsuir.ksis.dmanager.domain.ItemStatus;
import by.bsuir.ksis.dmanager.persistence.DownloadDAO;
import by.bsuir.ksis.dmanager.persistence.ItemDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
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
    
    public ActionResult create(DownloadNewDTO downloadDTO) {
        Download download = Download.builder()
            .name(downloadDTO.getName())
            .priority(downloadDTO.getPriority())
            .username(downloadDTO.getCredentials() != null ? downloadDTO.getCredentials().getUsername() : "ANONYMOUS")
            .password(downloadDTO.getCredentials() != null ? downloadDTO.getCredentials().getPassword() : "")
            .status(DownloadStatus.WAIT)
            .created(LocalDateTime.now())
            .build()
            ;
        if (!checkDownloadNameUnique(download)) {
            return ActionResult.fail("Закачка с таким именем уже существует");
        }

        String itemsDestination = downloadDTO.getDestination().getAbsolutePath() + File.pathSeparator + download.getName();


        List<Item> files = downloadDTO.getLinks().stream()
            .map(link -> Item.builder()
                .link(link)
                .destination(itemsDestination)
                .status(ItemStatus.WAIT)
                .build()
            )
            .collect(Collectors.toList());
        download = downloadDAO.create(download);
        final Integer downloadId = download.getId();
        files.stream().peek(file -> file.setDownloadId(downloadId)).forEach(itemDAO::create);
        
        //TODO: show new download on UI
        //TODO: start download if user select "start after creation"
        
        return ActionResult.success();
    }

    private boolean checkDownloadNameUnique(Download download) {
        return downloadDAO.findByName(download.getName()) == null;
    }
}
