package de.gfred.lbbms.service.crud.interfaces;

import de.gfred.lbbms.service.model.Token;
import java.util.Collection;
import javax.ejb.Local;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.22
 */
@Local
public interface ITokenCrudServiceLocal {

    Boolean delete(final Token token);

    Collection<Token> findAll();

    Token findById(final Long id);

    Token findByToken(final String token);

    Boolean save(final Token token);
}
