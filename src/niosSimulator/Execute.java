package niosSimulator;

import java.util.ArrayList;

public class Execute {

	private boolean isLast;
	private boolean hasJumped;
	private Instruction currentInstruction;
	private RegisterFile registers;

	
	public Execute(RegisterFile registers, boolean isLast){
		this.isLast = isLast;
		this.registers = registers;
	}
	
	public void doStep(){
		this.hasJumped = false;
		if (isLast){
			switch(currentInstruction.getOp()){
			case RTYPE:
				switch (currentInstruction.getOpx()){
				case _break:
					System.err.println("Execution of the instruction is not implemented yet !");
					break;
				case add:
					currentInstruction.setAluResult(new NiosValue32(currentInstruction.getValueA().getSignedValue() + currentInstruction.getValueB().getSignedValue(), true));
					break;
				case and:
					currentInstruction.setAluResult(new NiosValue32(currentInstruction.getValueA().getSignedValue() & currentInstruction.getValueB().getSignedValue(), true));
					break;
				case bret:
					System.err.println("Execution of the instruction is not implemented yet !");
					break;
				case cmpeq:
					break;
				case cmpge:
					break;
				case cmpgeu:
					break;
				case cmplt:
					break;
				case cmpltu:
					break;
				case cmpne:
					break;
				case div:
					currentInstruction.setAluResult(new NiosValue32(currentInstruction.getValueA().getSignedValue() / currentInstruction.getValueB().getSignedValue(), true));
					break;
				case divu:
					currentInstruction.setAluResult(new NiosValue32(currentInstruction.getValueA().getUnsignedValue() / currentInstruction.getValueB().getUnsignedValue(), true));
					break;
				case eret:
					System.err.println("Execution of the instruction is not implemented yet !");
					break;
				case flushi:
					System.err.println("Execution of the instruction is not implemented yet !");
					break;
				case flushp:
					System.err.println("Execution of the instruction is not implemented yet !");
					break;
				case initi:
					System.err.println("Execution of the instruction is not implemented yet !");
					break;
				case mul:
					currentInstruction.setAluResult(new NiosValue32(currentInstruction.getValueA().getSignedValue() * currentInstruction.getValueB().getSignedValue(), true));
					break;
				case mulxss:
					long mulxssValue = currentInstruction.getValueA().getSignedValue() * currentInstruction.getValueB().getSignedValue();
					currentInstruction.setAluResult(new NiosValue32((int) (mulxssValue >> 32), true));
					break;
				case mulxsu:
					long mulxsuValue = currentInstruction.getValueA().getSignedValue() * currentInstruction.getValueB().getUnsignedValue();
					currentInstruction.setAluResult(new NiosValue32((int) (mulxsuValue >> 32), true));
					break;
				case mulxuu:
					long mulxuuValue = currentInstruction.getValueA().getSignedValue() * currentInstruction.getValueB().getUnsignedValue();
					currentInstruction.setAluResult(new NiosValue32((int) (mulxuuValue >> 32), true));
					break;
				case nor:
					currentInstruction.setAluResult(new NiosValue32(~(currentInstruction.getValueA().getSignedValue() | currentInstruction.getValueB().getSignedValue()), true));
					break;
				case or:
					currentInstruction.setAluResult(new NiosValue32(currentInstruction.getValueA().getSignedValue() | currentInstruction.getValueB().getSignedValue(), true));
					break;
				case rdctl:
					System.err.println("Execution of the instruction is not implemented yet !");
					break;
				case rol:
					System.err.println("Execution of the instruction is not implemented yet !");
					break;
				case roli:
					System.err.println("Execution of the instruction is not implemented yet !");
					break;
				case ror:
					System.err.println("Execution of the instruction is not implemented yet !");
					break;
				case sll:
					currentInstruction.setAluResult(new NiosValue32(currentInstruction.getValueA().getSignedValue() << (currentInstruction.getValueB().getSignedValue() & 0x1f), true));
					break;
				case slli:
					currentInstruction.setAluResult(new NiosValue32(currentInstruction.getValueA().getSignedValue() << (currentInstruction.getImm5() & 0x1f), true));
					break;
				case sra:
					currentInstruction.setAluResult(new NiosValue32(currentInstruction.getValueA().getSignedValue() >> (currentInstruction.getValueB().getSignedValue() & 0x1f), true));
					break;
				case srai:
					currentInstruction.setAluResult(new NiosValue32((int) (currentInstruction.getValueA().getSignedValue() >> ((currentInstruction.getImm5() & 0x1f))), true));
					break;
				case srl:
					System.err.println("Execution of the instruction is not implemented yet !");
					break;
				case srli:
					System.err.println("Execution of the instruction is not implemented yet !");
					break;
				case sub:
					currentInstruction.setAluResult(new NiosValue32(currentInstruction.getValueA().getSignedValue() - currentInstruction.getValueB().getSignedValue(), true));
					break;
				case sync:
					System.err.println("Execution of the instruction is not implemented yet !");
					break;
				case trap:
					System.err.println("Execution of the instruction is not implemented yet !");
					break;
				case wrctl:
					System.err.println("Execution of the instruction is not implemented yet !");
					break;
				case wrprs:
					System.err.println("Execution of the instruction is not implemented yet !");
					break;
				case xor:
					currentInstruction.setAluResult(new NiosValue32(currentInstruction.getValueA().getSignedValue() ^ currentInstruction.getValueB().getSignedValue(), true));
					break;
				default:
					break;
				}
				break;
			case addi:
				currentInstruction.setAluResult(new NiosValue32(currentInstruction.getValueA().getSignedValue() + currentInstruction.getValueB().getSignedValue(), true));
				break;
			case andhi:
				currentInstruction.setAluResult(new NiosValue32(currentInstruction.getValueA().getSignedValue() & (currentInstruction.getValueB().getSignedValue() << 16), true));
				break;
			case andi:
				currentInstruction.setAluResult(new NiosValue32(currentInstruction.getValueA().getSignedValue() + currentInstruction.getValueB().getSignedValue(), true));
				break;
			case beq:
				if (currentInstruction.getValueA().getSignedValue() == currentInstruction.getValueB().getSignedValue()){
					registers.setPC(new NiosValue32(currentInstruction.getPC() + currentInstruction.getValueToStore().getSignedValue() + 4, false));
					this.hasJumped = true;
				}
				break;
			case bge:
				if (currentInstruction.getValueA().getSignedValue() >= currentInstruction.getValueB().getSignedValue()){
					registers.setPC(new NiosValue32(currentInstruction.getPC() + currentInstruction.getValueToStore().getSignedValue() + 4, false));
					this.hasJumped = true;
				}
				break;
			case bgeu:
				if (currentInstruction.getValueA().getUnsignedValue() >= currentInstruction.getValueB().getUnsignedValue()){
					registers.setPC(new NiosValue32(currentInstruction.getPC() + currentInstruction.getValueToStore().getSignedValue() + 4, false));
					this.hasJumped = true;
				}
				break;
			case blt:
				if (currentInstruction.getValueA().getSignedValue() < currentInstruction.getValueB().getSignedValue()){
					registers.setPC(new NiosValue32(currentInstruction.getPC() + currentInstruction.getValueToStore().getSignedValue() + 4, false));
					this.hasJumped = true;
				}
				break;
			case bltu:
				if (currentInstruction.getValueA().getUnsignedValue() < currentInstruction.getValueB().getUnsignedValue()){
					registers.setPC(new NiosValue32(currentInstruction.getPC() + currentInstruction.getValueToStore().getSignedValue() + 4, false));
					this.hasJumped = true;
				}
				break;
			case bne:
				if (currentInstruction.getValueA().getSignedValue() != currentInstruction.getValueB().getSignedValue()){
					registers.setPC(new NiosValue32(currentInstruction.getPC() + currentInstruction.getValueToStore().getSignedValue() + 4, false));
					this.hasJumped = true;
				}
				break;
			case cmpeqi:
				if (currentInstruction.getValueA().getSignedValue() == currentInstruction.getValueB().getSignedValue())
					currentInstruction.setAluResult(new NiosValue32(1, true));
				else 
					currentInstruction.setAluResult(new NiosValue32(0, true));
				break;
			case cmpgei:
				if (currentInstruction.getValueA().getSignedValue() >= currentInstruction.getValueB().getSignedValue())
					currentInstruction.setAluResult(new NiosValue32(1, true));
				else 
					currentInstruction.setAluResult(new NiosValue32(0, true));
				break;
			case cmpgeui:
				if (currentInstruction.getValueA().getUnsignedValue() >= currentInstruction.getValueB().getUnsignedValue())
					currentInstruction.setAluResult(new NiosValue32(1, true));
				else 
					currentInstruction.setAluResult(new NiosValue32(0, true));
				break;
			case cmplti:
				if (currentInstruction.getValueA().getSignedValue() < currentInstruction.getValueB().getSignedValue())
					currentInstruction.setAluResult(new NiosValue32(1, true));
				else 
					currentInstruction.setAluResult(new NiosValue32(0, true));
				break;
			case cmpltui:
				if (currentInstruction.getValueA().getUnsignedValue() < currentInstruction.getValueB().getUnsignedValue())
					currentInstruction.setAluResult(new NiosValue32(1, true));
				else 
					currentInstruction.setAluResult(new NiosValue32(0, true));
				break;
			case cmpnei:
				if (currentInstruction.getValueA().getSignedValue() != currentInstruction.getValueB().getSignedValue())
					currentInstruction.setAluResult(new NiosValue32(1, true));
				else 
					currentInstruction.setAluResult(new NiosValue32(0, true));
				break;
			case custom:
				System.err.println("Execution of the instruction is not implemented yet !");
				break;
			case flushd:
				System.err.println("Execution of the instruction is not implemented yet !");
				break;
			case flushda:
				System.err.println("Execution of the instruction is not implemented yet !");
				break;
			case initd:
				System.err.println("Execution of the instruction is not implemented yet !");
				break;
			case initda:
				System.err.println("Execution of the instruction is not implemented yet !");
				break;
			case ldb:
			case ldbio:
			case ldbu:
			case ldbuio:
			case ldh:
			case ldhio:
			case ldhu:
			case ldhuio:
			case ldw:
			case ldwio:
			case stw:
			case sth:
			case sthio:
			case stbio:
			case stb:
			case stwio:
				currentInstruction.setAluResult(new NiosValue32(currentInstruction.getValueA().getSignedValue() + currentInstruction.getValueB().getSignedValue(), true));
				break;
			case muli:
				currentInstruction.setAluResult(new NiosValue32(currentInstruction.getValueA().getSignedValue() * currentInstruction.getValueB().getSignedValue(), true));
				break;
			case orhi:
				currentInstruction.setAluResult(new NiosValue32(currentInstruction.getValueA().getSignedValue() | (currentInstruction.getValueB().getSignedValue() << 16), true));
				break;
			case ori:
				currentInstruction.setAluResult(new NiosValue32(currentInstruction.getValueA().getSignedValue() | currentInstruction.getValueB().getSignedValue(), true));
				break;
			case rdprs:
				System.err.println("Execution of the instruction is not implemented yet !");
				break;
			case xorhi:
				currentInstruction.setAluResult(new NiosValue32(currentInstruction.getValueA().getSignedValue() ^ (currentInstruction.getValueB().getSignedValue() << 16), true));
				break;
			case xori:
				currentInstruction.setAluResult(new NiosValue32(currentInstruction.getValueA().getSignedValue() ^ currentInstruction.getValueB().getSignedValue(), true));
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
	
	public boolean hasJumped(){
		return this.hasJumped;
	}
}
