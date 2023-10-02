package inventory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xurpas.cerebro.base.Property;
import org.xurpas.cerebro.base.SQLConnection;
import org.xurpas.cerebro.base.Synapse;
import org.xurpas.cerebro.crypto.CryptFactory;
import org.xurpas.inventory.PriceChange;

public class testPriceChange {
    Synapse _synapse;
    PriceChange _trans;
    
    @Before
    public void setUpConnection() {
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
        _synapse = new Synapse();
        
        _synapse.setConnection(loConn);
        _synapse.setEncryption(CryptFactory.make(CryptFactory.CrypType.AESCrypt));
        
        _synapse.setUserID("000100210001");
        if (!_synapse.load(PRODUCTID)){
            System.err.println(_synapse.getMessage());
            System.exit(1);
        } else
            System.out.println("Main application successfully initialized.");
        
        _trans = new PriceChange(_synapse, (String) _synapse.getBranchConfig("sBranchCd"));
    }

    @Test
    public void runTestsInSpecificOrder() {
        InitTransaction();
        NewTransaction();
    }

    public void InitTransaction() {
        if (!_trans.InitTransaction()) {
            Assert.fail(_trans.getMessage());
        }
    }
    public void NewTransaction() {
        if (!_trans.NewTransaction()) {
            Assert.fail(_trans.getMessage());
        }
    }
    
    public void TestEncode(){
    }
}
