/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.datatest;

import com.adr.data.rabbitmq.MQDataLinkServer;
import com.adr.data.rabbitmq.MQQueryLinkServer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author adrian
 */
public class MQTestServer {
    
    public static void main(String[] args) {
       try {
            // Run the query server
            MQQueryLinkServer queryserver = createMQQueryLinkServer(System.getProperty("rabbitmq.host"), System.getProperty("rabbitmq.queryqueue"));
            new Thread(() -> {
                try {
                    System.out.println("Query server started.");
                    queryserver.mainloop();
                } catch (IOException ex) {
                    Logger.getLogger(MQTestServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }).start();         
            
            // Run the data server
            MQDataLinkServer dataserver = createMQDataLinkServer(System.getProperty("rabbitmq.host"), System.getProperty("rabbitmq.dataqueue"));
            new Thread(() -> {
                try {
                    System.out.println("Data server started.");
                    dataserver.mainloop();
                } catch (IOException ex) {
                    Logger.getLogger(MQTestServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }).start();
            
        } catch (IOException | TimeoutException ex) {
            Logger.getLogger(MQTestServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static MQQueryLinkServer createMQQueryLinkServer(String host, String queryexchange) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        Connection connection = factory.newConnection();        
        Channel channel = connection.createChannel();

        MQQueryLinkServer server = new MQQueryLinkServer(channel, queryexchange, SourceLink.getQueryLink());

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    server.close();
                    channel.close();
                    connection.close();
                } catch (IOException | TimeoutException ex) {
                    Logger.getLogger(MQTestServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }            
        });

        return server;
    }
    
    public static MQDataLinkServer createMQDataLinkServer(String host, String dataexchange) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        Connection connection = factory.newConnection();        
        Channel channel = connection.createChannel();

        MQDataLinkServer server = new MQDataLinkServer(channel, dataexchange, SourceLink.getDataLink());

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    server.close();
                    channel.close();
                    connection.close();
                } catch (IOException | TimeoutException ex) {
                    Logger.getLogger(MQTestServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }            
        }); 
        
        return server;
    }    
}
