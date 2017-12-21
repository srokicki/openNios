package niosSimulator;

public class NiosValue16 extends NiosValue{
	
	private long value;
	
	public NiosValue16(long value){
		this.value = value;
	}

	@Override
	public long getSignedValue() {
		if (value>>15 != 0)
			return (long) (value - Math.pow(2, 16));
		else
			return value;
	}

	@Override
	public long getUnsignedValue() {
		return value;
	}
	
	public NiosValue16 copy(){
		return new NiosValue16(getSignedValue());
	}

}
