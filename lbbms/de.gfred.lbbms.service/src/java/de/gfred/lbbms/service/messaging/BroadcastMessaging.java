//package de.gfred.lbbms.service.messaging;
//
//import javax.ejb.ActivationConfigProperty;
//import javax.ejb.MessageDriven;
//import javax.jms.Message;
//import javax.jms.MessageListener;
//
///**
// *
// * @author Frederik Goetz
// * @date 2011.02.17
// */
//@MessageDriven(mappedName = "lbbmstopic", activationConfig =  {
//        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
//        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
//        @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
//        @ActivationConfigProperty(propertyName = "clientId", propertyValue = "BroadcastMessaging"),
//        @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "BroadcastMessaging")
//    })
//public class BroadcastMessaging implements MessageListener {
//
//    public BroadcastMessaging() {
//    }
//
//    public void onMessage(Message message) {
//    }
//
//}
