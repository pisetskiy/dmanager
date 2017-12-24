package by.bsuir.ksis.dmanager.persistence;

import by.bsuir.ksis.dmanager.domain.Download;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Vladislav Piseckij
 */
@Repository
@Transactional
public class DownloadDAO extends DAO {
    
    private static final String SELECT = "" +
        "select\n" +
        "   id, name, priority, status, created, username, password\n" +
        "from\n" +
        "   download";
    
    private static final String SELECT_BY_ID = "" +
        SELECT + "\n" +
        "where\n" +
        "   id = ?";
    
    private static final String CREATE = "" +
        "insert into\n" +
        "   download(name, priority, status, created, username, password)\n" +
        "values\n" +
        "   (?, ?, ?, ?, ?, ?)";

    public Download create(Download download) {
        Integer id = (Integer)insert(
            con -> {
                PreparedStatement statement = con.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, download.getName());
                statement.setString(2, download.getPriority().name());
                statement.setString(3, download.getStatus().name());
                statement.setTimestamp(4, Timestamp.valueOf(download.getCreated()));
                statement.setString(5, download.getUsername());
                statement.setString(6, download.getPassword());

                return statement;
            }
        );
        download.setId(id);

        return download;
    }

    private static final String SELECT_BY_NAME = "" +
        SELECT + "\n" +
        "where\n" +
        "   name = ?";

    @Transactional(readOnly = true)
    public Download findByName(String name) {
        List<Download> downloads = jdbcTemplate.query(SELECT_BY_NAME, new Object[]{name}, DOWNLOAD_ROW_MAPPER);

        return downloads.isEmpty() ? null : downloads.get(0);
    }

    @Transactional(readOnly = true)
    public List<Download> list() {
        return jdbcTemplate.query(SELECT, DOWNLOAD_ROW_MAPPER);
    }

    private static final String UPDATE = "" +
        "update\n" +
        "   download\n" +
        "set\n" +
        "   name = ?\n" +
        "   ,priority = ?\n" +
        "   ,status = ?\n" +
        "   ,created = ?\n" +
        "   ,username = ?\n" +
        "   ,password = ?\n" +
        "where\n" +
        "   id = ?";

    public void update(Download download) {
        jdbcTemplate.update(
            UPDATE,
            download.getName(),
            download.getPriority().name(),
            download.getStatus().name(),
            Timestamp.valueOf(download.getCreated()),
            download.getUsername(),
            download.getPassword(),
            download.getId()
        );
    }

    private static final String DELETE = "" +
        "delete from\n" +
        "   download\n" +
        "where\n" +
        "   id = ?";

    public void delete(Integer id) {
        jdbcTemplate.update(DELETE, id);
    }

    private static final RowMapper<Download> DOWNLOAD_ROW_MAPPER = (rs, rowNum) -> Download.builder()
        .id(rs.getInt("id"))
        .name(rs.getString("name"))
        .priority(Download.Priority.valueOf(rs.getString("priority")))
        .status(Download.Status.valueOf(rs.getString("status")))
        .created(rs.getTimestamp("created").toLocalDateTime())
        .username(rs.getString("username"))
        .password(rs.getString("password"))
        .build();
}
