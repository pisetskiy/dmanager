package by.bsuir.ksis.dmanager.persistence;

import by.bsuir.ksis.dmanager.domain.Item;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * @author Vladislav Piseckij
 */
@Repository
@Transactional
public class ItemDAO extends DAO {
    
    private static final String create = "" +
        "insert into\n" +
        "   item(id_download, link, destination, status)\n" +
        "values\n" +
        "   (?, ?, ?, ?)";
    
    public Item create(Item item) {
        //TODO: reurn id of item
        Integer id = (Integer)insert(
            con -> {
                PreparedStatement statement = con.prepareStatement(create, Statement.RETURN_GENERATED_KEYS);
                statement.setInt(1, item.getDownloadId());
                statement.setString(2, item.getLink());
                statement.setString(3, item.getDestination());
                statement.setString(4, item.getStatus().name());

                return statement;
            }
        );
        item.setId(id);
        
        return item;
    }

}
