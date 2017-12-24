package by.bsuir.ksis.dmanager.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static by.bsuir.ksis.dmanager.ui.Util.imageButton;
import static by.bsuir.ksis.dmanager.ui.Util.loadImage;

class ControlPanel extends JPanel {

    private JButton addDownloadButton = imageButton(loadImage("icons/add.png"));
    private JButton startDownloadButton = imageButton(loadImage("icons/start.png"));
    private JButton stopDownloadButton = imageButton(loadImage("icons/stop.png"));
    private JButton deleteDownloadButton = imageButton(loadImage("icons/delete.png"));

    ControlPanel() {
        super(new FlowLayout(FlowLayout.LEFT));

        add(addDownloadButton);
        add(startDownloadButton);
        add(stopDownloadButton);
        stopDownloadButton.setEnabled(false);
        add(deleteDownloadButton);
        deleteDownloadButton.setEnabled(false);
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


}
