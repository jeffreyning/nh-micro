package com.nh.micro.datasource.msg;



import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

/**
 * 
 * @author ninghao
 *
 */
public class MicroDataSourceJmsSender {
	private static Logger log=Logger.getLogger(MicroDataSourceJmsSender.class);	
	private static String jmsUser=ActiveMQConnection.DEFAULT_USER;
	private static String jmsPassword=ActiveMQConnection.DEFAULT_PASSWORD;
	private static String jmsUrl="tcp://localhost:61616";
	private static String jmsTopicName="micro_xa_topic";
	private static Integer jmsDeliveryMode=DeliveryMode.NON_PERSISTENT;

    public static Integer getJmsDeliveryMode() {
		return jmsDeliveryMode;
	}

	public static void setJmsDeliveryMode(Integer jmsDeliveryMode) {
		MicroDataSourceJmsSender.jmsDeliveryMode = jmsDeliveryMode;
	}

	public String getJmsUser() {
		return jmsUser;
	}

	public void setJmsUser(String jmsUser) {
		this.jmsUser = jmsUser;
	}

	public String getJmsPassword() {
		return jmsPassword;
	}

	public void setJmsPassword(String jmsPassword) {
		this.jmsPassword = jmsPassword;
	}

	public String getJmsUrl() {
		return jmsUrl;
	}

	public void setJmsUrl(String jmsUrl) {
		this.jmsUrl = jmsUrl;
	}

	public String getJmsTopicName() {
		return jmsTopicName;
	}

	public void setJmsTopicName(String jmsTopicName) {
		this.jmsTopicName = jmsTopicName;
	}

	public static void sendXaMsg(String cmd,String xid) {

        ConnectionFactory connectionFactory;

        Connection connection = null;

        Session session;

        Destination destination;

        MessageProducer producer;

        connectionFactory = new ActiveMQConnectionFactory(
                jmsUser,
                jmsPassword,
                jmsUrl);
        try {

            connection = connectionFactory.createConnection();

            connection.start();

            session = connection.createSession(Boolean.TRUE,
                    Session.AUTO_ACKNOWLEDGE);

            destination = session.createTopic(jmsTopicName);

            producer = session.createProducer(destination);

            producer.setDeliveryMode(jmsDeliveryMode);
            String msg=cmd+","+xid;
            TextMessage message = session.createTextMessage(msg);
            producer.send(message);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection)
                    connection.close();
            } catch (Throwable ignore) {
            }
        }
    }


}
