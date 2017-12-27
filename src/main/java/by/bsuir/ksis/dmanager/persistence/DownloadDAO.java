package by.bsuir.ksis.dmanager.persistence;

import by.bsuir.ksis.dmanager.domain.Download;
import by.bsuir.ksis.dmanager.domain.File;
import by.bsuir.ksis.dmanager.domain.Priority;
import by.bsuir.ksis.dmanager.domain.Status;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DownloadDAO extends DAO {

    @Autowired
    private FileDAO fileDAO;
    
    private static final String CREATE = "" +
        "insert into\n" +
        "   download(name, destination, priority, status, created, message)\n" +
        "values\n" +
        "   (?, ?, ?, ?, ?, ?)";

    public void create(Download download) {
        Integer id = (Integer)insert(
            con -> {
                PreparedStatement statement = con.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, download.getName());
                statement.setString(2, download.getDestination());
                statement.setInt(3, download.getPriority().getOrder());
                statement.setString(4, download.getStatus().name());
                statement.setTimestamp(5, Timestamp.valueOf(download.getCreated()));
                statement.setString(6, download.getMessage());

                return statement;
            }
        );
        download.setId(id);
    }

    private static final String SELECT = "" +
        "select\n" +
        "   id, name, destination, priority, status, created, message\n" +
        "from\n" +
        "   download";

    private static final String SELECT_BY_NAME = "" +
        SELECT + "\n" +
        "where\n" +
        "   name = ?";

    @Transactional(readOnly = true)
    public Download findByName(String name) {
        List<Download> downloads = jdbcTemplate.query(SELECT_BY_NAME, new Object[]{name}, DOWNLOAD_ROW_MAPPER);

        return downloads.isEmpty() ? null : downloads.get(0);
    }

    private static final String LIST = "" +
        SELECT + "\n" +
        "order by\n" +
        "   priority, created";

    @Transactional(readOnly = true)
    public List<Download> list() {
        List<Download> downloads = jdbcTemplate.query(LIST, DOWNLOAD_ROW_MAPPER);
        for (Download download : downloads) {
            List<File> files = fileDAO.getForFilesExecution(download.getId());
            download.setFiles(files);
        }

        return downloads;
    }

    private static final String UPDATE = "" +
        "update\n" +
        "   download\n" +
        "set\n" +
        "   name = ?\n" +
        "   ,destination = ?\n" +
        "   ,priority = ?\n" +
        "   ,status = ?\n" +
        "   ,created = ?\n" +
        "   ,message = ?\n" +
        "where\n" +
        "   id = ?";

    public void update(Download download) {
        jdbcTemplate.update(
            UPDATE,
            download.getName(),
            download.getDestination(),
            download.getPriority().getOrder(),
            download.getStatus().name(),
            Timestamp.valueOf(download.getCreated()),
            download.getMessage(),
            download.getId()
        );
    }

    private static final String DELETE = "" +
        "delete from\n" +
        "   download\n" +
        "where\n" +
        "   id = ?";

    public void delete(Integer id) {
        Objects.requireNonNull(id);

        jdbcTemplate.update(DELETE, id);
    }

    private static final String SELECT_FOR_EXECUTION = "" +
        SELECT + "\n" +
        "where\n" +
        "   status = 'RUN'" +
        "order by\n" +
        "   priority, created\n" +
        "limit\n" +
        "   1";

    public Download getDownloadForExecution() {
        List<Download> downloads = jdbcTemplate.query(SELECT_FOR_EXECUTION, DOWNLOAD_ROW_MAPPER);

        return downloads.isEmpty() ? null : downloads.get(0);
    }
    
    private static final String SELECT_RUN_DOWNLOADS = "" +
        SELECT + "\n" +
        "where\n" +
        "   status = 'RUN'" +
        "order by\n" +
        "   priority, created";
    
    public List<Download> getRunDownloads() {
        return jdbcTemplate.query(SELECT_RUN_DOWNLOADS, DOWNLOAD_ROW_MAPPER);
    }

    private static final RowMapper<Download> DOWNLOAD_ROW_MAPPER = (rs, rowNum) -> Download.builder()
        .id(rs.getInt("id"))
        .name(rs.getString("name"))
        .destination(rs.getString("destination"))
        .priority(Priority.fromOrder(rs.getInt("priority")))
        .status(Status.valueOf(rs.getString("status")))
        .created(rs.getTimestamp("created").toLocalDateTime())
        .message(rs.getString("message"))
        .build();
}
