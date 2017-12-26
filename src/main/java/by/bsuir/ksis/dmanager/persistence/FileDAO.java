package by.bsuir.ksis.dmanager.persistence;

import by.bsuir.ksis.dmanager.domain.File;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author Vladislav Piseckij
 */
@Repository
@Transactional
public class FileDAO extends DAO {
    
    private static final String create = "" +
        "insert into\n" +
        "   file(id_download, link, username, password, status, name, message, created)\n" +
        "values\n" +
        "   (?, ?, ?, ?, ?, ?, ?, ?)";
    
    public File create(File file) {
        Integer id = (Integer)insert(
            con -> {
                PreparedStatement statement = con.prepareStatement(create, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, file.getDownloadId());
                statement.setString(2, file.getLink());
                statement.setString(3, file.getUsername());
                statement.setString(4, file.getPassword());
                statement.setString(5, file.getStatus().name());
                statement.setString(6, file.getName());
                statement.setString(7, file.getMessage());
                statement.setTimestamp(8, Timestamp.valueOf(file.getCreated()));

                return statement;
            }
        );
        file.setId(id);
        
        return file;
    }

    private static final String DELETE_BY_DOWNLOAD = "" +
        "delete from\n" +
        "   file\n" +
        "where\n" +
        "   id_download = ?";

    public void deleteByDownload(Integer downloadId) {
        Objects.requireNonNull(downloadId);

        jdbcTemplate.update(DELETE_BY_DOWNLOAD, downloadId);
    }

}
