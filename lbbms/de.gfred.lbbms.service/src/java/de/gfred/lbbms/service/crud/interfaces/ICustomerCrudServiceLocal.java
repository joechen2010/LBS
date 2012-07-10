package de.gfred.lbbms.service.crud.interfaces;

import de.gfred.lbbms.service.model.Customer;
import java.util.Collection;
import javax.ejb.Local;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.03
 */
@Local
public interface ICustomerCrudServiceLocal {

    Boolean save(final Customer customer);

    Boolean delete(final Customer customer);

    Customer findById(final Long id);

    Collection<Customer> findAll();

    Customer findByEmail(final String email);

    Customer findByName(final String name);

    Customer findByMobileNr(final String mobilenr);
}
