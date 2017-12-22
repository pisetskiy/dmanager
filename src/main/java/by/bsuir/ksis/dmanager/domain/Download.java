package by.bsuir.ksis.dmanager.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Vladislav Piseckij
 */
@Data
@Builder
public class Download {
    
    private Integer id;
    
    private String name;
    
    private DownloadPriority priority;
    
    private String username;
    
    private String password;
    
    private LocalDateTime created;
    
    private DownloadStatus status;
}
