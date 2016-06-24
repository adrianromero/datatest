/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.datatest;

import com.adr.data.DataLink;
import com.adr.data.rabbitmq.MQDataLink;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 *
 * @author adrian
 */
public class MQDataLinkSource {
    
    private Connection connection = null;
    private Channel channel = null;
    private MQDataLink link = null;
    
    public void setUpResources() throws Exception {       
        
        String host = "localhost";
        String exchange = "exdatalink";
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        connection = factory.newConnection();
        channel = connection.createChannel();
        link = new MQDataLink(channel, exchange);  
    }

    public void tearDownResources() throws Exception {
        
        if (link != null) {
            link.close();
            link = null;
        }
        if (channel != null) {
            channel.close();
            channel = null;
        }
        if (connection != null) {
            connection.close();
            connection = null;
        }      
    }
    
    public DataLink getDataLink() {
        return link;
    }      
}
