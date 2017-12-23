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
    
    private Priority priority;

    private Status status;

    private LocalDateTime created;
    
    private String username;
    
    private String password;

    public enum Priority {
        HIGH("Высокий"),
        NORMAL("Обычный"),
        LOW("Низкий");

        private String value;

        Priority(String value) {
            this.value = value;
        }


        @Override
        public String toString() {
            return value;
        }
    }

    public enum Status {
        WAIT("Готова к загрузке"),
        ERROR("Не удается загрузить"),
        RUN("Загружается"),
        END("Загружена");

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
