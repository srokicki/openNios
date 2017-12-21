package niosSimulator;

import java.util.ArrayList;
import java.util.HashMap;

public class NiosMemory {

	private HashMap<Integer, NiosValue8> memory;
	
	public NiosMemory(){
		this.memory = new HashMap<Integer, NiosValue8>();
	}
	
	private NiosValue8 loadByteAsByte(int addr){
		if (this.memory.containsKey(addr))
			return this.memory.get(addr).copy();
		else
			return new NiosValue8(0);
	}
	
	public NiosValue8 loadByteUnsigned(int addr){
		return this.loadByteAsByte(addr);

	}
	
	public NiosValue8 loadByteSigned(int addr){
		//TODO
		return null;
	}
	
	public NiosValue32 loadWord(int addr){
		NiosValue8 v1 = this.loadByteAsByte(addr);
		NiosValue8 v2 = this.loadByteAsByte(addr+1);
		NiosValue8 v3 = this.loadByteAsByte(addr+2);
		NiosValue8 v4 = this.loadByteAsByte(addr+3);
		
		return new NiosValue32(v1.getUnsignedValue() + (v2.getUnsignedValue() << 8) + (v3.getUnsignedValue()<<16) + (v4.getUnsignedValue()<<24), false);
	}
	
	public NiosValue32 loadWord(long addr){
		NiosValue8 v1 = this.loadByteAsByte((int) addr);
		NiosValue8 v2 = this.loadByteAsByte((int) (addr+1));
		NiosValue8 v3 = this.loadByteAsByte((int) (addr+2));
		NiosValue8 v4 = this.loadByteAsByte((int) (addr+3));
		
		return new NiosValue32(v1.getUnsignedValue() + (v2.getUnsignedValue() << 8) + (v3.getUnsignedValue()<<16) + (v4.getUnsignedValue()<<24), false);
	}
	
	public NiosValue32 loadHalf(long addr){
		NiosValue8 v1 = this.loadByteAsByte((int) addr);
		NiosValue8 v2 = this.loadByteAsByte((int) (addr+1));

		
		return new NiosValue32(v1.getUnsignedValue() + (v2.getUnsignedValue() << 8), false);
	}
	
	public void set(int addr, NiosValue8 value){
		this.memory.put(addr, value);
	}
	public void setWord(int addr, NiosValue32 word){
		this.set(addr, new NiosValue8(word.getUnsignedValue() & 0xff));
		this.set(addr+1, new NiosValue8((word.getUnsignedValue()>>8) & 0xff));
		this.set(addr+2, new NiosValue8((word.getUnsignedValue()>>16) & 0xff));
		this.set(addr+3, new NiosValue8((word.getUnsignedValue()>>24) & 0xff));

	}
	
	public void setWord(long addr, NiosValue word){
		this.set((int) addr, new NiosValue8(word.getUnsignedValue() & 0xff));
		this.set((int) addr+1, new NiosValue8((word.getUnsignedValue()>>8) & 0xff));
		this.set((int) addr+2, new NiosValue8((word.getUnsignedValue()>>16) & 0xff));
		this.set((int) addr+3, new NiosValue8((word.getUnsignedValue()>>24) & 0xff));
		
		System.out.println("Writing at 0x" + Long.toHexString(addr) + " : 0x" + Long.toHexString(word.getUnsignedValue()));
	}
	
	
	public void setHalf(long addr, NiosValue word){
		this.set((int) addr, new NiosValue8(word.getUnsignedValue() & 0xff));
		this.set((int) addr+1, new NiosValue8((word.getUnsignedValue()>>8) & 0xff));
	}
}
