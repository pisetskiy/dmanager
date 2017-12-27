package by.bsuir.ksis.dmanager.ui;

import by.bsuir.ksis.dmanager.domain.Download;
import by.bsuir.ksis.dmanager.domain.File;
import by.bsuir.ksis.dmanager.domain.Priority;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static by.bsuir.ksis.dmanager.ui.Util.*;

class DownloadDialog extends JDialog {

    private JPanel contentPane;
    private java.io.File destination;
    private JFileChooser destinationChooser = new JFileChooser();
    private JButton destinationButton = new JButton();
    private JTextField nameField = new JTextField();
    private JComboBox<Priority> priorityBox = new JComboBox<>(Priority.values());
    private FilesTableModel filesTableModel = new FilesTableModel();
    private JTable filesTable = new JTable(filesTableModel);
    private JButton addFileButton = new JButton("Добавить");
    private JButton deleteFilesButton = new JButton("Удалить");
    private JButton submitButton = new JButton("Сохранить");

    private Download download;
    private Consumer<Download> onSubmit;

    DownloadDialog(Window owner, Consumer<Download> onSubmit) {
        this(
            owner,
            Download.builder()
                .name(getRandomString(8))
                .destination(System.getProperty("user.home"))
                .priority(Priority.NORMAL)
                .files(new ArrayList<>())
                .build(),
            onSubmit
        );
    }

    DownloadDialog(Window owner, Download download, Consumer<Download> onSubmit) {
        super(owner, "Новая загрузка", ModalityType.DOCUMENT_MODAL);
        this.download = download;
        contentPane = new JPanel(new GridBagLayout());
        createMainBlock();
        createSubmitButton();
        add(contentPane, BorderLayout.CENTER);
        defineBehavior();
        setFieldsState();
        pack();
        setResizable(false);
        setLocationRelativeTo(owner);

        this.onSubmit = onSubmit;

    }
    
    private Download getDownload() {
        download.setName(nameField.getText());
        download.setDestination(destination.getAbsolutePath());
        download.setPriority(priorityBox.getItemAt(priorityBox.getSelectedIndex()));
        download.setFiles(filesTableModel.getFiles());
        return download;
    }
    
    private boolean validateDownload(Download download) {

        if (download.getName() == null || download.getName().trim().isEmpty()) {
            showError("Укажите название закачки");

            return false;
        }
        
        if (download.getDestination() == null || download.getDestination().isEmpty()) {
            showError("Укажите папку для сохранения файлов");

            return false;
        }
        
        if (download.getPriority() == null) {
            showError("Укажите приоритет загрузки");

            return false;
        }

        if (download.getFiles() == null || download.getFiles().isEmpty()) {
            showError("Добавьте файлы для загрузки");

            return false;
        }

        for (File file : download.getFiles()) {
            if (file == null) {
                showError("Ошибка при чтении списка файлов");

                return false;
            }

            if (file.getLink() == null || file.getLink().trim().isEmpty()) {
                showError("У некотрых файлов не указана ссылка для загрузки");

                return false;
            }
        }
        
        return true;
    }

    private void createMainBlock() {

        GridBagConstraints nameLabelConstraints = constraintsWithPadding(true);
        nameLabelConstraints.gridx = 0;
        nameLabelConstraints.gridy = 0;
        contentPane.add(new JLabel("Имя"), nameLabelConstraints);

        GridBagConstraints nameFieldConstraints = constraintsWithPadding(false);
        nameFieldConstraints.gridy = 0;
        nameFieldConstraints.gridwidth = 2;
        nameFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
//        nameField.setColumns(30);
        contentPane.add(nameField, nameFieldConstraints);

        GridBagConstraints destinationLabelConstraints = constraintsWithPadding(true);
        destinationLabelConstraints.gridx = 0;
        destinationLabelConstraints.gridy = 1;
        contentPane.add(new JLabel("Загрузить в"), destinationLabelConstraints);

        GridBagConstraints destinationButtonConstraints  = constraintsWithPadding(false);
        destinationButtonConstraints.gridy = 1;
        destinationButtonConstraints.gridwidth = 2;
        destinationButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(destinationButton,destinationButtonConstraints);

        GridBagConstraints priorityLabelConstraints = constraintsWithPadding(true);
        priorityLabelConstraints.gridx = 0;
        priorityLabelConstraints.gridy = 2;
        contentPane.add(new JLabel("Приоритет"), priorityLabelConstraints);

        GridBagConstraints priorityComboBoxConstraints = constraintsWithPadding(false);
        priorityComboBoxConstraints.gridy = 2;
        priorityComboBoxConstraints.gridwidth = 2;
        priorityComboBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(priorityBox, priorityComboBoxConstraints);

        GridBagConstraints filesTableLabelConstraints = constraintsWithPadding(true);
        filesTableLabelConstraints.gridy = 3;
        filesTableLabelConstraints.gridx = 0;
        filesTableLabelConstraints.gridwidth = 1;
        contentPane.add(new JLabel("Файлы"), filesTableLabelConstraints);

        GridBagConstraints addFileButtonConstraints = constraintsWithPadding(false);
        addFileButtonConstraints.gridy = 3;
        addFileButtonConstraints.gridwidth = 1;
        addFileButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(addFileButton, addFileButtonConstraints);

        GridBagConstraints deleteFilesButtonConstraints = constraintsWithPadding(false);
        deleteFilesButtonConstraints.gridy = 3;
        deleteFilesButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        deleteFilesButtonConstraints.gridwidth = 1;
        contentPane.add(deleteFilesButton, deleteFilesButtonConstraints);

        GridBagConstraints filesTableConstraints = constraintsWithPadding(true);
        filesTableConstraints.gridy = 4;
        filesTableConstraints.gridx = 0;
        filesTableConstraints.gridwidth = 3;
        filesTableConstraints.fill = GridBagConstraints.HORIZONTAL;
        JScrollPane filesScroll = new JScrollPane(filesTable);
        filesScroll.setPreferredSize(new Dimension(450, 250));
        filesTable.setFillsViewportHeight(true);
        contentPane.add(filesScroll, filesTableConstraints);
    }

    private void createSubmitButton() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(submitButton);
        add(panel, BorderLayout.SOUTH);
    }
    
    private void defineBehavior() {
        destinationChooser.addActionListener(e -> {
            if (destinationChooser.getSelectedFile() != null) {
                destination = destinationChooser.getSelectedFile();
                destinationButton.setText(directoryName(destination));
            }
        });
        
        destinationButton.addActionListener(e -> {
            destinationChooser.setMultiSelectionEnabled(false);
            destinationChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            destinationChooser.showSaveDialog(this);
        });

        addFileButton.addActionListener(e -> {
            File file = File.builder().created(LocalDateTime.now()).build();
            filesTableModel.addFile(file);
        });

        deleteFilesButton.addActionListener(e -> {
            int[] rows = filesTable.getSelectedRows();
            java.util.List<File> files = Arrays.stream(rows).mapToObj(filesTableModel::getFileByRow).collect(Collectors.toList());
            filesTableModel.deleteFiles(files);
        });
        
        submitButton.addActionListener(e -> {
            Download download = getDownload();
            if (validateDownload(download) && onSubmit != null) {
                onSubmit.accept(download);
            }
        });
    }
    
    private void setFieldsState() {
        destinationChooser.setSelectedFile(new java.io.File(download.getDestination()));
        destinationChooser.approveSelection();
        priorityBox.setSelectedItem(download.getPriority());
        nameField.setText(download.getName());
        filesTableModel.addFiles(download.getFiles());
    }

    private GridBagConstraints constraintsWithPadding(boolean label) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = label ?
            new Insets(5, 10, 5, 5):
            new Insets(5, 5 , 5, 10);

        return c;
    }

}
