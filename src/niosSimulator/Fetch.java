package niosSimulator;

import java.util.ArrayList;

public class Fetch {

	private NiosMemory memory;
	private boolean isLast;
	private Instruction currentInstruction;
	private RegisterFile registers;
	
	public Fetch(RegisterFile registers, NiosMemory memory, boolean isLast){
		this.memory = memory;
		this.isLast = isLast;
		this.registers = registers;
	}
	
	public void doFetch(long cycleNumber){
		long binaryInstruction = this.memory.loadWord(this.registers.getPC().getUnsignedValue()).getUnsignedValue();

		this.currentInstruction = new Instruction(binaryInstruction, this.registers.getPC().getUnsignedValue(), cycleNumber);

		if (!(currentInstruction.getOp().equals(niosOpCode.RTYPE) && currentInstruction.getOpx().equals(niosOpxCode.trap)))
			//If we are not in a trap, we increment PC
			registers.setPC(new NiosValue32(registers.getPC().getUnsignedValue() + 4, false));

	}
	
	public Instruction getCurrentInstruction(){
		return this.currentInstruction;
	}

	public void setCurrentInstruction(Instruction instr) {
		this.currentInstruction = instr;
		
	}
	
}
