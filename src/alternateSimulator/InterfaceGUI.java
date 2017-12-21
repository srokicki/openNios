package alternateSimulator;

import java.util.Properties;

import openDLX.util.ClockCycleLog;
import openDLX.util.Statistics;

public class InterfaceGUI {


    
    public InterfaceGUI(){
    }

    
    public void clearCycleLog(){
        ClockCycleLog.getCycleLog().clear();
    }

    public void addCycleLog(CycleDescription desc){
    	ClockCycleLog.add(desc);
    }
}

