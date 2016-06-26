/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.datatest.web;

import com.adr.data.http.WebQueryLinkServer;
import static spark.Spark.get;

/**
 *
 * @author adrian
 */
public class WebTestQueryServer {
    public static void main(String[] args) throws Exception {
        
//        QueryLinkSourceH2 linksource = new QueryLinkSourceH2();
//        linksource.setUpResources();
        
        // default port 4567
        get("/:message", new WebQueryLinkServer(null));
        

    }    
}
