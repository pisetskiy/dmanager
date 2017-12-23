package by.bsuir.ksis.dmanager.logic;

import lombok.Data;

/**
 * @author Vladislav Piseckij
 */
@Data
public class Result {
    
    private static final Result SUCCESS = new Result(Status.SUCCESS, "");

    public static Result success() {
        return SUCCESS;
    }

    public static Result fail(String message) {
        return new Result(Status.FAIL, message);
    }
    
    private final Status status;
    
    private final String message;

    public enum Status {
        SUCCESS,
        FAIL
    }
    
}
