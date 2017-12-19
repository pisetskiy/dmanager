package by.bsuir.ksis.dmanager;

import by.bsuir.ksis.dmanager.ui.MainWindow;

import javax.swing.*;

public class Application {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
