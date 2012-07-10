package de.gfred.lbbms.service.representations;

import de.gfred.lbbms.service.model.Message;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/*XML
<message>
    <type><type>
    <content></content>
    <receiver></receiver>
    <location>
        <lat></lat>
        <lon></lon>
    </location>
</message>
 */

/**
 *
 * @author Frederik Goetz
 * @date 2011.02.03
 */
@XmlRootElement(name="message")
public class MessageRepresentation implements Comparable<MessageRepresentation> {
    private static final String TAG = "de.gfred.lbbms.service.representations.MessageRepresentation";
    private static final boolean DEBUG = false;

    private Long id;
    private String type;
    private String content;
    private LocationRepresentation location;
    private String receiver;
    private Date sendDate;
    private String sender;

    public MessageRepresentation(){
    }

    public MessageRepresentation(Message message){
        type = message.getType();
        content = message.getContent();

        location = new LocationRepresentation();
        location.setLat(message.getLocation().getLatitude());
        location.setLon(message.getLocation().getLongitude());

        sendDate = message.getSendDate();
        sender = message.getCustomer().getName();
        receiver = message.getReceiver();
        id = message.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocationRepresentation getLocation() {
        return location;
    }

    public void setLocation(LocationRepresentation location) {
        this.location = location;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(MessageRepresentation o) {
        return o.sendDate.compareTo(sendDate);
    }
}
