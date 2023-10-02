package org.xurpas.cerebro;

import org.xurpas.cerebro.base.Property;

public class CreateConnectionProperty {
    public static void main(String[] args) {
        Property loProperty = new Property();
        
        loProperty.setProductID("cerebro");
        loProperty.setConfig("MainServer", "localhost");
        loProperty.setConfig("ServerName", "localhost");
        loProperty.setConfig("Database", "Synapse");
        loProperty.setConfig("UserName", "sa");
        loProperty.setConfig("Password", "Atsp,imrtptd");
        loProperty.setConfig("Port", "3307");
        loProperty.setConfig("DBDriver", "MySQL");
        loProperty.setConfig("ClientID", "MTC_A001");
        
        loProperty.save("d:/xurpas/synapse.properties");
    }
}
