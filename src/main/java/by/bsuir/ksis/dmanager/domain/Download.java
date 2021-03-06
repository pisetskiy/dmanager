package by.bsuir.ksis.dmanager.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Vladislav Piseckij
 */
@Data
@Builder
public class Download {
    
    private Integer id;
    private String name;
    private String destination;
    private Priority priority;
    private Status status;
    private LocalDateTime created;
    private String message;
    private List<File> files;

}
