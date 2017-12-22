package by.bsuir.ksis.dmanager.domain;

/**
 * @author Vladislav Piseckij
 */
public enum DownloadPriority {
    
    HIGH("Высокий"),
    NORM("Обычный"),
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
