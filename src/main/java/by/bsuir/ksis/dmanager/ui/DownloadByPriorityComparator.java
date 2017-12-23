package by.bsuir.ksis.dmanager.ui;

import by.bsuir.ksis.dmanager.domain.Download;

import java.util.Comparator;

public class DownloadByPriorityComparator implements Comparator<Download> {

    public static final DownloadByPriorityComparator INSTANCE = new DownloadByPriorityComparator();

    @Override
    public int compare(Download o1, Download o2) {
        return Comparator.comparing(Download.Priority::position).compare(o1.getPriority(), o2.getPriority());
    }
}
