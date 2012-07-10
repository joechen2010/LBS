package de.gfred.lbbms.service.logic.interfaces;

import de.gfred.lbbms.service.model.Customer;
import java.util.Collection;
import javax.ejb.Local;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.03
 */
@Local
public interface ICustomerAdministrationLocal {

    Boolean verifyCustomer(final String email, final String password);

    Boolean saveCurrentLocation(final String email, final Double longitude, final Double latitude);

    Boolean isCustomerAvailable(final String email);

    Boolean registerCustomer(final Customer customer);

    Customer getCustomerByEmail(final String email);

    Collection<Customer> getAllCustomers();

    Customer getCustomerById(Long id);

    Boolean updateCustomer(final Customer customer);
    
}
