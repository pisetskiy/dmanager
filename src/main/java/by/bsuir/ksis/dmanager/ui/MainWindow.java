package by.bsuir.ksis.dmanager.ui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private ControlPanel controlPanel = new ControlPanel();

    public MainWindow() throws HeadlessException {
        super("Менеджер загрузок");

        add(controlPanel, BorderLayout.NORTH);

        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        requestFocus();
    }
}
