package cp.ex1;

public interface ReadWrite {
	
	public void acquireRead();
	public void releaseRead();
	public void acquireWrite();
	public void releaseWrite();

}
