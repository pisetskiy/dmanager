package by.bsuir.ksis.dmanager.api.data;

import lombok.Builder;
import lombok.Data;

import java.io.File;

/**
 * @author Vladislav Piseckij
 */
@Data
@Builder
public class DownloadNewDTO {
    
    private final String links;
    
    private final File destination;
    
    private final String name;
    
    private final DownloadPriority priority;
    
    private final boolean start;
    
    private final CredentialsDTO credentials;
    
}
