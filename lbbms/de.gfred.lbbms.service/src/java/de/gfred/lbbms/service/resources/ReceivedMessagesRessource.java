package de.gfred.lbbms.service.resources;


import de.gfred.lbbms.service.logic.interfaces.ICustomerAdministrationLocal;
import de.gfred.lbbms.service.logic.interfaces.ITokenAdministrationLocal;
import de.gfred.lbbms.service.model.Message;
import de.gfred.lbbms.service.representations.MessageRepresentation;
import de.gfred.lbbms.service.resources.util.ResourceValues;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.16
 */
@Stateless
@Path(ReceivedMessagesRessource.URI_TEMPLATE)
public class ReceivedMessagesRessource {
    private static final String TAG = "de.gfred.lbbms.service.resources.ReceivedMessagesRessource";
    private static final boolean DEBUG = false;
    public static final String URI_TEMPLATE = MessagesRessource.URI_TEMPLATE + "/receive";

    @EJB
    private ICustomerAdministrationLocal customerBean;

    @EJB
    private ITokenAdministrationLocal tokenBean;

    @GET   
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public List<MessageRepresentation> getNewMessagesByLocation(@HeaderParam(ResourceValues.TOKEN) String token, @PathParam(ResourceValues.CUSTOMER_ID) final Long id){
        if(!tokenBean.isTokenValid(id, token)){
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }

        if(id==null || customerBean.getCustomerById(id)==null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        Set<Message> receivedMessages = customerBean.getCustomerById(id).getReceivedMessages();
        List<MessageRepresentation> messages = new ArrayList<MessageRepresentation>();
        
        for(Message msg : receivedMessages){
            messages.add(new MessageRepresentation(msg));
        }

        Collections.sort(messages);

        return messages;
    }
}
