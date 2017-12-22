package by.bsuir.ksis.dmanager.domain;

/**
 * @author Vladislav Piseckij
 */
public enum ItemStatus {
    
    WAIT("Ожидает"),
    ERROR("Ошибка"),
    RUN("Загружается"),
    FINISHED("Завершено");
    
    private String value;
    
    ItemStatus(String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
    
}
