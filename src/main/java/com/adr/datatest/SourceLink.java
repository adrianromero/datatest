//     Data Access is a Java library to store data
//     Copyright (C) 2016 Adrián Romero Corchado.
//
//     This file is part of Data Access
//
//     Licensed under the Apache License, Version 2.0 (the "License");
//     you may not use this file except in compliance with the License.
//     You may obtain a copy of the License at
//     
//         http://www.apache.org/licenses/LICENSE-2.0
//     
//     Unless required by applicable law or agreed to in writing, software
//     distributed under the License is distributed on an "AS IS" BASIS,
//     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//     See the License for the specific language governing permissions and
//     limitations under the License.

package com.adr.datatest;

import com.adr.data.DataLink;
import com.adr.data.DataQueryLink;
import com.adr.data.QueryLink;
import com.adr.data.security.SecureLink;
import com.adr.data.sql.SQLDataLink;
import com.adr.data.sql.SQLQueryLink;
import com.adr.data.sql.SecureCommands;
import com.adr.data.sql.SentencePut;
import com.adr.data.http.WebSecureLinkServer;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author Eva
 */
public class SourceLink {
     
    private static ComboPooledDataSource cpds = null;
    private static Connection connection = null;
    
    public static DataSource getDataSource() {
        if (cpds == null) {
            try {
                cpds = new ComboPooledDataSource();
                cpds.setDriverClass(System.getProperty("database.driver"));
                cpds.setJdbcUrl(System.getProperty("database.url"));
                cpds.setUser(System.getProperty("database.user"));  
                cpds.setPassword(System.getProperty("database.password"));             
            } catch (PropertyVetoException ex) {
                Logger.getLogger(SourceLink.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        }
        return cpds; 
    }
    
    public static Connection getConnection() {
        if (connection == null) {
            try {
                String host = System.getProperty("rabbitmq.host");
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost(host);            
                connection = factory.newConnection();
            } catch (IOException | TimeoutException ex) {
                Logger.getLogger(SourceLink.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        }
        return connection;
    } 
    
    public static DataLink createDataLink() {
        return new SQLDataLink(getDataSource(), new SentencePut(), SecureCommands.COMMANDS); 
    }
    
    public static QueryLink createQueryLink() {
        return new SQLQueryLink(getDataSource(), SecureCommands.QUERIES);
    }  
    
    public static DataQueryLink createSecureLink() {
        return new SecureLink(
            createQueryLink(),
            createDataLink(),
            new HashSet<>(Arrays.asList("username_visible")), // anonymous res
            new HashSet<>(Arrays.asList("authenticatedres"))); // authenticated res
    }
    
    public static WebSecureLinkServer WebSecureLinkServer() {
        return new WebSecureLinkServer(
            createQueryLink(),
            createDataLink(),
            new HashSet<>(Arrays.asList("username_visible")), // anonymous res
            new HashSet<>(Arrays.asList("authenticatedres"))); // authenticated res
    }
}
