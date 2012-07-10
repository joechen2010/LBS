package de.gfred.lbbms.service.logic;

import de.gfred.lbbms.service.crud.interfaces.ICustomerCrudServiceLocal;
import de.gfred.lbbms.service.logic.interfaces.ICustomerAdministrationLocal;
import de.gfred.lbbms.service.model.Customer;
import de.gfred.lbbms.service.model.Location;
import java.util.Collection;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.03
 */
@Stateless
public class CustomerAdministration implements ICustomerAdministrationLocal {
    private static final String TAG = "de.gfred.lbbms.service.logic.CustomerAdministration";
    private static final boolean DEBUG = false;

    @EJB
    private ICustomerCrudServiceLocal customerCrud;

    public CustomerAdministration() {
    }
   

    @Override
    public Boolean verifyCustomer(final String email, final String password) {
        Customer customer = customerCrud.findByEmail(email);
        return customer!=null && customer.getPassword().equals(password);
    }
   
    @Override
    public Boolean saveCurrentLocation(final String email, final Double longitude, final Double latitude) {
        Customer customer = customerCrud.findByEmail(email);

        Location location = new Location();
        location.setLongitude(longitude);
        location.setLatitude(latitude);
        customer.setCurrentLocation(location);
        
        return customerCrud.save(customer);
    }

    @Override
    public Boolean isCustomerAvailable(final String email) {
        return customerCrud.findByEmail(email)==null;
    }
    
    @Override
    public Boolean updateCustomer(final Customer customer) {
        return registerCustomer(customer);
    }

    @Override
    public Boolean registerCustomer(final Customer customer) {
        return customerCrud.save(customer);
    }

    @Override
    public Customer getCustomerByEmail(final String email) {
        return customerCrud.findByEmail(email);
    }

    @Override
    public Collection<Customer> getAllCustomers() {
        return customerCrud.findAll();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerCrud.findById(id);
    }





}
