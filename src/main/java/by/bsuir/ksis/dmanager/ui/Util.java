package by.bsuir.ksis.dmanager.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

class Util {

    private Util() {
    }
    
    private static final char[] CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("ssmmhhddMMYY");
    private static final Random RND = new Random();
    
    static String createDownloadName() {
        return String.valueOf(CHARS[RND.nextInt(CHARS.length)]) +
            CHARS[RND.nextInt(CHARS.length)] +
            CHARS[RND.nextInt(CHARS.length)] +
            CHARS[RND.nextInt(CHARS.length)] +
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

}
