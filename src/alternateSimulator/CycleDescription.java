package alternateSimulator;

import java.util.HashMap;

import niosSimulator.Instruction;
import openDLX.gui.GUI_CONST;

public class CycleDescription {

	private HashMap<Instruction, String> pipelineMap;
	private Instruction fetchedInstruction;
	private int cycleNumber;
	private boolean isStall;
	
	public CycleDescription(int cycleNumber){
		this.pipelineMap = new HashMap<Instruction, String>();
		isStall = false;
		this.cycleNumber = cycleNumber;
	}
	
	
	public void addPipelineState(Instruction id, String value){
		this.pipelineMap.put(id, value);
	}
	
	public HashMap<Instruction, String> getPipelineMap(){
		return pipelineMap;
	}
		
	public void setFetchedInstruction(Instruction code){
		this.fetchedInstruction = code;
	}
	
	public Instruction getFetchedInstruction(){
		return this.fetchedInstruction;
	}
	
	public void setStall(boolean stall){
		this.isStall = stall;
	}
	
	public boolean isStall(){
		return this.isStall;
	}
	
	public int getCycleNumber(){
		return this.cycleNumber;
	}
	
}
