package by.bsuir.ksis.dmanager.ui;

import by.bsuir.ksis.dmanager.domain.Download;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class DownloadsPanel extends JPanel {

    private DownloadsTableModel downloadsTableModel = new DownloadsTableModel();
    private JTable downloadsTable = new JTable(downloadsTableModel);

    DownloadsPanel() {
        JScrollPane scroll = new JScrollPane(downloadsTable);
        scroll.setPreferredSize(new Dimension(640, 480));
        downloadsTable.setFillsViewportHeight(true);
        add(scroll, BorderLayout.CENTER);
    }

    public void showDownloads(java.util.List<Download> downloads) {
        downloadsTableModel.setDownloads(downloads);
        downloadsTableModel.fireTableDataChanged();
    }

    public List<Download> getSelectedDownloads() {
        int[] rows = downloadsTable.getSelectedRows();
        return Arrays.stream(rows)
            .mapToObj(rowIndex -> downloadsTableModel.getDownloadByRow(rowIndex))
            .collect(Collectors.toList());
    }

    @Getter
    @Setter
    private static class DownloadsTableModel extends AbstractTableModel {

        private static final java.util.List<TableColumn<Download>> COLUMNS = Arrays.asList(
            new TableColumn<>("Имя", Download::getName),
            new TableColumn<>("Приоритет", Download::getPriority),
            new TableColumn<>("Состояние", Download::getStatus),
            new TableColumn<>("Сохраниттся в", Download::getDestination)
        );

        private java.util.List<Download> downloads;

        @Override
        public String getColumnName(int column) {
            return COLUMNS.get(column).getName();
        }

        @Override
        public int getRowCount() {
            return downloads != null ?downloads.size() : 0;
        }

        @Override
        public int getColumnCount() {
            return COLUMNS.size();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Download download = downloads.get(rowIndex);
            return COLUMNS.get(columnIndex).getFunction().apply(download);
        }

        Download getDownloadByRow(int rowIndex) {
            return downloads.get(rowIndex);
        }
    }
}
