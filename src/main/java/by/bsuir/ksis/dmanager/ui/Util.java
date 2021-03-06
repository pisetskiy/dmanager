package by.bsuir.ksis.dmanager.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Util {

    private Util() {
    }
    
    private static final char[] CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase().toCharArray();
    private static final Random RND = new Random();
    
    public static String getRandomString(int length) {
        return IntStream.range(0, length)
            .map(num -> RND.nextInt(CHARS.length))
            .mapToObj(num -> String.valueOf(CHARS[num]))
            .collect(Collectors.joining());
    }

    static ImageIcon loadImage(String file) {
        InputStream resource = Util.class.getClassLoader().getResourceAsStream(file);
        if (resource != null) {
            try {
                Image image = ImageIO.read(resource);
                image = image.getScaledInstance(16, 16, Image.SCALE_DEFAULT);
                return new ImageIcon(image);
            } catch (Exception e) {
                e.printStackTrace();
                // should never happen
            }
        }

        return null;
    }

    static JButton imageButton(ImageIcon image, String title) {
        return new JButton(title, image);
    }
    
    static String directoryName(File directory) {
        String name = "";
        if (directory != null) {
            name = directory.getName();
            if (name.isEmpty()) {
                name = directory.getAbsolutePath();
            }
        }

        return name;
    }
    
    static void showError(String error) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
            null,
            error,
            "Сообщение об ошибке",
            JOptionPane.ERROR_MESSAGE
        ));
    }

    public static void openWindow(Window window) {
        window.setVisible(true);
        window.requestFocus();
    }

}
