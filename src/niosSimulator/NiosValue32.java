package niosSimulator;

public class NiosValue32 extends NiosValue {
	
	private long value;
	
	public NiosValue32(int value, boolean isSigned){
		this.value = value;
	}
	
	public NiosValue32(long value, boolean isSigned){
		this.value = value;
	}

	public long getSignedValue(){
		if (value>>31 != 0)
			return (long) (value - Math.pow(2, 32));
		else
			return value;
	}
	
	public long getUnsignedValue(){
		return value & 0xffffffff;
	}
	
	public NiosValue32 copy(){
		return new NiosValue32(getUnsignedValue(), false);
	}
}
