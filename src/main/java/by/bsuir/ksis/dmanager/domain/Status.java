package by.bsuir.ksis.dmanager.domain;

public enum Status {

    READY("Ожидает исполнения"),
    ERROR("Ошибка исполнения"),
    RUN("Загружается"),
    END("Загрузка зевершена");

    private String value;

    Status(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
