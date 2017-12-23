package by.bsuir.ksis.dmanager.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemsInfo {

    private Integer downloadId;
    private long count;
    private long loadedBytes;
    private long totalBytes;
}
