package by.bsuir.ksis.dmanager.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Vladislav Piseckij
 */
@Data
@Builder
public class File {
    
    private Integer id;
    private Integer downloadId;
    private String link;
    private String username;
    private String password;
    private Status status;
    private String name;
    private String message;
    private LocalDateTime created;

}
