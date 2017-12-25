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

    private String destination;
    
    private Priority priority;

    private Status status;

    private LocalDateTime created;
    
    private String username;
    
    private String password;

    private long allFilesCount;

    private long totalBytes;

    private long loadedBytes;

    public enum Priority {
        HIGH("Высокий", 1),
        NORMAL("Обычный", 2),
        LOW("Низкий", 3);

        private String value;
        private int position;

        Priority(String value, int position) {
            this.value = value;
            this.position = position;
        }

        public int position() {
            return position;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public enum Status {
        WAIT("Остановлена"),
        ERROR("Ошибка"),
        RUN("Загружается"),
        END("Завершена");

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
