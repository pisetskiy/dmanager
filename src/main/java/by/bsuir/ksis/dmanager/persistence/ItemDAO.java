package by.bsuir.ksis.dmanager.persistence;

import by.bsuir.ksis.dmanager.domain.Item;
import org.springframework.stereotype.Repository;

/**
 * @author Vladislav Piseckij
 */
@Repository
public class ItemDAO extends DAO {
    
    private static final String create = "" +
        "insert into\n" +
        "   item(id_download, link, destination, status)\n" +
        "values\n" +
        "   (?, ?, ?, ?)";
    
    public Item create(Item item) {
        //TODO: reurn id of item
        
        jdbcTemplate.update(create,
            item.getDownloadId(),
            item.getLink(),
            item.getDestination(),
            item.getStatus().name()
        );
        
        return item;
    }

}
