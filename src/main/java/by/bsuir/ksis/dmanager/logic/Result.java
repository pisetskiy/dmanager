package by.bsuir.ksis.dmanager.logic;

import lombok.Data;

/**
 * @author Vladislav Piseckij
 */
@Data
public class Result {


    public static Result success() {
        return new Result(true, "");
    }

    public static Result fail(String message) {
        return new Result(false, message);
    }
    
    private final boolean success;
    
    private final String message;

    public boolean isSuccess() {
        return success;
    }

    public boolean isFail() {
        return !success;
    }
    
}
