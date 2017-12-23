package by.bsuir.ksis.dmanager;

import by.bsuir.ksis.dmanager.api.service.DownloadsService;
import by.bsuir.ksis.dmanager.ui.MainWindow;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.swing.*;

import static by.bsuir.ksis.dmanager.ui.Util.openWindow;

@Configuration
@ComponentScan
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(Application.class);
        DownloadsService service = ctx.getBean(DownloadsService.class);
        MainWindow window = new MainWindow(service);
        SwingUtilities.invokeLater(() -> openWindow(window));
    }
}
