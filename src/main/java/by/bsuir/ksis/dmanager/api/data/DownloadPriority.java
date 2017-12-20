package by.bsuir.ksis.dmanager.api.data;

/**
 * @author Vladislav Piseckij
 */
public enum DownloadPriority {
    HIGH("Высокий"),
    NORMAL("Обычный"),
    LOW("Низкий");
    
    private String value;
    
    DownloadPriority(String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return value;
    }
}
