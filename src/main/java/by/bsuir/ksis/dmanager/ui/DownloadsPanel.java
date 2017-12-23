package by.bsuir.ksis.dmanager.ui;

import by.bsuir.ksis.dmanager.domain.Download;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;
import java.util.function.Function;

class DownloadsPanel extends JPanel {

    private DownloadsTableModel downloadsTableModel = new DownloadsTableModel();
    private JTable downloadsTable = new JTable(downloadsTableModel);

    DownloadsPanel() {
        JScrollPane scroll = new JScrollPane(downloadsTable);
        downloadsTable.setFillsViewportHeight(true);
        add(scroll, BorderLayout.CENTER);
    }

    public void showDownloads(java.util.List<Download> downloads) {
        downloadsTableModel.setDownloads(downloads);
        downloadsTableModel.fireTableDataChanged();
    }

    private static class DownloadsTableModel extends AbstractTableModel {

        private static final Column[] COLUMNS = new Column[] {
            new Column("Имя", Download::getName),
            new Column("Состояние", Download::getStatus),
            new Column("Приоритет", Download::getPriority),
            new Column("Кол-во файлов", Download::getAllFilesCount),
            new Column("Всего байт", Download::getTotalBytes),
            new Column("Загружено байт", Download::getLoadedBytes)
        };

        private java.util.List<Download> downloads;

        public List<Download> getDownloads() {
            return downloads;
        }

        public void setDownloads(List<Download> downloads) {
            this.downloads = downloads;
        }

        @Override
        public String getColumnName(int column) {
            return COLUMNS[column].getName();
        }

        @Override
        public int getRowCount() {
            return downloads != null ?downloads.size() : 0;
        }

        @Override
        public int getColumnCount() {
            return COLUMNS.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Download download = downloads.get(rowIndex);
            return COLUMNS[columnIndex].getFunction().apply(download);
        }
    }

    @Getter
    @AllArgsConstructor
    private static class Column {
        private final String name;

        private final Function<Download, Object> function;
    }
}
