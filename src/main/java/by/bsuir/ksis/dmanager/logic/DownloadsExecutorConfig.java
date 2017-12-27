package by.bsuir.ksis.dmanager.logic;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
public class DownloadsExecutorConfig {

    @Bean
    public FTPClient ftpClient() {
        return new FTPClient();
    }
    
    @Bean
    public TaskScheduler scheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(2);
        return scheduler;
    }
    
    @Bean
    public SimpleAsyncTaskExecutor executor() {
        return new SimpleAsyncTaskExecutor();
    }
}
