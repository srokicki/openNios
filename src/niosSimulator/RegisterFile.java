package niosSimulator;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterFile {

	private NiosValue32 pc;
	private HashMap<Integer, NiosValue32> registers;
	
	public RegisterFile(){
		this.registers = new HashMap<Integer, NiosValue32>();
		this.pc = new NiosValue32(0, false);
	}
	
	public NiosValue32 get(int index){
		if (!registers.containsKey(index))
			return new NiosValue32(0, false);
		
		return registers.get(index).copy();
	}
	
	public NiosValue32 getPC(){
		return this.pc.copy();
	}
	
	public void setPC(NiosValue32 value){
		this.pc = value;
	}
	
	public void set(int place, NiosValue32 value){
		this.registers.put(place, value);
	}
}
