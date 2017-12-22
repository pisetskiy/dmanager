package by.bsuir.ksis.dmanager.api.data;

import by.bsuir.ksis.dmanager.domain.DownloadPriority;
import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.util.List;

/**
 * @author Vladislav Piseckij
 */
@Data
@Builder
public class DownloadNewDTO {
    
    private final List<String> links;
    
    private final File destination;
    
    private final String name;
    
    private final DownloadPriority priority;
    
    private final boolean start;
    
    private final CredentialsDTO credentials;
    
}
