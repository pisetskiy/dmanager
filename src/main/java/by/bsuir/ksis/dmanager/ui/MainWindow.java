package by.bsuir.ksis.dmanager.ui;

import by.bsuir.ksis.dmanager.domain.Download;
import by.bsuir.ksis.dmanager.logic.DownloadsService;
import by.bsuir.ksis.dmanager.logic.DownloadsViewModel;
import by.bsuir.ksis.dmanager.logic.NewDownload;
import by.bsuir.ksis.dmanager.logic.Result;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static by.bsuir.ksis.dmanager.ui.Util.openWindow;
import static by.bsuir.ksis.dmanager.ui.Util.showError;

public class MainWindow extends JFrame {

    private static final Predicate<Download> WAIT_ERROR_DOWNLOADS =
        d -> Download.Status.WAIT == d.getStatus() || Download.Status.ERROR == d.getStatus();
    private static final Predicate<Download> RUN_DOWNLOADS = d -> Download.Status.RUN == d.getStatus();
    private static final Predicate<Download> END_DOWNLOADS = d -> Download.Status.END == d.getStatus();

    private final DownloadsService service;
    private final DownloadsViewModel viewModel;

    private ControlPanel controlPanel = new ControlPanel();
    private DownloadsPanel waitDownloadsPanel = new DownloadsPanel();
    private DownloadsPanel runDownloadsPanel = new DownloadsPanel();
    private DownloadsPanel finishDownloadsPanel = new DownloadsPanel();
    private DownloadDialog newDownloadDialog;

    public MainWindow(DownloadsService service, DownloadsViewModel viewModel) throws HeadlessException {
        super("Менеджер загрузок");

        this.service = service;
        this.viewModel = viewModel;

        add(controlPanel, BorderLayout.NORTH);
        defineControlActions();
        createDownloadsTabs();
        setPreferredSize(new Dimension(800, 600));
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        viewModel.emitDownloadsListChange();
    }

    private void defineControlActions() {
        controlPanel.setOnAddButtonClick(e -> {
            this.newDownloadDialog = new DownloadDialog(this, this::createNewDownload);
            openWindow(this.newDownloadDialog);
        });

        viewModel.submitOnDownloadsListChange(downloads -> {
            downloads = downloads.stream()
                .sorted(
                    DownloadByPriorityComparator.INSTANCE
                        .thenComparing(Comparator.comparing(Download::getCreated).reversed())
                )
                .collect(Collectors.toList());
            waitDownloadsPanel.showDownloads(
                downloads.stream().filter(WAIT_ERROR_DOWNLOADS).collect(Collectors.toList())
            );
            runDownloadsPanel.showDownloads(
                downloads.stream().filter(RUN_DOWNLOADS).collect(Collectors.toList())
            );
            finishDownloadsPanel.showDownloads(
                downloads.stream().filter(END_DOWNLOADS).collect(Collectors.toList())
            );
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

    private void createNewDownload(final NewDownload download) {
        SwingUtilities.invokeLater(() -> {
            try {
                Result result = service.create(download);
                if (Result.Status.SUCCESS == result.getStatus()) {
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
