package com.forsrc.utils;


import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.text.MessageFormat;

public final class ActiveMqUtils {

    private ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

    private ConnectionFactory connectionFactory;

    private String url;

    public ActiveMqUtils(String url) {
        connectionFactory = getConnectionFactory(url);
    }

    private ConnectionFactory getConnectionFactory(String url) {

        Connection connection = null;
        connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                url);
        return connectionFactory;
    }

    public synchronized Connection getConnection() throws JMSException {
        Connection connection = threadLocal.get();
        if (connection != null) {
            LogUtils.LOGGER.info(MessageFormat.format("GetConnection form threadLocal: {0}", connection));
            return connection;
        }
        connection = connectionFactory.createConnection();
        threadLocal.set(connection);
        LogUtils.LOGGER.info(MessageFormat.format("New connection : {0}", connection));
        return connectionFactory.createConnection();
    }

    public synchronized void close() throws JMSException {

        Connection connection = threadLocal.get();
        threadLocal.set(null);

        if (connection != null) {
            connection.close();
        }
    }

    public ActiveMqUtils sendMessage(String queueName, ActiveMqUtilsSendMessage activeMqUtilsSendMessage) throws JMSException {
        Connection connection = getConnection();
        connection.start();
        LogUtils.LOGGER.info(MessageFormat.format("Connection start : {0}", connection));
        Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
        LogUtils.LOGGER.info(MessageFormat.format("Session create : {0}", connection));
        Destination destination = session.createQueue(queueName);
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        activeMqUtilsSendMessage.sendMessage(session, producer, destination);
        session.commit();
        LogUtils.LOGGER.info(MessageFormat.format("Session commit : {0}", connection));
        session.close();
        LogUtils.LOGGER.info(MessageFormat.format("Session close : {0}", connection));
        connection.close();
        LogUtils.LOGGER.info(MessageFormat.format("Connection close : {0}", connection));
        return this;
    }

    public ActiveMqUtils sendMessage(String queueName, final String message) throws JMSException {
        return sendMessage(queueName, new ActiveMqUtilsSendMessage() {
            @Override
            public void sendMessage(Session session, MessageProducer producer, Destination destination) throws JMSException {
                TextMessage textMessage = session.createTextMessage(message);
                producer.send(destination, textMessage);
            }
        });
    }

    public ActiveMqUtils sendMessage(String[] topic, ActiveMqUtilsSendMessage activeMqUtilsSendMessage) throws JMSException {
        Connection connection = getConnection();
        connection.start();
        LogUtils.LOGGER.info(MessageFormat.format("Connection start : {0}", connection));
        Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
        LogUtils.LOGGER.info(MessageFormat.format("Session create : {0}", connection));
        for (int i = 0; i < topic.length; i++) {
            Destination destination = session.createTopic(topic[i]);
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            activeMqUtilsSendMessage.sendMessage(session, producer, destination);
        }

        session.commit();
        LogUtils.LOGGER.info(MessageFormat.format("Session commit : {0}", connection));
        session.close();
        LogUtils.LOGGER.info(MessageFormat.format("Session close : {0}", connection));
        connection.close();
        LogUtils.LOGGER.info(MessageFormat.format("Connection close : {0}", connection));
        return this;
    }

    public ActiveMqUtils receiveMessage(String queueName, ActiveMqUtilsReceiveMessage activeMqUtilsReceiveMessage) throws JMSException {
        Connection connection = getConnection();
        connection.start();
        LogUtils.LOGGER.info(MessageFormat.format("Connection start : {0}", connection));
        Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
        LogUtils.LOGGER.info(MessageFormat.format("Session create : {0}", connection));
        Destination destination = session.createQueue(queueName);
        MessageConsumer consumer = session.createConsumer(destination);
        activeMqUtilsReceiveMessage.receiveMessage(session, consumer, destination);
        while (true) {
            TextMessage message = (TextMessage) consumer.receive(2000);
            if (message != null) {
                LogUtils.LOGGER.info(MessageFormat.format("Receive message : {0} --> {1}", message, connection));
                activeMqUtilsReceiveMessage.todo(message);
                break;
            }
        }
        session.commit();
        LogUtils.LOGGER.info(MessageFormat.format("Session commit : {0}", connection));
        session.close();
        LogUtils.LOGGER.info(MessageFormat.format("Session close : {0}", connection));
        connection.close();
        LogUtils.LOGGER.info(MessageFormat.format("Connection colse : {0}", connection));
        return this;
    }

    public ActiveMqUtils receiveMessage(String queueName) throws JMSException {
        return receiveMessage(queueName, new ActiveMqUtilsReceiveMessage() {
            @Override
            public void receiveMessage(Session session, MessageConsumer consumer, Destination destination) throws JMSException {

            }

            @Override
            public void todo(TextMessage message) {
                System.out.print(message);
            }
        });
    }

    public interface ActiveMqUtilsSendMessage {
        void sendMessage(Session session, MessageProducer producer, Destination destination) throws JMSException;
    }

    public interface ActiveMqUtilsReceiveMessage {
        void receiveMessage(Session session, MessageConsumer consumer, Destination destination) throws JMSException;

        void todo(TextMessage message);
    }

}
