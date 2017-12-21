package niosSimulator;

import java.util.ArrayList;

public class Instruction {

	private long pc;
	private niosOpCode op;
	private niosType type; 
	private niosOpxCode opx;
	
	private int imm5;
	private int imm16;
	private int imm26;
	private int ra;
	private int rb;
	private int rc;
	
	private NiosValue32 valueA;
	private NiosValue32 valueB;
	private NiosValue32 aluResult;
	private NiosValue valueToStore;
	private NiosValue32 valueLoaded;
	
	public boolean isDecodeDone;
	public boolean isExecuteDone;
	public boolean isMemoryDone; 
	public boolean isWritebackDone;
	
	private long cycleNumber;
	

	
	public Instruction(long binaryInstruction, long pc, long startCycle){
		this.pc = pc;
		int opBinary = (int) (binaryInstruction & 0x3f);
		this.setCycleNumber(startCycle);

		for (niosOpCode opcode : niosOpCode.values()){
			if (opcode.getOpCode() == opBinary){
				this.op = opcode;
				break;
			}
			
			this.op = niosOpCode.unknown;
		}

		
		this.isDecodeDone = false;
		this.isExecuteDone = false; 
		this.isMemoryDone = false;
		this.isWritebackDone = false;
		
		switch(op){
			case RTYPE:
				this.type = niosType.RTYPE;
				int opxBinary = (int) ((binaryInstruction >> 11) & 0x3f);
				
				for (niosOpxCode opxcode : niosOpxCode.values())
					if (opxcode.getOpxCode() == opxBinary){
						this.opx = opxcode;
						break;
					}
				
				this.imm5 = (int) ((binaryInstruction >> 6) & 0x1f);
				this.rc = (int) ((binaryInstruction >> 17) & 0x1f);
				this.rb = (int) ((binaryInstruction >> 22) & 0x1f);
				this.ra = (int) ((binaryInstruction >> 27) & 0x1f);
				break;
			case call:
			case jmpi:
				this.type = niosType.JTYPE;
				this.imm26 = (int) ((binaryInstruction >> 6) & 0x3ffffff);
				break;
			case unknown:
				this.type = niosType.UNKNOWN;
				break;
			default:
				this.type = niosType.ITYPE;
				this.imm16 = (int) ((binaryInstruction >> 6) & 0xffff);
				this.rb = (int) ((binaryInstruction >> 22) & 0x1f);
				this.ra = (int) ((binaryInstruction >> 27) & 0x1f);
				break;
		}
		
	}
	
	public int getWrittenRegister(){	
		switch(this.getType()){
		case RTYPE:
			switch (this.getOpx()){
			case roli:
			case rol:
			case nor:
			case mulxuu:
			case cmpge:
			case ror:
			case and:
			case cmplt:
			case slli:
			case sll:
			case or:
			case cmpne:
			case srli:
			case srl:
			case xor:
			case mulxss:
			case cmpeq:
			case divu:
			case div:
			case mul:
			case cmpgeu:
			case cmpltu:
			case add:
			case sub:
			case srai:
			case sra:
				//We write the alu result
				return this.rc;
			case callr:
				return 31;
			default:
				//In defalut case we write nothing
				return -1;
			}
		case JTYPE:
			switch (this.getOp()) {
			case call:
				return 31;
			default:
				return -1;
			}
				//None of JTYPE instruction use writeback
		case ITYPE:
			switch (this.getOp()){
			case ldbu:
			case addi:
			case ldb:
			case cmpgei:
			case ldhu:
			case andi:
			case ldh:
			case cmplti:
			case ori:
			case ldw:
			case cmpnei:
			case xori:
			case cmpeqi:
			case ldbuio:
			case muli:
			case ldbio:
			case cmpgeui:
			case ldhuio:
			case andhi:
			case ldhio:
			case cmpltui:
			case orhi:
			case ldwio:
			case xorhi:				
				//We write the alu result
				return this.rb;
			default:
				break;
			}
		case UNKNOWN:
			break;
		default:
			break;
		}
		return -1;
	}
	
	public ArrayList<Integer> getUsedRegisters(){
		ArrayList<Integer> result = new ArrayList<Integer>();
		switch (this.type){
		case RTYPE:
			switch (this.opx){
			case rol:
			case nor:
			case mulxuu:
			case cmpge:
			case ror:
			case and:
			case cmplt:
			case sll:
			case or:
			case cmpne:
			case srl:
			case xor:
			case mulxss:
			case cmpeq:
			case divu:
			case div:
			case mul:
			case cmpgeu:
			case cmpltu:
			case add:
			case sub:
			case sra:
				result.add(this.ra);
				result.add(this.rb);
				break;
			case roli:
			case slli:
			case srli:
			case jmp:
			case callr:
			case srai:
				result.add(this.ra);
				break;
			default:
				break;
			}
			break;
		case JTYPE:
			break;
		case ITYPE:
			switch (this.op){
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
				result.add(this.ra);
				break;
			case stb:
			case sth:
			case stw:
			case stbio:
			case sthio:
			case stwio:
			case br:
			case bge:
			case blt:
			case bne:
			case beq:
			case bgeu:
				result.add(this.ra);
				result.add(this.rb);
				break;
			case addi:
			case cmpgei:
			case andi:
			case cmplti:
			case ori:
			case cmpnei:
			case xori:
			case cmpeqi:
			case muli:
			case cmpgeui:
			case andhi:
			case cmpltui:
			case orhi:
			case xorhi:
				result.add(this.ra);
			default:
				break;
			}
			break;
		case UNKNOWN:
			break;
		default:
			break;
		}
		
		
		return result;		
	}
	
	//******************************************************************************************
	//*              Getters and setters
	//******************************************************************************************

	public NiosValue32 getValueA() {
		return valueA;
	}


	public void setValueA(NiosValue32 valueA) {
		this.valueA = valueA;
	}


	public NiosValue32 getValueB() {
		return valueB;
	}


	public void setValueB(NiosValue32 valueB) {
		this.valueB = valueB;
	}


	public NiosValue32 getAluResult() {
		return aluResult;
	}


	public void setAluResult(NiosValue32 aluResult) {
		this.aluResult = aluResult;
	}


	public NiosValue getValueToStore() {
		return valueToStore;
	}


	public void setValueToStore(NiosValue valueToStore) {
		this.valueToStore = valueToStore;
	}


	public NiosValue32 getValueLoaded() {
		return valueLoaded;
	}


	public void setValueLoaded(NiosValue32 valueLoaded) {
		this.valueLoaded = valueLoaded;
	}


	public niosOpCode getOp() {
		return op;
	}


	public niosType getType() {
		return type;
	}


	public niosOpxCode getOpx() {
		return opx;
	}


	public int getImm5() {
		return imm5;
	}


	public int getImm16() {
		return imm16;
	}


	public int getImm26() {
		return imm26;
	}


	public int getRa() {
		return ra;
	}


	public int getRb() {
		return rb;
	}


	public int getRc() {
		return rc;
	}
	
	public long getPC(){
		return this.pc;
	}

	public String toString(){
		switch (this.type) {
		case RTYPE:
			switch (this.opx){
			case roli:
			case slli:
			case srli:
			case srai:
				return this.opx.name() + " r" + this.rc + " r" + this.ra + " " + this.imm5;
			default:
				return this.opx.name() + " r" + this.rc + " r" + this.ra + " r" + this.rb;
			}
		case ITYPE:
			switch (this.op){
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
			case stb:
			case sth:
			case stw:
			case stbio:
			case sthio:
			case stwio:
				return this.op.name() + " r" + this.ra + " " +  (new NiosValue16(this.imm16)).getSignedValue() + " (r" + this.rb + ")";
			case br:
			case bge:
			case blt:
			case bne:
			case beq:
			case bgeu:
				return this.op.name() + " r" + this.ra + " r" + this.rb + " " + (new NiosValue16(this.imm16)).getSignedValue();
			default:
				return this.op.name() + " r" + this.rb + " r" + this.ra + " " + (new NiosValue16(this.imm16)).getSignedValue();
			}
		case JTYPE:
			return this.op.name() +  this.imm26;
		case UNKNOWN:
			return "?";
		default:
			break;

		}
		return this.op.name() + " r" + this.ra + " r" + this.rb + " r" + this.rc;
	}

	public long getCycleNumber() {
		return cycleNumber;
	}

	public void setCycleNumber(long cycleNumber) {
		this.cycleNumber = cycleNumber;
	}
}
	
