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
    
    private static final String select = "" +
        "select\n" +
        "   id, name, priority, status, created, username, password\n" +
        "from\n" +
        "   download";
    
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
        Integer id = (Integer)insert(
            con -> {
                PreparedStatement statement = con.prepareStatement(create, Statement.RETURN_GENERATED_KEYS);
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

    private static final String selectByName = "" +
        select + "\n" +
        "where\n" +
        "   name = ?";

    public Download findByName(String name) {
        List<Download> downloads = jdbcTemplate.query(selectByName, new Object[]{name}, DOWNLOAD_ROW_MAPPER);

        return downloads.isEmpty() ? null : downloads.get(0);
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
