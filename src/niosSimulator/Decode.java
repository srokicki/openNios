package niosSimulator;

public class Decode {

	private RegisterFile registers;
	private boolean isLast;
	
	private Instruction currentInstruction;
	private boolean isStalled;
	private boolean jumped;

	public Decode(RegisterFile registers){
		this.registers = registers;
		this.isLast = isLast;
	}
	
	public void doStep(){
		//Main job of this part is to access registers and extend/select immediate values
		switch(currentInstruction.getType()){
		case RTYPE:
			currentInstruction.setValueA(registers.get(currentInstruction.getRa()));
			currentInstruction.setValueB(registers.get(currentInstruction.getRb()));
			break;
		case JTYPE:
			currentInstruction.setValueA(new NiosValue32((registers.getPC().getUnsignedValue() & 0xf0000000) + (currentInstruction.getImm26() << 4), false));
		case ITYPE:
			switch(currentInstruction.getOp()){
			case ldbu:
			case ldb:
			case ldhu:
			case ldh:
			case ldw:
			case ldbuio:
			case ldbio:
			case ldhuio:
			case ldhio:
			case ldwio:
				currentInstruction.setValueA(registers.get(currentInstruction.getRa()));
				currentInstruction.setValueB(new NiosValue32(currentInstruction.getImm16(), true));
				break;
			case stb:
			case sth:
			case stw:
			case stbio:
			case sthio:
			case stwio:
				currentInstruction.setValueA(registers.get(currentInstruction.getRa()));
				currentInstruction.setValueB(new NiosValue32((new NiosValue16(currentInstruction.getImm16())).getSignedValue(), true));
				
				currentInstruction.setValueToStore(registers.get(currentInstruction.getRb()));
				break;
			case br:
			case bge:
			case blt:
			case bne:
			case beq:
			case bgeu:
				currentInstruction.setValueA(registers.get(currentInstruction.getRa()));
				currentInstruction.setValueB(registers.get(currentInstruction.getRb()));
				
				currentInstruction.setValueToStore(new NiosValue16(currentInstruction.getImm16()));
				break;
			default:
				currentInstruction.setValueA(registers.get(currentInstruction.getRa()));
				currentInstruction.setValueB(new NiosValue32((new NiosValue16(currentInstruction.getImm16())).getSignedValue(), true));
				break;
			}
		}
	
	
		//For unconditional jumps, we perform the jump now
		jumped = false;
		switch (currentInstruction.getOp()){
		case call:
			currentInstruction.setAluResult(registers.getPC());
			registers.setPC(currentInstruction.getValueA());
			jumped = true;
			break;
		case jmpi:
			registers.setPC(currentInstruction.getValueA());
			jumped = true;
			break;
		case br:
			registers.setPC(new NiosValue32(currentInstruction.getValueToStore().getSignedValue() + currentInstruction.getPC() + 4, false));
			jumped = true;
			break;
		case RTYPE:
			switch (currentInstruction.getOpx()){
			case jmp:
				registers.setPC(currentInstruction.getValueA());
				jumped = true;
				break;
			case callr:
				registers.set(31, new NiosValue32(registers.getPC().getSignedValue(), false));
				registers.setPC(currentInstruction.getValueA());
				jumped = true;
				break;
			default:
				break;	
			}
			break;
		default:
			break;
		
		}
	}
	
	public Instruction getCurrentInstruction(){
		return this.currentInstruction;
	}
	
	public void setCurrentInstruction(Instruction instr){
		this.currentInstruction = instr;
	}
	
	
	public void setStalled(boolean stall){
		this.isStalled = stall;
	}

	public boolean isStalled(){
		return this.isStalled;
	}
	
	public boolean hasJumped(){
		return this.jumped;
	}
}
