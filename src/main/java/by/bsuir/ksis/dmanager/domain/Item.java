package by.bsuir.ksis.dmanager.domain;

import lombok.Builder;
import lombok.Data;

/**
 * @author Vladislav Piseckij
 */
@Data
@Builder
public class Item {
    
    private Integer id;
    
    private Integer downloadId;
    
    private String link;
    
    private String destination;
    
    private ItemStatus status;
}
