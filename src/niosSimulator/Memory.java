package niosSimulator;

import java.util.ArrayList;

public class Memory {
	private RegisterFile registers;
	private NiosMemory memory;
	private boolean isLast;
	
	private Instruction currentInstruction;
	
	public Memory(RegisterFile registers, NiosMemory memory, boolean isLast){
		this.registers = registers;
		this.isLast = isLast;
		this.memory = memory;
	}
	
	public void doStep(){
		//Main job is to write the operation result in regfile
		if (!isLast){
			switch (currentInstruction.getType()){
			case ITYPE:
				switch (currentInstruction.getOp()){
				case ldbu:
					System.err.println("Operation not implemented in memory stage !");
					break;
				case ldb:
					System.err.println("Operation not implemented in memory stage !");
					break;
				case ldhu:
					System.err.println("Operation not implemented in memory stage !");
					break;
				case ldh:
					currentInstruction.setAluResult(memory.loadHalf(currentInstruction.getAluResult().getUnsignedValue()));
					break;
				case ldw:
					currentInstruction.setAluResult(memory.loadWord(currentInstruction.getAluResult().getUnsignedValue()));
					break;
				case stb:
					System.err.println("Operation not implemented in memory stage !");
					break;
				case sth:
					System.err.println("Operation not implemented in memory stage !");
					break;
				case stw:
					memory.setWord(currentInstruction.getAluResult().getUnsignedValue(), currentInstruction.getValueToStore());
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
		}
	}
	
	public Instruction getCurrentInstruction(){
		return this.currentInstruction;
	}
	
	public void setCurrentInstruction(Instruction instr){
		this.currentInstruction = instr;
	}
}
