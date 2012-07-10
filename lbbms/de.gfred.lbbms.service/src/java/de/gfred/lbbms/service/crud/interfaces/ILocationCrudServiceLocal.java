package de.gfred.lbbms.service.crud.interfaces;

import de.gfred.lbbms.service.model.Location;
import java.util.Collection;
import javax.ejb.Local;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.03
 */
@Local
public interface ILocationCrudServiceLocal {
    Boolean save(final Location location);

    Boolean delete(final Location location);

    Location findById(final Long id);

    Collection<Location> findAll();

    Location getLocationByLongAndLat(final long longitude, final long latitude);
}
