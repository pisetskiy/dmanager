package by.bsuir.ksis.dmanager.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MainWindow extends JFrame {

    private ControlPanel controlPanel = new ControlPanel();
    private DownloadsPanel waitDownloadsPanel = new DownloadsPanel();
    private DownloadsPanel runDownloadsPanel = new DownloadsPanel();
    private DownloadsPanel finishDownloadsPanel = new DownloadsPanel();

    public MainWindow() throws HeadlessException {
        super("Менеджер загрузок");
        add(controlPanel, BorderLayout.NORTH);
        defineControlActions();
        createDownloadsTabs();
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        requestFocus();
    }

    private void defineControlActions() {
        controlPanel.setOnAddButtonClick(e -> new DownloadDialog(this));
    }

    private void createDownloadsTabs() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Ожидают", waitDownloadsPanel);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        tabbedPane.addTab("В процессе", runDownloadsPanel);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        tabbedPane.addTab("Завершены", finishDownloadsPanel);
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
        add(tabbedPane, BorderLayout.CENTER);
    }

}
