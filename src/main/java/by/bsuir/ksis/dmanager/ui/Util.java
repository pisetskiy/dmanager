package by.bsuir.ksis.dmanager.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

class Util {

    private Util() {
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

}
