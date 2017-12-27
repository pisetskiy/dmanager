package by.bsuir.ksis.dmanager.ui;

import by.bsuir.ksis.dmanager.domain.Download;
import by.bsuir.ksis.dmanager.logic.DownloadsService;
import by.bsuir.ksis.dmanager.logic.DownloadsViewModel;
import by.bsuir.ksis.dmanager.logic.Result;

import javax.swing.*;
import java.awt.*;

import static by.bsuir.ksis.dmanager.ui.Util.openWindow;
import static by.bsuir.ksis.dmanager.ui.Util.showError;

public class MainWindow extends JFrame {

    private final DownloadsService service;
    private final DownloadsViewModel viewModel;

    private ControlPanel controlPanel = new ControlPanel();
    private DownloadsPanel downloadsPanel = new DownloadsPanel();
    private DownloadDialog newDownloadDialog;

    public MainWindow(DownloadsService service, DownloadsViewModel viewModel) throws HeadlessException {
        super("Менеджер загрузок");

        this.service = service;
        this.viewModel = viewModel;

        add(controlPanel, BorderLayout.NORTH);
        add(downloadsPanel, BorderLayout.CENTER);
        defineControlActions();
        pack();
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        viewModel.emitDownloadsListChange();
    }

    private void defineControlActions() {
        controlPanel.addOnAddButtonClick(e -> {
            this.newDownloadDialog = new DownloadDialog(this, this::createNewDownload);
            openWindow(this.newDownloadDialog);
        });

        controlPanel.addOnStartButtonClick(e -> startDownloads(downloadsPanel.getSelectedDownloads()));

        controlPanel.addOnStopButtonClick(e -> stopDownloads(downloadsPanel.getSelectedDownloads()));

        controlPanel.addOnEditButtonClick(e -> {
            Download download = downloadsPanel.getSelectedDownload();
            if (download != null) {
                this.newDownloadDialog = new DownloadDialog(this, download, this::updateDownload );
                openWindow(this.newDownloadDialog);
            }
        });

        controlPanel.addOnDeleteButtonClick(e -> deleteDownloads(downloadsPanel.getSelectedDownloads()));

        viewModel.subscribeOnDownloadsListChange(downloads -> downloadsPanel.showDownloads(downloads));
    }

    private void createNewDownload(final Download download) {
        SwingUtilities.invokeLater(() -> {
            try {
                Result result = service.create(download);
                if (result.isSuccess()) {
                    this.newDownloadDialog.dispose();
                } else {
                    showError(result.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void startDownloads(java.util.List<Download> downloads) {
        SwingUtilities.invokeLater(() -> {
            try {
                Result result = service.startDownloads(downloads);
                if (!result.isSuccess()) {
                    showError(result.getMessage());
                }
            } catch (Exception e) {
               e.printStackTrace();
            }
        });
    }

    private void stopDownloads(java.util.List<Download> downloads) {
        SwingUtilities.invokeLater(() -> {
            try {
                Result result = service.stopDownloads(downloads);
                if (!result.isSuccess()) {
                    showError(result.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void updateDownload(final Download download) {
        SwingUtilities.invokeLater(() -> {
            try {
                Result result = service.updateDownload(download);
                if (result.isSuccess()) {
                    this.newDownloadDialog.dispose();
                } else {
                    showError(result.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void deleteDownloads(java.util.List<Download> downloads) {
        SwingUtilities.invokeLater(() -> {
            try {
                Result result = service.deleteDownloads(downloads);
                if (!result.isSuccess()) {
                    showError(result.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
