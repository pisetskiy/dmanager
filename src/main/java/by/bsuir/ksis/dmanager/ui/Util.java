package by.bsuir.ksis.dmanager.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

class Util {

    private Util() {
    }
    
    private static final char[] CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase().toCharArray();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
    private static final Random RND = new Random();
    
    static String createDownloadName() {
        return String.valueOf(CHARS[RND.nextInt(CHARS.length)]) +
            CHARS[RND.nextInt(CHARS.length)] +
            CHARS[RND.nextInt(CHARS.length)] +
            CHARS[RND.nextInt(CHARS.length)] +
            '-' +
            LocalDateTime.now().format(DATE_TIME_FORMATTER)
        ;
    }

    static ImageIcon loadImage(String file) {
        InputStream resource = Util.class.getClassLoader().getResourceAsStream(file);
        if (resource != null) {
            try {

                return new ImageIcon(ImageIO.read(resource));
            } catch (IOException e) {
                e.printStackTrace();
                // should never happen
            }
        }

        return null;
    }

    static JButton imageButton(ImageIcon image) {
        return new JButton(image);
    }
    
    static String directoryName(File directory) {
        return directory != null ? directory.getName() : "";
    }
    
    static boolean showError(String error) {
        JOptionPane.showMessageDialog(null, error, "Сообщение об ошибке", JOptionPane.ERROR_MESSAGE);
        
        return false;
    }

}
