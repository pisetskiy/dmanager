package by.bsuir.ksis.dmanager.ui;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

@Getter
@AllArgsConstructor
class TableColumn<E> {

    private final String name;
    private final Function<E, Object> function;

}
