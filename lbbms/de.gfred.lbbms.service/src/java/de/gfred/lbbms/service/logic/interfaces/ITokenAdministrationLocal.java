package de.gfred.lbbms.service.logic.interfaces;

import javax.ejb.Local;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.22
 */
@Local
public interface ITokenAdministrationLocal {

    String getToken(final String email, final String password);

    boolean isTokenValid(final Long userId, final String token);

}
