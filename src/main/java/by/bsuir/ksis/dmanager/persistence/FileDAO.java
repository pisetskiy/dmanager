package by.bsuir.ksis.dmanager.persistence;

import by.bsuir.ksis.dmanager.domain.Download;
import by.bsuir.ksis.dmanager.domain.File;
import by.bsuir.ksis.dmanager.domain.Status;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
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

    private static final String SELECT = "" +
        "select\n" +
        "   id, id_download, link, username, password, status, name, message, created\n" +
        "from\n" +
        "   file";

    private static final String SELECT_FOR_EXECUTION = "" +
        SELECT +"\n" +
        "where\n" +
        "   id_download = ?\n" +
        "order by\n" +
        "   created";

    public List<File> getForFilesExecution(Integer downloadId) {
        return jdbcTemplate.query(SELECT_FOR_EXECUTION, new Object[]{downloadId}, FILE_ROW_MAPPER);
    }

    private static final String UPDATE = "" +
        "update\n" +
        "   file\n" +
        "set\n" +
        "   id_download = ?\n" +
        "   ,link = ?\n" +
        "   ,username = ?\n" +
        "   ,password = ?\n" +
        "   ,status = ?\n" +
        "   ,name = ?\n" +
        "   ,message = ?\n" +
        "   ,created = ?\n" +
        "where\n" +
        "   id = ?";

    public void update(File file) {
        jdbcTemplate.update(
            UPDATE,
            file.getDownloadId(),
            file.getLink(),
            file.getUsername(),
            file.getPassword(),
            file.getStatus().name(),
            file.getName(),
            file.getMessage(),
            Timestamp.valueOf(file.getCreated()),
            file.getId()
        );
    }

    private static final String CHECK_ERROR_FILES = "" +
        SELECT + "\n" +
        "where\n" +
        "   id_download = ?\n" +
        "and\n" +
        "   status = 'ERROR'";

    public boolean hasErrorFiles(Download download) {
        return !jdbcTemplate.query(CHECK_ERROR_FILES, new Object[]{download.getId()}, FILE_ROW_MAPPER).isEmpty();
    }

    private static final RowMapper<File> FILE_ROW_MAPPER = (rs, rowNum) -> File.builder()
        .id(rs.getInt("id"))
        .downloadId(rs.getInt("id_download"))
        .link(rs.getString("link"))
        .username(rs.getString("username"))
        .password(rs.getString("password"))
        .status(Status.valueOf(rs.getString("status")))
        .name(rs.getString("name"))
        .message(rs.getString("message"))
        .created(rs.getTimestamp("created").toLocalDateTime())
        .build();
}
