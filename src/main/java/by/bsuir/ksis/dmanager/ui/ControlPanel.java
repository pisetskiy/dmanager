package by.bsuir.ksis.dmanager.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static by.bsuir.ksis.dmanager.ui.Util.imageButton;
import static by.bsuir.ksis.dmanager.ui.Util.loadImage;

class ControlPanel extends JPanel {

    private JButton addDownloadButton = imageButton(loadImage("icons/add.png"), "Добавить");
    private JButton startDownloadButton = imageButton(loadImage("icons/start.png"), "Запустить");
    private JButton stopDownloadButton = imageButton(loadImage("icons/stop.png"), "Остановить");
    private JButton editDownloadButton = imageButton(loadImage("icons/edit.png"), "Изменить");
    private JButton deleteDownloadButton = imageButton(loadImage("icons/delete.png"), "Удалить");

    ControlPanel() {
        super(new FlowLayout(FlowLayout.LEFT));

        add(addDownloadButton);
        add(startDownloadButton);
        add(stopDownloadButton);
        add(editDownloadButton);
        add(deleteDownloadButton);
    }

    void addOnAddButtonClick(ActionListener listener) {
        if (listener != null) {
            addDownloadButton.addActionListener(listener);
        }
    }

    void addOnStartButtonClick(ActionListener listener) {
        if (listener != null) {
            startDownloadButton.addActionListener(listener);
        }
    }

    void addOnStopButtonClick(ActionListener listener) {
        if (listener != null) {
            stopDownloadButton.addActionListener(listener);
        }
    }

    void addOnEditButtonClick(ActionListener listener) {
        if (listener != null) {
            editDownloadButton.addActionListener(listener);
        }
    }

    void addOnDeleteButtonClick(ActionListener listener) {
        if (listener != null) {
            deleteDownloadButton.addActionListener(listener);
        }
    }
}
