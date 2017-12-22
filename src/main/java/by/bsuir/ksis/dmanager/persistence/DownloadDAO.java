package by.bsuir.ksis.dmanager.persistence;

import by.bsuir.ksis.dmanager.domain.Download;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * @author Vladislav Piseckij
 */
@Repository
@Transactional
public class DownloadDAO extends DAO {
    
    private static final String select = "" +
        "select\n" +
        "   id, name, priority, status, created, username, password\n" +
        "from\n" +
        "   download";
    
    private static final String selectByName = "" +
        select + "\n" +
        "where\n" +
        "   name = ?";
    
    private static final String selectById = "" +
        select + "\n" +
        "where\n" +
        "   id = ?";
    
    private static final String create = "" +
        "insert into\n" +
        "   download(name, priority, status, created, username, password)\n" +
        "values\n" +
        "   (?, ?, ?, ?, ?, ?)";

    public Download create(Download download) {
        //TODO: will need to rewrite for getting id in one query
        jdbcTemplate.update(create,
            download.getName(),
            download.getPriority().name(),
            download.getStatus().name(),
            Timestamp.valueOf(download.getCreated()),
            download.getUsername(),
            download.getPassword()
        );
        
        jdbcTemplate.query(selectByName, new Object[]{download.getName()}, (ResultSet rs) -> {
            download.setId(rs.getInt("id"));
        });
        
        return download;
    }
}
