package by.bsuir.ksis.dmanager.domain;

import lombok.Getter;

@Getter
public enum Priority {

    HIGH("Высокий", 1),
    NORMAL("Обычный", 2),
    LOW("Низкий", 3);

    public static Priority fromOrder(int order) {
        for (Priority priority : Priority.values()) {
            if (priority.order == order) {
                return priority;
            }
        }

        return null;
    }

    private final String value;
    private final int order;

    Priority(String value, int order) {
        this.value = value;
        this.order = order;
    }


    @Override
    public String toString() {
        return value;
    }


}
