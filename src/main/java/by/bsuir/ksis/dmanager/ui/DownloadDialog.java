package by.bsuir.ksis.dmanager.ui;

import by.bsuir.ksis.dmanager.api.data.CredentialsDTO;
import by.bsuir.ksis.dmanager.api.data.DownloadNewDTO;
import by.bsuir.ksis.dmanager.domain.DownloadPriority;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static by.bsuir.ksis.dmanager.ui.Util.*;

class DownloadDialog extends JDialog {

    private JPanel contentPane;
    private JTextArea linksTextArea = new JTextArea();
    private File destination;
    private JFileChooser destinationChooser = new JFileChooser();
    private JButton destinationButton = new JButton();
    private JTextField nameTextField = new JTextField();
    private JComboBox<DownloadPriority> priorityComboBox = new JComboBox<>(DownloadPriority.values());
    private JCheckBox authCheckBox = new JCheckBox("Авторизация");
    private JTextField loginTextField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JCheckBox startCheckBox = new JCheckBox("Сразу запустить");
    private JButton submitButton = new JButton("Сохранить");
    
    private Consumer<DownloadNewDTO> onSubmit;

    DownloadDialog(Window owner, Consumer<DownloadNewDTO> onSubmit) {
        super(owner, "Новая загрузка", ModalityType.DOCUMENT_MODAL);
        this.onSubmit = onSubmit;
        contentPane = new JPanel(new GridBagLayout());
        createMainBlock();
        createAuthBlock();
        createSubmitButton();
        add(contentPane, BorderLayout.CENTER);
        defineBehavior();
        setFieldsState();
        pack();
        setResizable(false);
        setLocationRelativeTo(owner);
        setVisible(true);
        requestFocus();
    }
    
    private DownloadNewDTO getDownload() {
        java.util.List<String> links = Arrays.stream(linksTextArea.getText().split("\\n"))
            .map(String::trim).filter(link -> !link.isEmpty()).collect(Collectors.toList());
        
        return DownloadNewDTO.builder()
            .links(links)
            .destination(destination)
            .name(nameTextField.getText())
            .priority(priorityComboBox.getItemAt(priorityComboBox.getSelectedIndex()))
            .start(startCheckBox.isSelected())
            .credentials(
                authCheckBox.isSelected() ?
                    new CredentialsDTO(loginTextField.getText(), String.valueOf(passwordField.getPassword())) :
                    null
            )
            .build();
    }
    
    private boolean validateDownload(DownloadNewDTO download) {
        if (download.getLinks() == null || download.getLinks().isEmpty()) {
            return showError("Отсутствуют ссылки на файлы");
        }
        
        if (download.getDestination() == null || !download.getDestination().isDirectory()) {
            return showError("Укажите папку для сохранения файлов");
        }
        
        if (download.getName() == null || download.getName().trim().isEmpty()) {
            return showError("Укажите название закачки");
        }
        
        if (download.getPriority() == null) {
            return showError("Укажите приоритет загрузки");
        }
        
        if (authCheckBox.isSelected() && (loginTextField.getText() == null || loginTextField.getText().trim().isEmpty())) {
            return showError("Укажите логин");
        }
        
        return true;
    }

    private void createMainBlock() {
        GridBagConstraints filesLabelConstraints = constraintsWithPadding(true);
        filesLabelConstraints.gridx = 0;
        filesLabelConstraints.gridy = 0;
        contentPane.add(new JLabel("Файлы"), filesLabelConstraints);

        GridBagConstraints filesScrollConstrints = constraintsWithPadding(false);
        filesScrollConstrints.gridy = 0;
        filesScrollConstrints.gridwidth = 2;
        JScrollPane scrollPane = new JScrollPane(linksTextArea);
        linksTextArea.setColumns(30);
        linksTextArea.setRows(4);
        contentPane.add(scrollPane, filesScrollConstrints);

        GridBagConstraints destinationLabelConstraints = constraintsWithPadding(true);
        destinationLabelConstraints.gridx = 0;
        destinationLabelConstraints.gridy = 1;
        contentPane.add(new JLabel("Сохранить в"), destinationLabelConstraints);

        GridBagConstraints destinationButtonConstraints  = constraintsWithPadding(false);
        destinationButtonConstraints.gridy = 1;
        destinationButtonConstraints.gridwidth = 2;
        destinationButtonConstraints.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(destinationButton,destinationButtonConstraints);
        
        GridBagConstraints nameLabelConstraints = constraintsWithPadding(true);
        nameLabelConstraints.gridx = 0;
        nameLabelConstraints.gridy = 2;
        contentPane.add(new JLabel("Название"), nameLabelConstraints);
        
        GridBagConstraints nameFieldConstraints = constraintsWithPadding(false);
        nameFieldConstraints.gridy = 2;
        nameFieldConstraints.gridwidth = 2;
        nameFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        nameTextField.setColumns(30);
        contentPane.add(nameTextField, nameFieldConstraints);

        GridBagConstraints priorityLabelConstraints = constraintsWithPadding(true);
        priorityLabelConstraints.gridx = 0;
        priorityLabelConstraints.gridy = 3;
        contentPane.add(new JLabel("Приоритет"), priorityLabelConstraints);

        GridBagConstraints priorityComboBoxConstraints = constraintsWithPadding(false);
        priorityComboBoxConstraints.gridy = 3;
        priorityComboBoxConstraints.gridwidth = 2;
        priorityComboBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(priorityComboBox, priorityComboBoxConstraints);

        GridBagConstraints startCheckBoxConstraints = constraintsWithPadding(false);
        startCheckBoxConstraints.gridx = 1;
        startCheckBoxConstraints.gridy = 4;
        startCheckBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(startCheckBox, startCheckBoxConstraints);
    }

    private void createAuthBlock() {
        GridBagConstraints cc = constraintsWithPadding(false);
        cc.gridx = 1;
        cc.gridy = 5;
        cc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(authCheckBox, cc);

        GridBagConstraints lc1 = constraintsWithPadding(true);
        lc1.gridx = 0;
        lc1.gridy = 6;
        cc.gridwidth = 1;
        contentPane.add(new JLabel("Логин"), lc1);

        loginTextField.setColumns(30);
        GridBagConstraints flc = constraintsWithPadding(false);
        flc.gridy = 6;
        flc.gridwidth = 2;
        contentPane.add(loginTextField, flc);

        GridBagConstraints lc2 = constraintsWithPadding(true);
        lc2.gridx = 0;
        lc2.gridy = 7;
        cc.gridwidth = 1;
        contentPane.add(new JLabel("Пароль"), lc2);

        passwordField.setColumns(30);
        GridBagConstraints fpc = constraintsWithPadding(false);
        fpc.gridy = 7;
        fpc.gridwidth = 2;
        contentPane.add(passwordField, fpc);
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
        
        authCheckBox.addActionListener(e -> {
            loginTextField.setEnabled(authCheckBox.isSelected());
            passwordField.setEnabled(authCheckBox.isSelected());
        });
        
        submitButton.addActionListener(e -> {
            DownloadNewDTO download = getDownload();
            if (validateDownload(download) && onSubmit != null) {
                onSubmit.accept(download);
            }
        });
    }
    
    private void setFieldsState() {
        destinationChooser.setSelectedFile(new File(System.getProperty("user.home")));
        destinationChooser.approveSelection();
        priorityComboBox.setSelectedItem(DownloadPriority.NORM);
        nameTextField.setText(createDownloadName());
        startCheckBox.doClick();
        authCheckBox.doClick();
        authCheckBox.doClick();
    }

    private GridBagConstraints constraintsWithPadding(boolean label) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = label ?
            new Insets(5, 10, 5, 5):
            new Insets(5, 5 , 5, 10);

        return c;
    }

}
