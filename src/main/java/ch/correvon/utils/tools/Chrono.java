package ch.correvon.utils.tools;

public class Chrono
{
	public Chrono()
	{
		this("");
	}
	
	public Chrono(String name)
	{
		this.name = name;
		this.start = 0;
		this.stop = 0;
		this.isStarted = false;
	}

	public String getName()
	{
		return this.name;
	}
	
	public String start()
	{
		if(this.isStarted)
			return "Le chrono '"+this.name+"' était déjà démarré";
		
		isStarted = true;
		start = System.currentTimeMillis();
		return "";
	}
	
	public String stop()
	{
		if(!this.isStarted)
			return "Le chrono '"+this.name+"' n'était pas démarré";
		
		this.stop = System.currentTimeMillis();
		this.isStarted = false;
		return "";
	}
	
	public String stopAndPrint(Precision precision)
	{
		this.stop();
		return this.printDelay(precision);
	}
	
	public double getDelay(Precision precision)
	{
//		if(this.isStarted)
//		{
//			String msg = "Le chrono '"+this.name+"' est en cours d'exécution, veuillez l'arrêter avant de demander le temps écoulé";
//			throw new RuntimeException(msg);
//		}
		
		double delay = this.stop - this.start;
		
		PrecisionEnum precisionValue = precision.getValue();
		if(precisionValue == PrecisionEnum.MILLI)
			return delay;
		
		delay /= 1000.0;
		if(precisionValue == PrecisionEnum.SECOND)
			return delay;
		
		delay /= 60.0;
		if(precisionValue == PrecisionEnum.MINUTE)
			return delay;
		
		delay /= 60.0;
		if(precisionValue == PrecisionEnum.HOUR)
			return delay;
		
		delay /= 24.0;
		return delay;
	}
	
	public void printDelay()
	{
		this.printDelay(MILLI);
	}
	
	public String printDelay(Precision precision)
	{
		return this.getDelayStr(precision);
	}
	
	public String getDelayStr()
	{
		return this.getDelayStr(MILLI);
	}
	
	public String getDelayStr(Precision precision)
	{
		return this.name + " " + this.getDelay(precision) + " " + precision.getUnit();
	}
	
	// Attributs
	private String name;
	private double start;
	private double stop;
	private boolean isStarted;
	
	// Static
	private static Chrono currentChrono;
	
	public static void startCurrent()
	{
		startNewCurrent("");
	}
	
	public static String startNewCurrent(String name)
	{
		currentChrono = new Chrono(name);
		return currentChrono.start();
	}
	
	public static String stopCurrent()
	{
		return currentChrono.stop();
	}
	
	public static String stopAndPrintCurrent()
	{
		return stopAndPrintCurrent(MILLI);
	}
	
	public static String stopAndPrintCurrent(Precision precision)
	{
		return currentChrono.stopAndPrint(precision);
	}

	public static double getDelayCurrent()
	{
		return getDelayCurrent(MILLI);
	}
	
	public static double getDelayCurrent(Precision precision)
	{
		return currentChrono.getDelay(precision);
	}
	
	public static void printDelayCurrent()
	{
		printDelayCurrent(MILLI);
	}
	
	public static void printDelayCurrent(Precision precision)
	{
		currentChrono.printDelay(precision);
	}

	public static String getDelayStrCurrent()
	{
		return getDelayStrCurrent(MILLI);
	}
	
	public static String getDelayStrCurrent(Precision precision)
	{
		return currentChrono.getDelayStr(precision);
	}

	// Precision
	private static class Precision
	{
		public Precision(String unit, PrecisionEnum value)
		{
			this.unit = unit;
			this.value = value;
		}
		
		public String getUnit()
		{
			return this.unit;
		}
		
		public PrecisionEnum getValue()
		{
			return this.value;
		}
		
		private String unit;
		private PrecisionEnum value;
	}

	public static final Precision MILLI = new Precision("ms", PrecisionEnum.MILLI);
	public static final Precision SECOND = new Precision("s", PrecisionEnum.SECOND);
	public static final Precision MINUTE = new Precision("m", PrecisionEnum.MINUTE);
	public static final Precision HOUR = new Precision("h", PrecisionEnum.HOUR);
	
	private enum PrecisionEnum
	{
		MILLI,
		SECOND,
		MINUTE,
		HOUR;
	}
}
