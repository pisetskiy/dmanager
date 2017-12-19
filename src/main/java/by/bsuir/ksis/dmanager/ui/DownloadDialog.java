package by.bsuir.ksis.dmanager.ui;

import javax.swing.*;
import java.awt.*;

class DownloadDialog extends JDialog {

    private JPanel contentPane;
    private JTextArea linksTextArea = new JTextArea();
    private JFileChooser destinationChooser = new JFileChooser();
    private JButton destinationButton = new JButton();
    private JComboBox<String> priorityComboBox = new JComboBox<>();
    private JCheckBox authCheckBox = new JCheckBox("Авторизация");
    private JTextField loginTextField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JCheckBox startCheckBox = new JCheckBox("Сразу запустить");
    private JButton submitButton = new JButton("Сохранить");

    DownloadDialog(Window owner) {
        super(owner, "Новая загрузка", ModalityType.DOCUMENT_MODAL);
        contentPane = new JPanel(new GridBagLayout());
        createMainBlock();
        createAuthBlock();
        createSubmitButton();
        add(contentPane, BorderLayout.CENTER);
        pack();
        setResizable(false);
        setLocationRelativeTo(owner);
        setVisible(true);
        requestFocus();
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
        contentPane.add(new JLabel("Сохранить"), destinationLabelConstraints);

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
        contentPane.add(priorityComboBox, priorityComboBoxConstraints);

        GridBagConstraints startCheckBoxConstraints = constraintsWithPadding(false);
        startCheckBoxConstraints.gridx = 1;
        startCheckBoxConstraints.gridy = 3;
        startCheckBoxConstraints.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(startCheckBox, startCheckBoxConstraints);
    }

    private void createAuthBlock() {
        GridBagConstraints cc = constraintsWithPadding(false);
        cc.gridx = 1;
        cc.gridy = 4;
        cc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(authCheckBox, cc);

        GridBagConstraints lc1 = constraintsWithPadding(true);
        lc1.gridx = 0;
        lc1.gridy = 5;
        cc.gridwidth = 1;
        contentPane.add(new JLabel("Логин"), lc1);

        loginTextField.setColumns(30);
        GridBagConstraints flc = constraintsWithPadding(false);
        flc.gridy = 5;
        flc.gridwidth = 2;
        contentPane.add(loginTextField, flc);

        GridBagConstraints lc2 = constraintsWithPadding(true);
        lc2.gridx = 0;
        lc2.gridy = 6;
        cc.gridwidth = 1;
        contentPane.add(new JLabel("Пароль"), lc2);

        passwordField.setColumns(30);
        GridBagConstraints fpc = constraintsWithPadding(false);
        fpc.gridy = 6;
        fpc.gridwidth = 2;
        contentPane.add(passwordField, fpc);
    }

    private void createSubmitButton() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(submitButton);
        add(panel, BorderLayout.SOUTH);
    }

    private GridBagConstraints constraintsWithPadding(boolean label) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = label ?
            new Insets(5, 10, 5, 5):
            new Insets(5, 5 , 5, 10);

        return c;
    }

}
