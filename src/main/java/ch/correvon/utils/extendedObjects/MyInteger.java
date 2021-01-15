package ch.correvon.utils.extendedObjects;

/**
 * Contain a primitif int and some basic mathematic options 
 * @author lco
 *
 */
public class MyInteger
{
	public MyInteger()
	{
		this(0);
	}
	
	public MyInteger(int value)
	{
		this.value = value;
	}
	
	public int increment()
	{
		return ++this.value;
	}
	
	public int decrement()
	{
		return --this.value;
	}
	
	public int multiply(int multiplicator)
	{
		return this.value *= multiplicator;
	}
	
	public int divide(int divisor)
	{
		return this.value /= divisor;
	}
	
	public void setValue(int value)
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return this.value;
	}
	
	private int value;
}
