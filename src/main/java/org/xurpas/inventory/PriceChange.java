package org.xurpas.inventory;

import junit.framework.Assert;
import org.xurpas.cerebro.base.Cortex;
import org.xurpas.cerebro.base.MiscUtil;
import org.xurpas.cerebro.base.SQLUtil;
import org.xurpas.cerebro.base.Synapse;
import org.xurpas.cerebro.constants.Message;
import org.xurpas.cerebro.interfaces.XBaseClass;

public class PriceChange {
    Synapse _synapse;
    String _branchcd;
    String _message;
    
    boolean _init_tran;
    
    Cortex _base_class;
    XBaseClass _base_callback;
    
    public PriceChange(Synapse foValue, String fsBranchCd){
        _synapse = foValue;
        _branchcd = fsBranchCd;
    }
    
    public void setMessage(String fsValue){
        _message = fsValue;
    }
    
    public String getMessage(){
        return _message;
    }
    
    public boolean InitTransaction(){
        if (_synapse == null) {
            setMessage("Main application is not set.");
            return false;
        }
        
        if (_branchcd.isEmpty()) _branchcd = (String) _synapse.getBranchConfig("sBranchCd");
        
        //initialize event callback
        initEvent();
        
        _base_class = new Cortex(_synapse, _base_callback);
        
        _base_class.setBranch(_branchcd);
        _base_class.setMasterTable("Price_Change_Master");
        _base_class.setDetailTable("Price_Change_Detail");
        
        _base_class.setMasterQuery(
            "SELECT" +
                "  sTransNox" +
                ", sBranchCd" +
                ", dTransact" +
                ", nEntryNox" +
                ", dEffectve" +
                ", sReferNox" +
                ", sRemarksx" +
                ", sApproved" +
                ", dApproved" +
                ", cTranStat" +
                ", sModified" +
                ", dModified" +
            " FROM " + _base_class.getMasterTable()
        );
        
        _base_class.setDetailQuery(
            "SELECT" +
                "  a.sTransNox" +
                ", a.nEntryNox" +
                ", a.sStockIDx" +
                ", a.nUnitPrce" +
                ", a.nSelPrce1" +
                ", a.nSelPrce2" +
                ", a.nSelPrce3" +
                ", a.nDiscLev1" +
                ", a.nDiscLev2" +
                ", a.nDiscLev3" +
                ", b.sBarCodex" +
                ", b.sDescript" +
            " FROM " + _base_class.getDetailTable() + " a" +
                " LEFT JOIN Inventory b ON a.sStockIDx = b.sStockIDx"
        );
        
        _base_class.setReference("a.sTransNox|a.sTransNox");
        
        if (!_base_class.InitTransaction()) Assert.fail(_base_class.getMessage());
        
        _base_class.setBrowseQuery(0, 
            "SELECT" +
                "  sTransNox" +
                ", sRemarksx" +
                ", dEffectve" +
                ", dTransact" +
            " FROM " + _base_class.getMasterTable()
        );
        _base_class.setBrowseColumn(0, "dTransact»sRemarksx»dEffectve");
        _base_class.setBrowseCriteria(0, "dTransact»sRemarksx»dEffectve");
        _base_class.setBrowseTitle(0, "Date Entry»Remarks»Effectivity");
        _base_class.setBrowseTitle(0, SQLUtil.FORMAT_MEDIUM_DATE + "»@»" +SQLUtil.FORMAT_MEDIUM_DATE);
        
        _base_class.setDetailBrowseQuery(2, 
            "SELECT" +
                "  a.sStockIDx" +
                ", a.sBarCodex" +
                ", a.sDescript" +
            " FROM Inventory a");
        _base_class.setDetailBrowseColumn(2, "sBarCodex»sDescript");
        _base_class.setDetailBrowseCriteria(2, "sBarCodex»sDescript");
        _base_class.setDetailBrowseTitle(2, "Part No.»Description");
        _base_class.setDetailBrowseTitle(2, "@»@");
        
        _init_tran = true;
        
        return true;
    }
    
    
    public boolean NewTransaction(){
        if (!_init_tran) {
            setMessage("Transaction parameters are not initialized.");
            return false;
        }
        
        if (!_base_class.NewTransaction()) {
            setMessage(_base_class.getMessage());
            return false;
        }
        
        return true;
    }
    
    private void initEvent(){
        _base_callback = new XBaseClass() {
            @Override
            public boolean WillDelete() {
                return false;
            }

            @Override
            public boolean Delete() {
                return false;
            }

            @Override
            public boolean DeleteOthers() {
                return false;
            }

            @Override
            public void DeleteComplete() {
            }

            @Override
            public boolean WillSave() {
                return false;
            }

            @Override
            public boolean Save() {
                return false;
            }

            @Override
            public boolean SaveOthers() {
                return false;
            }

            @Override
            public void SaveComplete() {
            }

            @Override
            public boolean WillCancel() {
                return false;
            }

            @Override
            public boolean Cancel() {
                return false;
            }

            @Override
            public boolean CancelOthers() {
                return false;
            }

            @Override
            public void CancelComplete() {
            }

            @Override
            public void LoadOthers() {
            }

            @Override
            public void InitMaster() {
                _base_class.setMaster("sTransNox", MiscUtil.getNextCode(_base_class.getMasterTable(), "sTransNox", true, _synapse.getConnection().getConnection(), _branchcd));
                _base_class.setMaster("sBranchCd", _branchcd);
                _base_class.setMaster("dTransact", _synapse.getServerDate());
                
                System.out.println(_base_class.getMaster("sTransNox"));
                System.out.println(_base_class.getMaster("sBranchCd"));
                System.out.println(_base_class.getMaster("dTransact"));
            }

            @Override
            public void InitDetail(int ItemNo) {
                
            }

            @Override
            public void MasterRetreive(int Index) {
                
            }

            @Override
            public void DetailRetreive(int Index) {
                
            }

            @Override
            public void DisplayConfirmation(Message.Type foType, String fsMessage) {
                
            }
        };
    }
}
