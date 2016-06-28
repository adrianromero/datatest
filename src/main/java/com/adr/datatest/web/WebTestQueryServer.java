/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.datatest.web;

import com.adr.data.http.WebSecureLinkServer;
import com.adr.datatest.SourceLink;
import spark.Spark;

/**
 *
 * @author adrian
 */
public class WebTestQueryServer {
    public static void main(String[] args) throws Exception {

        WebSecureLinkServer route = SourceLink.WebSecureLinkServer();
        
        // default port 4567
//        Spark.get("/data/:message", route);
//        Spark.post("/data", route);
        Spark.post("/data/:process", (request, response) -> {
            return route.handle(request, response);
        });
    }    
}
