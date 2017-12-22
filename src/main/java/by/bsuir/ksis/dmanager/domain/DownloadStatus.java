package by.bsuir.ksis.dmanager.domain;

/**
 * @author Vladislav Piseckij
 */
public enum DownloadStatus {
    
    WAIT("Приостановлено"),
    ERROR("Ошибка"),
    RUN("Загружается"),
    FINISH("Завершено");
    
    private String value;
    
    DownloadStatus(String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
}
