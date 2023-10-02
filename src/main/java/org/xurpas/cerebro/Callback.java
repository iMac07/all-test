package org.xurpas.cerebro;

public class Callback {
    public static void main(String [] args){
        Sample callback = new Sample() {
            @Override
            public boolean WillDelete() {
                return false;
            }
        };
        
        SampleClass cls = new SampleClass();
        cls.setCallback(callback);
        cls.SendCallback();
    }
}

class SampleClass{
    Sample callback;
    
    public void setCallback(Sample foValue){
        callback = foValue;
    }
    
    public void SendCallback(){
        boolean lbCallback = callback.WillDelete();;
        
        System.out.println("Will delete: " + lbCallback);
    }
    
}
interface Sample{
    boolean WillDelete();
}
