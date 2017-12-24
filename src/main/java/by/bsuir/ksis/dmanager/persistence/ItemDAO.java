package by.bsuir.ksis.dmanager.persistence;

import by.bsuir.ksis.dmanager.domain.Item;
import by.bsuir.ksis.dmanager.domain.ItemsInfo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vladislav Piseckij
 */
@Repository
@Transactional
public class ItemDAO extends DAO {
    
    private static final String create = "" +
        "insert into\n" +
        "   item(id_download, link, destination, status, name)\n" +
        "values\n" +
        "   (?, ?, ?, ?, ?)";
    
    public Item create(Item item) {
        Integer id = (Integer)insert(
            con -> {
                PreparedStatement statement = con.prepareStatement(create, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, item.getDownloadId());
                statement.setString(2, item.getLink());
                statement.setString(3, item.getDestination());
                statement.setString(4, item.getStatus().name());
                statement.setString(5, item.getName());

                return statement;
            }
        );
        item.setId(id);
        
        return item;
    }

    private static final String info = "" +
        "select\n" +
        "   id_download, count(id) as items_count, sum(totalBytes) as totalBytes, sum(loadedBytes) as loadedBytes\n" +
        "from\n" +
        "   item\n" +
        "group by \n" +
        "   id_download";

    public Map<Integer, ItemsInfo> itemsInfo() {
        Map<Integer, ItemsInfo> result = new HashMap<>();
        jdbcTemplate.query(info, rs -> {
            ItemsInfo info = ItemsInfo.builder()
                .downloadId(rs.getInt("id_download"))
                .count(rs.getLong("items_count"))
                .totalBytes(rs.getLong("totalBytes"))
                .loadedBytes(rs.getLong("loadedBytes"))
                .build();
            result.put(info.getDownloadId(), info);
        });

        return result;
    }

    private static final String DELETE_BY_DOWNLOAD = "" +
        "delete from\n" +
        "   item\n" +
        "where\n" +
        "   id_download = ?";

    public void deleteByDownload(Integer downloadId) {
        jdbcTemplate.update(DELETE_BY_DOWNLOAD, downloadId);
    }

}
