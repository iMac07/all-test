package org.xurpas.cerebro;

import org.xurpas.cerebro.base.Property;
import org.xurpas.cerebro.base.SQLConnection;
import org.xurpas.cerebro.base.Synapse;
import org.xurpas.cerebro.crypto.CryptFactory;

public class CreateConnection {
    public static void main(String [] args){
        final String PRODUCTID = "cerebro";
               
        String path;
        if (System.getProperty("os.name").toLowerCase().contains("win")){
            path = "d:/xurpas/";
        } else {
            path = "/srv/icarus/";
        }
        
        System.setProperty("sys.default.path.config", path);
        
        //get database property
        Property loConfig = new Property("synapse.properties", PRODUCTID);
        if (!loConfig.loadConfig()){
            System.err.println(loConfig.getMessage());
            System.exit(1);
        } else System.out.println("Database configuration was successfully loaded.");
        
        //connect to database
        SQLConnection loConn = new SQLConnection();
        loConn.setProperty(loConfig);
        if (loConn.getConnection() == null){
            System.err.println(loConn.getMessage());
            System.exit(1);
        } else
            System.out.println("Connection was successfully initialized.");        
        
        //load application driver
        Synapse loNautilus = new Synapse();
        
        loNautilus.setConnection(loConn);
        loNautilus.setEncryption(CryptFactory.make(CryptFactory.CrypType.AESCrypt));
        
        loNautilus.setUserID("000100210001");
        if (!loNautilus.load(PRODUCTID)){
            System.err.println(loNautilus.getMessage());
            System.exit(1);
        } else
            System.out.println("Application driver successfully initialized.");        
    }
}
