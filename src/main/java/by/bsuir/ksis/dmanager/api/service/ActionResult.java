package by.bsuir.ksis.dmanager.api.service;

import lombok.Data;

/**
 * @author Vladislav Piseckij
 */
@Data
public class ActionResult {
    
    private static final ActionResult SUCCESS = new ActionResult(ResultStatus.SUCCESS, "");

    public static ActionResult success() {
        return SUCCESS;
    }

    public static ActionResult fail(String message) {
        return new ActionResult(ResultStatus.FAIL, message);
    }
    
    private final ResultStatus status;
    
    private final String message;
    
}
