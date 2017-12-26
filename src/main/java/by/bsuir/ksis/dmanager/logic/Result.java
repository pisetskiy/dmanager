package by.bsuir.ksis.dmanager.logic;

import lombok.Data;

/**
 * @author Vladislav Piseckij
 */
@Data
public class Result<T> {


    public static <T> Result<T> success() {
        return new Result<T>(null, true, "");
    }

    public static <T> Result<T> fail(String message) {
        return new Result<T>(null, false, message);
    }

    private final T value;
    
    private final boolean success;
    
    private final String message;

    public boolean isSuccess() {
        return success;
    }

    public boolean isFail() {
        return !success;
    }
    
}
