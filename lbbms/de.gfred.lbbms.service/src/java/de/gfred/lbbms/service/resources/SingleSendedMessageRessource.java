package de.gfred.lbbms.service.resources;

import de.gfred.lbbms.service.logic.interfaces.ICustomerAdministrationLocal;
import de.gfred.lbbms.service.logic.interfaces.IMessageAdministrationLocal;
import de.gfred.lbbms.service.logic.interfaces.ITokenAdministrationLocal;
import de.gfred.lbbms.service.model.Customer;
import de.gfred.lbbms.service.model.Message;
import de.gfred.lbbms.service.representations.MessageRepresentation;
import de.gfred.lbbms.service.resources.util.ResourceValues;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.22
 */
@Path(SingleSendedMessageRessource.URI_TEMPLATE)
@Stateless
public class SingleSendedMessageRessource {
    private static final String TAG = "de.gfred.lbbms.service.resources.SingleSendedMessageRessource";
    private static final boolean DEBUG = false;
    public static final String URI_TEMPLATE = SendMessagesRessource.URI_TEMPLATE + "/{" + ResourceValues.MESSAGE_ID + ": [0-9]+}";

    @EJB
    private ICustomerAdministrationLocal customerBean;

    @EJB
    private IMessageAdministrationLocal messageBean;

    @EJB
    private ITokenAdministrationLocal tokenBean;

    @GET
    @Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public MessageRepresentation getSingleCustomerById(@HeaderParam(ResourceValues.TOKEN) String token, @PathParam(ResourceValues.CUSTOMER_ID) Long id, @PathParam(ResourceValues.MESSAGE_ID) Long msgId){
        if(!tokenBean.isTokenValid(id, token)){
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }

        if(id==null || customerBean.getCustomerById(id)==null || msgId==null || messageBean.getMessageById(msgId)==null ){
            throw new WebApplicationException(Status.NOT_FOUND);
        }

        Message msg = messageBean.getMessageById(msgId);

        return new MessageRepresentation(msg);
    }

    @DELETE
    public void removeMsg(@HeaderParam(ResourceValues.TOKEN) String token, @PathParam(ResourceValues.CUSTOMER_ID) Long id, @PathParam(ResourceValues.MESSAGE_ID) Long msgId){
        if(!tokenBean.isTokenValid(id, token)){
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }

        if(id==null || customerBean.getCustomerById(id)==null || msgId==null || messageBean.getMessageById(msgId)==null ){
            throw new WebApplicationException(Status.NOT_FOUND);
        }

        messageBean.deleteMessage(msgId);
    }
}
