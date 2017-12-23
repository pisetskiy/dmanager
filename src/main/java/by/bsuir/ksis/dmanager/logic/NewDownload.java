package by.bsuir.ksis.dmanager.logic;

import by.bsuir.ksis.dmanager.domain.Download;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Vladislav Piseckij
 */
@Data
@Builder
public class NewDownload {
    
    private final List<String> links;
    
    private final String destination;
    
    private final String name;
    
    private final Download.Priority priority;

    private String username;

    private String password;

    private boolean start;
}
