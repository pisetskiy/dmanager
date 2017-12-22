package by.bsuir.ksis.dmanager.api.service;

import lombok.Data;

/**
 * @author Vladislav Piseckij
 */
@Data
public class ActionResult {
    
    public static final ActionResult SUCCESS = new ActionResult(ResultStatus.SUCCESS, "");
    
    private final ResultStatus status;
    
    private final String message;
    
}
