package de.gfred.lbbms.service.logic.interfaces;

import de.gfred.lbbms.service.model.Message;
import java.util.List;
import javax.ejb.Local;


/**
 *
 * @author Frederik Goetz
 * @date 2011.02.17
 */
@Local
public interface IMessageAdministrationLocal {

    void sendBroadcastMessage(Long sender,String receiver,String msg, Double longitude, Double latitude);
    List<Message> receiveBroadcastMessages(Long id, Double longitude, Double latitude);
    void sendPrivateMessage();
    void sendTwitterMessage();
    void sendFacebookMessage();
    void sendSMS();

    Message getMessageById(final Long msgId);

    Boolean deleteMessage(Long msgId);
    
}
