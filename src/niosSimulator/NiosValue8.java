package niosSimulator;

public class NiosValue8 extends NiosValue{

	private long value;
	
	public NiosValue8(long value){
		this.value = value;
	}
	
	public NiosValue8(int value){
		this.value = value;
	}
	
	public long getSignedValue(){
		if (value>>7 != 0)
			return (int) (value - Math.pow(2, 8));
		else
			return (int) value;
	}
	
	public long getUnsignedValue(){
		return this.value;
	}

	@Override
	public NiosValue8 copy() {
		return new NiosValue8(this.value);
	}
}
