package by.bsuir.ksis.dmanager.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author Vladislav Piseckij
 */
class DAO {
    
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
