package niosSimulator;

public abstract class NiosValue {
	
	public abstract long getSignedValue();
	public abstract long getUnsignedValue();
	
	public abstract NiosValue copy();

}
