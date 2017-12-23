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
    
    private Status status;

    public enum Status {
        WAIT("Готов к загрузке"),
        ERROR("Не удается загрузить"),
        RUN("Загружается"),
        END("Загружен");

        private String value;

        Status(String value) {
            this.value = value;
        }


        @Override
        public String toString() {
            return value;
        }
    }
}
