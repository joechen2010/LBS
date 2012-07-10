package de.gfred.lbbms.service.logic;

import de.gfred.lbbms.service.crud.interfaces.ILocationCrudServiceLocal;
import de.gfred.lbbms.service.logic.interfaces.ILocationAdministrationLocal;
import de.gfred.lbbms.service.model.Location;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.03
 */
@Stateless
public class LocationAdministration implements ILocationAdministrationLocal {
    private static final String TAG = "de.gfred.lbbms.service.logic.LocationAdministration";
    private static final boolean DEBUG = false;
 
}
