package com.nh.micro.datasource.msg;



import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;

import com.nh.micro.datasource.MicroXaDataSource;
import com.nh.micro.datasource.MicroXaDataSourceFactory;

/**
 * 
 * @author ninghao
 *
 */
public class MicroDataSourceJmsReceiver {
	private static Logger log=Logger.getLogger(MicroDataSourceJmsReceiver.class);	
	private String jmsUser=ActiveMQConnection.DEFAULT_USER;
	private String jmsPassword=ActiveMQConnection.DEFAULT_PASSWORD;
	private String jmsUrl="tcp://localhost:61616";
	private String jmsTopicName="micro_xa_topic";
	private String dataSourceId="default";
    public String getDataSourceId() {
		return dataSourceId;
	}
	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}
	public String getJmsUser() {
		return jmsUser;
	}
	public String getJmsTopicName() {
		return jmsTopicName;
	}
	public void setJmsTopicName(String jmsTopicName) {
		this.jmsTopicName = jmsTopicName;
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
	public void init() {
        ConnectionFactory connectionFactory;
        Connection connection = null;
        Session session;
        Destination destination;
        final MessageConsumer consumer;
        connectionFactory = new ActiveMQConnectionFactory(
                jmsUser,
                jmsPassword,
                jmsUrl);
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(Boolean.FALSE,
                    Session.AUTO_ACKNOWLEDGE);
            destination = session.createTopic(jmsTopicName);
            consumer = session.createConsumer(destination);
            Thread workThread=new Thread(new Runnable(){
				@Override
				public void run() {
					try {
						Thread.sleep(10*1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				
					while (true) {
						try{
			                TextMessage message = (TextMessage) consumer.receive(10000);
			                if (null != message) {
			                	String msg=message.getText();
			                	log.debug("receive msg="+msg);
			                	String[] splitArray=msg.split(",");
			                	if(splitArray==null || splitArray.length!=2){
			                		log.error("msg format error");
			                		continue;
			                	}
			                	String cmd=splitArray[0];
			                	String groupid=splitArray[1];
			                	if(cmd.equals("commit")){
			                		log.debug("commit groupid="+groupid);
			                		MicroXaDataSourceFactory.getDataSourceInstance(dataSourceId).commit(groupid);
			                	}else if(cmd.equals("rollback")){
			                		log.debug("rollback groupid="+groupid);
			                		MicroXaDataSourceFactory.getDataSourceInstance(dataSourceId).rollback(groupid);
			                	}else{
			                		log.error("msg format cmd error");
			                		continue;	
			                	}
			                }
						}catch(Exception e){
							log.error(e.toString());
							e.printStackTrace();
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
            });
            workThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }
}
