package by.bsuir.ksis.dmanager.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.sqlite.JDBC;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;

/**
 * @author Vladislav Piseckij
 */
@Configuration
@EnableTransactionManagement
public class Config {
    
    @Bean
    public DatabasePopulator populator() {
        ClassPathResource script = new ClassPathResource("scripts/init.sql");
        return new ResourceDatabasePopulator(false, false, null, script);
    }
    
    @Bean
    public DataSource dataSource(DatabasePopulator populator) throws IOException {
        File dbFile = new File(System.getProperty("user.home"), "dmanager.sqlite");
        if (!dbFile.exists()) {
            dbFile.createNewFile();
        }
        String url = JDBC.PREFIX + dbFile.getAbsolutePath();
        SQLiteConnectionPoolDataSource dt = new SQLiteConnectionPoolDataSource();
        dt.setUrl(url);
        DatabasePopulatorUtils.execute(populator, dt);
        
        return dt;
    }
    
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    
}
