package niosSimulator;

public class WriteBack {
	private RegisterFile registers;
	
	private Instruction currentInstruction;
	
	public WriteBack(RegisterFile registers){
		this.registers = registers;
	}
	
	public void doStep(){
		//Main job is to write the operation result in regfile
			int destRegister = currentInstruction.getWrittenRegister();
			if (destRegister != -1)
				registers.set(destRegister, currentInstruction.getAluResult());
	}
	
	public Instruction getCurrentInstruction(){
		return this.currentInstruction;
	}
	
	public void setCurrentInstruction(Instruction instr){
		this.currentInstruction = instr;
	}
}
