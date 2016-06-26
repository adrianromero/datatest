/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.datatest;

import com.adr.data.DataQueryLink;
import com.adr.data.rabbitmq.MQDataLinkServer;
import com.adr.data.rabbitmq.MQQueryLinkServer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.RpcServer;
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
            DataQueryLink link = SourceLink.createSecureLink();
            
            // Run the query server
            Channel querychannel = SourceLink.getConnection().createChannel();
            MQQueryLinkServer queryserver = new MQQueryLinkServer(querychannel, System.getProperty("rabbitmq.queryqueue"), link);            
            
            // Run the data server
            Channel datachannel = SourceLink.getConnection().createChannel();
            MQDataLinkServer dataserver = new MQDataLinkServer(datachannel, System.getProperty("rabbitmq.dataqueue"), link);        
            
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    close(queryserver);
                    close(dataserver);
                    close(querychannel);
                    close(datachannel);
                }            
            });            
            
            // Run the query server
            new Thread(() -> {
                try {
                    System.out.println("Query server started.");
                    queryserver.mainloop();
                } catch (IOException ex) {
                    Logger.getLogger(MQTestServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }).start();         
            
            // Run the data server
            new Thread(() -> {
                try {
                    System.out.println("Data server started.");
                    dataserver.mainloop();
                } catch (IOException ex) {
                    Logger.getLogger(MQTestServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }).start();
            
        } catch (IOException ex) {
            Logger.getLogger(MQTestServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void close(RpcServer resource) {
        try {
            if (resource != null) {
                resource.close();
            }
        } catch (IOException ex) {
             Logger.getLogger(MQTestServer.class.getName()).log(Level.WARNING, null, ex);
        }   
    }
    
    private static void close(Channel resource) {
        try {
            if (resource != null) {
                resource.close();
            }
        } catch (IOException | TimeoutException ex) {
             Logger.getLogger(MQTestServer.class.getName()).log(Level.WARNING, null, ex);
        }   
    }
}
