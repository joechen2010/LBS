package de.gfred.lbbms.service.logic;

import de.gfred.lbbms.service.crud.interfaces.IMessageCrudServiceLocal;
import de.gfred.lbbms.service.logic.interfaces.IMessageAdministrationLocal;
import de.gfred.lbbms.service.logic.interfaces.ICustomerAdministrationLocal;
import de.gfred.lbbms.service.logic.interfaces.ILocationAdministrationLocal;
import de.gfred.lbbms.service.model.Customer;
import de.gfred.lbbms.service.model.Location;
import de.gfred.lbbms.service.model.Message;
import de.gfred.lbbms.service.util.ConfigurationValues;
import de.gfred.lbbms.service.util.LocationCalculator;
import de.gfred.lbbms.service.util.MessageType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;


/**
 *
 * @author Frederik Goetz
 * @date 2011.02.17
 */
@Stateless
public class MessageAdministration implements IMessageAdministrationLocal {
    private static final String TAG = "de.gfred.lbbms.service.logic.MessageAdministration";
    private static final boolean DEBUG = false;

    @EJB
    private ICustomerAdministrationLocal customerBean;

    @EJB
    private IMessageCrudServiceLocal messageCrudBean;

    @EJB
    private ILocationAdministrationLocal locationBean;

    @Resource(name = "TopicConnectionFactory")
    private TopicConnectionFactory connectionFactory;

    @Resource(mappedName = "lbbmstopic")
    private Topic broadcastTopic;

    private TopicConnection connection;

    @PostConstruct
    public void openConnection() {
        try {
            connection = connectionFactory.createTopicConnection();
        } catch (JMSException ex) {
            Logger.getLogger(MessageAdministration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PreDestroy
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException ex) {
                Logger.getLogger(MessageAdministration.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void sendBroadcastMessage(Long sender, String receiver, String content, Double longitude, Double latitude) {
        Customer customer = customerBean.getCustomerById(sender);
        
        Location location = new Location();
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        de.gfred.lbbms.service.model.Message message = new de.gfred.lbbms.service.model.Message();
        message.setContent(content);
        message.setLocation(location);
        message.setReceiver(receiver);
        message.setSendDate(new Date());
        message.setType(MessageType.BROADCAST.toString());
        message.setCustomer(customer);
        Long msgID = messageCrudBean.save(message);

        if(msgID!=-1){
            message = messageCrudBean.findById(msgID);
            
            customer.getMessages().add(message);
            customer.setCurrentLocation(location);            
            customerBean.updateCustomer(customer);
        }
//        try {
//            TopicSession session = connection.createTopicSession(true, Session.AUTO_ACKNOWLEDGE);
//            TopicPublisher publisher = session.createPublisher(broadcastTopic);
//
//            TextMessage msg = session.createTextMessage();
//            msg.setText(content);
//            msg.setDoubleProperty(BroadcastMessageKeys.LONGITUDE, longitude);
//            msg.setDoubleProperty(BroadcastMessageKeys.LATITUDE, latitude);
//
//            //msg.setDoubleProperty("range", Double.parseDouble(receiver));
//            msg.setDoubleProperty(BroadcastMessageKeys.RANGE, ConfigurationValues.DEFAULT_RANGE);
//
//            Customer customer = customerBean.getCustomerById(sender);
//            if (customer != null) {
//                msg.setStringProperty(BroadcastMessageKeys.SENDER_NAME, customer.getName());
//            }
//
//            msg.setJMSType(MessageType.BROADCAST.toString());
//            msg.setJMSTimestamp(System.currentTimeMillis());
//            msg.setJMSExpiration(ConfigurationValues.DEFAULT_EXPIRATION_TIME);
//
//            publisher.publish(msg);
//        } catch (JMSException ex) {
//            Logger.getLogger(MessageAdministration.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public List<de.gfred.lbbms.service.model.Message> receiveBroadcastMessages(Long id, Double longitude, Double latitude) {
        Customer customer = customerBean.getCustomerById(id);

        double[] boundings = LocationCalculator.calculateBoundingCoordinates(longitude, latitude, ConfigurationValues.DEFAULT_RANGE);
        double minLat = boundings[0];
        double minLon = boundings[1];
        double maxLat = boundings[2];
        double maxLon = boundings[3];
        Collection<de.gfred.lbbms.service.model.Message> c = messageCrudBean.findAllByLocationRange(minLat, minLon, maxLat, maxLon);
        List<de.gfred.lbbms.service.model.Message> msgs = new ArrayList<de.gfred.lbbms.service.model.Message>();
        for(de.gfred.lbbms.service.model.Message msg : c){
            if(!customer.getReceivedMessages().contains(msg) && !msg.getCustomer().getId().equals(id)){
                msgs.add(msg);
            }
        }

        Collections.sort(msgs);

        customer.addReceivedMessages(msgs);
        customerBean.updateCustomer(customer);

        return msgs;

//      final List<Message> messages = new ArrayList<Message>();
//        try {
//            Customer customer = customerBean.getCustomerById(id);
//
//            if (customer != null) {
//                double[] boundings = LocationCalculator.calculateBoundingCoordinates(longitude, latitude, ConfigurationValues.DEFAULT_RANGE);
//                double minLat = boundings[0];
//                double minLon = boundings[1];
//                double maxLat = boundings[2];
//                double maxLon = boundings[3];
//
//
//
//                String filterQuery = BroadcastMessageKeys.LATITUDE + " >= " + minLat + " AND " + BroadcastMessageKeys.LATITUDE + " <= " + maxLat
//                        + " AND " + BroadcastMessageKeys.LONGITUDE + " >= " + minLon + " AND " + BroadcastMessageKeys.LONGITUDE + " <= " + maxLon;
//
//
//                TopicSession session = connection.createTopicSession(true, Session.AUTO_ACKNOWLEDGE);
//
//                TopicSubscriber subscriber = session.createSubscriber(broadcastTopic,filterQuery,false);
//                //session.createDurableSubscriber(broadcastTopic, customer.getName(), filterQuery, false);
//
//                subscriber.setMessageListener(new MessageListener() {
//
//                    @Override
//                    public void onMessage(Message message) {
//                        messages.add(message);
//                    }
//                });
//
//                subscriber.close();
//            }
//        } catch (JMSException ex) {
//            Logger.getLogger(MessageAdministration.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return messages;
    }



    @Override
    public void sendPrivateMessage() {
        throw new UnsupportedOperationException("Not supported yet!");
    }

    @Override
    public void sendTwitterMessage() {
        throw new UnsupportedOperationException("Not supported yet!");
    }

    @Override
    public void sendFacebookMessage() {
        throw new UnsupportedOperationException("Not supported yet!");
    }

    @Override
    public void sendSMS() {
        throw new UnsupportedOperationException("Not supported yet!");
    }

    @Override
    public Message getMessageById(final Long msgId) {
        return messageCrudBean.findById(msgId);
    }

    public Boolean deleteMessage(Long msgId) {
        return messageCrudBean.delete(messageCrudBean.findById(msgId));
    }


}
