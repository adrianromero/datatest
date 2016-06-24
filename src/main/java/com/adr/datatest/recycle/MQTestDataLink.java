/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.adr.datatest.recycle;

import com.adr.data.DataException;
import com.adr.data.DataLink;
import com.adr.data.Record;
import com.adr.data.RecordMap;
import com.adr.data.ValuesEntry;
import com.adr.data.ValuesMap;
import com.adr.datatest.MQDataLinkSource;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adrian
 */
public class MQTestDataLink {

    public static void main(String[] args) {
//        DataLinkSourceH2 linksource = new DataLinkSourceH2();
        MQDataLinkSource linksource = new MQDataLinkSource();
        
        try {
            
            linksource.setUpResources();
            
            datanprint(linksource.getDataLink(), "subject", 
                role("t1", "Test1"),
                role("t2", "Test2"),
                role("t3", "Chotapick"),
                role("t4", "Test4"),
                role("t5", "Test5"));
            datanprint(linksource.getDataLink(), "subject",
                delrole("t1"),
                delrole("t2"),
                delrole("t3"),
                delrole("t4"),
                delrole("t5"));
//            datanprint(linksource.getDataLink(), "uservisible", query2());
//            datanprint(linksource.getDataLink(), "subjectbyrole", subjectbyrole());
//            datanprint(linksource.getDataLink(), "userbyname", userbyname());
          
            
            
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
    
    private static void datanprint(DataLink link, String name, Record... records) throws DataException {
        System.out.println(name + ":");
        link.execute(records);              
    }    
    private static void datanprint(DataLink link, String name, Record record) throws DataException {
        System.out.println(name + ":");
        link.execute(record);              
    }    
    
    private static Record role(String id, String name) {
        return new RecordMap(
            new ValuesMap(
                new ValuesEntry("_ENTITY", "role"),
                new ValuesEntry("id", id)),
            new ValuesMap(
                new ValuesEntry("name", name)));          
    }
    private static Record delrole(String id) {
        return new RecordMap(
            new ValuesMap(
                new ValuesEntry("_ENTITY", "role"),
                new ValuesEntry("id", id)),
            null);          
    }
}
