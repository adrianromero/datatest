/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.datatest.recycle;

import com.adr.data.DataException;
import com.adr.data.QueryLink;
import com.adr.data.Record;
import com.adr.data.RecordMap;
import com.adr.data.ValuesEntry;
import com.adr.data.ValuesMap;
import com.adr.data.utils.JSONSerializer;
import com.adr.data.var.VariantBoolean;
import com.adr.datatest.MQQueryLinkSource;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author adrian
 */
public class MQTestQueryLink {

    public static void main(String[] args) {
        MQQueryLinkSource linksource = new MQQueryLinkSource();
//        QueryLinkSourceMQH2 linksource = new QueryLinkSourceMQH2();
        
        try {
            
            linksource.setUpResources();
            
//            querynprint(linksource.getQueryLink(), "subject", query1());
//            querynprint(linksource.getQueryLink(), "uservisible", query2());
//            querynprint(linksource.getQueryLink(), "subjectbyrole", subjectbyrole());
//            querynprint(linksource.getQueryLink(), "userbyname", userbyname());
            querynprint(linksource.getQueryLink(), "roles", roles());
          
            
            
        } catch (DataException ex) {
            Logger.getLogger(MQTestQueryLink.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MQTestQueryLink.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {         
                linksource.tearDownResources();
            } catch (Exception ex) {
                Logger.getLogger(MQTestQueryLink.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private static void querynprint(QueryLink link, String name, Record filter) throws DataException {
        System.out.println(name + ":");
        System.out.println(JSONSerializer.INSTANCE.toSimpleJSON(link.query(filter)));              
    }
    
    private static Record query1() {
        return new RecordMap(
            new ValuesMap(
                new ValuesEntry("_ENTITY", "subject"),
                new ValuesEntry("id")),
            new ValuesMap(
                new ValuesEntry("name::LIKE", "%o%"),
                new ValuesEntry("name")));     
        // [{"_ENTITY":"subject","id":"role","name":"Role"},{"_ENTITY":"subject","id":"permission","name":"Permission"}]
    }
    
    private static Record query2() {
        return new RecordMap(
            new ValuesMap(
                new ValuesEntry("_ENTITY", "user_visible"),
                new ValuesEntry("id")),
            new ValuesMap(
                new ValuesEntry("displayname"),
                new ValuesEntry("name")));        
    }
    
    private static Record subjectbyrole() {
        return new RecordMap(
            new ValuesMap(
                new ValuesEntry("_ENTITY", "subject_byrole"),
                new ValuesEntry("role_id::PARAM", "m"),
                new ValuesEntry("code")),
            new ValuesMap(
                new ValuesEntry("name")));        
    }    
    
    private static Record userbyname() {
        return new RecordMap(
            new ValuesMap(
                new ValuesEntry("_ENTITY", "user_byname"),
                new ValuesEntry("id")),
            new ValuesMap(
                new ValuesEntry("name", "manager"),                
                new ValuesEntry("displayname"),                
                new ValuesEntry("password"),                
                new ValuesEntry("codecard"),                
                new ValuesEntry("role_id"),                
                new ValuesEntry("role"),                
                new ValuesEntry("visible", VariantBoolean.NULL)));        
    }
    
    private static Record roles() {
        return new RecordMap(
            new ValuesMap(
                new ValuesEntry("_ENTITY", "role"),
                new ValuesEntry("id")),
            new ValuesMap(
                new ValuesEntry("name")));     
    }
    
}
