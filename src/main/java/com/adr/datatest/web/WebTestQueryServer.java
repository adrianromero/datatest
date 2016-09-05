/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.datatest.web;

import com.adr.data.http.WebSecureLinkServer;
import com.adr.data.security.SecureLink;
import com.adr.datatest.SourceLink;
import spark.Spark;

/**
 *
 * @author adrian
 */
public class WebTestQueryServer {
    
    private static final String SESSIONNAME ="DataSessionName";
    
    public static void main(String[] args) throws Exception {

        WebSecureLinkServer route = SourceLink.createWebSecureLinkServer();
        
        // default port 4567
        Spark.post("/data/:process", (request, response) -> {

            // loads the session or create a new one
            byte[] session = request.session().attribute(SESSIONNAME);
            SecureLink.UserSession usersession = new SecureLink.UserSession();
            if (session != null) {
                usersession.setData(session);
            }
         
            String result = route.handle(request.params(":process"), request.body(), usersession);
         
            // store the session
            request.session().attribute(SESSIONNAME, usersession.getData());

            response.type("application/json; charset=utf-8");
            return result;
        });
    }    
}
