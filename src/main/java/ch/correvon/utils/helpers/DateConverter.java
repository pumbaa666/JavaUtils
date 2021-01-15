package ch.correvon.utils.helpers;

public class DateConverter
{
	public DateConverter(long millis)
	{
		this.millis = millis;
		this.convertFromMillisToTime();
	}
	
	public DateConverter(double millis) 
	{
		this((long)millis);
	}
	
	public DateConverter(int millis)
	{
		this((long)millis);
	}
	
	private void convertFromMillisToTime()
	{
		this.second = this.millis / 1000L;
		this.millis -= this.second * 1000L;

		this.minutes = this.second / 60L;
		this.second -= this.minutes * 60L;

		this.hours = this.minutes / 60L;
		this.minutes -= this.hours * 60L;
	}
	
	public String getConvertedDate()
	{
		return this.getConvertedDate("%02dh %02dm %02ds %02dms");
	}
	
	public String getConvertedDate(String format)
	{
		return String.format(format, this.hours, this.minutes, this.second, this.millis);
	}
	
	public long getMillis()
	{
		return this.millis;
	}
	
	public long getSecondes()
	{
		return this.second;
	}
	
	public long getMinutes()
	{
		return this.minutes;
	}
	
	public long getHoures()
	{
		return this.hours;
	}
	
	private long millis;
	private long second;
	private long minutes;
	private long hours;
}
