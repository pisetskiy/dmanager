package by.bsuir.ksis.dmanager.ui;

import by.bsuir.ksis.dmanager.api.data.DownloadNewDTO;
import by.bsuir.ksis.dmanager.api.service.ActionResult;
import by.bsuir.ksis.dmanager.api.service.DownloadsService;
import by.bsuir.ksis.dmanager.api.service.ResultStatus;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

import static by.bsuir.ksis.dmanager.ui.Util.openWindow;
import static by.bsuir.ksis.dmanager.ui.Util.showError;

public class MainWindow extends JFrame {
    
    private final DownloadsService service;

    private ControlPanel controlPanel = new ControlPanel();
    private DownloadsPanel waitDownloadsPanel = new DownloadsPanel();
    private DownloadsPanel runDownloadsPanel = new DownloadsPanel();
    private DownloadsPanel finishDownloadsPanel = new DownloadsPanel();
    private DownloadDialog newDownloadDialog;

    public MainWindow(DownloadsService service) throws HeadlessException {
        super("Менеджер загрузок");
        add(controlPanel, BorderLayout.NORTH);
        defineControlActions();
        createDownloadsTabs();
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        this.service = service;
    }

    private void defineControlActions() {
        controlPanel.setOnAddButtonClick(e -> {
            this.newDownloadDialog = new DownloadDialog(this, this::createNewDownload);
            openWindow(this.newDownloadDialog);
        });
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

    private void createNewDownload(final DownloadNewDTO download) {
        SwingUtilities.invokeLater(() -> {
            try {
                ActionResult result = service.create(download);
                if (ResultStatus.SUCCESS == result.getStatus()) {
                    this.newDownloadDialog.dispose();
                } else {
                    showError(result.getMessage());
                }
            } catch (Exception e) {
                showError(e.getMessage());
            }
        });
    }

}
