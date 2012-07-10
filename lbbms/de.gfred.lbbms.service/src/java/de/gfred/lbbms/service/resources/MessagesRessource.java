package de.gfred.lbbms.service.resources;

import de.gfred.lbbms.service.logic.interfaces.IMessageAdministrationLocal;
import de.gfred.lbbms.service.logic.interfaces.ICustomerAdministrationLocal;
import de.gfred.lbbms.service.logic.interfaces.ITokenAdministrationLocal;
import de.gfred.lbbms.service.model.Customer;
import de.gfred.lbbms.service.model.Location;
import de.gfred.lbbms.service.model.Message;
import de.gfred.lbbms.service.representations.MessageRepresentation;
import de.gfred.lbbms.service.resources.util.ResourceValues;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
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
@Path(MessagesRessource.URI_TEMPLATE)
public class MessagesRessource {
    private static final String TAG = "de.gfred.lbbms.service.resources.MessagesRessource";
    private static final boolean DEBUG = false;
    public static final String URI_TEMPLATE = SingleCustomerResource.URI_TEMPLATE + "/messages";

    @EJB
    private IMessageAdministrationLocal messagingBean;

    @EJB
    private ICustomerAdministrationLocal customerBean;

    @EJB
    private ITokenAdministrationLocal tokenBean;

    @POST
    @Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
    public Response sendMessage(@HeaderParam(ResourceValues.TOKEN) String token, @PathParam(ResourceValues.CUSTOMER_ID) final Long id, final MessageRepresentation msg){
        if(!tokenBean.isTokenValid(id, token)){
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }

        if(id==null || customerBean.getCustomerById(id)==null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        
        //TODO unterscheide verschiedene msg typen - concept
        messagingBean.sendBroadcastMessage(id,msg.getReceiver(),msg.getContent(),msg.getLocation().getLon(),msg.getLocation().getLat());

        //TODO korrekter respone mit location header 
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public List<MessageRepresentation> getNewMessagesByLocation(@HeaderParam(ResourceValues.TOKEN) String token, @PathParam(ResourceValues.CUSTOMER_ID) final Long id){
        if(!tokenBean.isTokenValid(id, token)){
            throw new WebApplicationException(Status.UNAUTHORIZED);
        }

        if(id==null || customerBean.getCustomerById(id)==null){
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        Location location = customerBean.getCustomerById(id).getCurrentLocation();

        List<Message> list = messagingBean.receiveBroadcastMessages(id, location.getLongitude(), location.getLatitude());
        List<MessageRepresentation> messages = new ArrayList<MessageRepresentation>();
        for(Message msg : list){
            messages.add(new MessageRepresentation(msg));
        }
        
        return messages;
    }
}
