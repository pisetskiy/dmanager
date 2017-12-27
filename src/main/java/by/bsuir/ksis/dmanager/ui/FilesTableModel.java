package by.bsuir.ksis.dmanager.ui;

import by.bsuir.ksis.dmanager.domain.File;
import lombok.Getter;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
public class FilesTableModel extends AbstractTableModel {

    private static final java.util.List<TableColumn<File>> COLUMNS = Arrays.asList(
        new TableColumn<>("Ссылка", File::getLink),
        new TableColumn<>("Логин", File::getUsername),
        new TableColumn<>("Пароль", File::getPassword),
        new TableColumn<>("Состояние", File::getStatus),
        new TableColumn<>("Сообщение", File::getMessage)
    );

    private java.util.List<File> files = new ArrayList<>();

    public void addFile(File file) {
        files.add(file);
        fireTableDataChanged();
    }

    public void addFiles(List<File> files) {
        this.files.addAll(files);
        fireTableDataChanged();
    }

    public void deleteFiles(java.util.List<File> files) {
        this.files.removeAll(files);
        fireTableDataChanged();
    }

    public File getFileByRow(int rowIndex) {
        return files.get(rowIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex < 3 ;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex >= files.size()) return;

        File file = files.get(rowIndex);
        switch (columnIndex) {
            case 0:
                file.setLink(Objects.toString(aValue, ""));
                break;
            case 1:
                file.setUsername(Objects.toString(aValue, ""));
                break;
            case 2:
                file.setPassword(Objects.toString(aValue, ""));
                break;
        }
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS.get(column).getName();
    }

    @Override
    public int getRowCount() {
        return files.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        File file = files.get(rowIndex);
        return COLUMNS.get(columnIndex).getFunction().apply(file);
    }

}
