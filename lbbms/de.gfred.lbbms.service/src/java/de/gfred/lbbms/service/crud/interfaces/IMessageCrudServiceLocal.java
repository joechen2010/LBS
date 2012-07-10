package de.gfred.lbbms.service.crud.interfaces;

import de.gfred.lbbms.service.model.Message;
import java.util.Collection;
import java.util.Date;
import javax.ejb.Local;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.03
 */
@Local
public interface IMessageCrudServiceLocal {

    Long save(final Message message);

    Boolean delete(final Message message);

    Message findById(final Long id);

    Collection<Message> findAll();

    Message findByDate(final Date date);

    Message findByType(final String type);

    Collection<Message> findAllByLocationRange(final Double minLatitude, final Double minLongitude,final Double maxLatitude, final Double maxLongitude);
}
