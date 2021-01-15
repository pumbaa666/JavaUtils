package ch.correvon.utils.helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class StringHelper
{

	/**
	 * Make a string to pad filename with same number of character
	 * @param character to fill string
	 * @param nbChar number of wanted characters
	 * @param currentNumber value of last characters
	 * @return
	 */
	public static String getPadding(String character, int nbChar, int currentNumber)
	{
		return getPadding(character, nbChar, ""+currentNumber);
	}

	/**
	 * Make a string to pad filename with same number of character
	 * @param character to fill string
	 * @param nbChar number of wanted characters
	 * @param currentNumber value of last characters
	 * @return
	 */
	public static String getPadding(String character, int nbChar, String currentNumber)
	{
		String output = currentNumber;

		for(int i = currentNumber.length(); i <= nbChar; i++)
			output = character + output;

		return output;
	}

	/**
	 * Find the extension of a filename
	 * @param fileName
	 * @return the extension (without dot). If no extension is found, return empty String
	 */
	public static String findExtension(String fileName)
	{
		return findExtension(fileName, false);
	}
	
	/**
	 * Find the extension of a filename
	 * @param fileName
	 * @param addDot If true, return dot + extension
	 * @return the extension. If no extension is found, return empty String
	 */
	public static String findExtension(String fileName, boolean addDot)
	{
		int lastIndex = lastIndexOf(fileName, ".");
		if(lastIndex == -1)
			return "";
		
		fileName = fileName.substring(lastIndex + 1);
		return (addDot?".":"") + fileName;
	}
	
	/**
	 * Return a file name without extension (search the last dot and delete every char after)
	 * @param fileName
	 * @return
	 */
	public static String removeExtension(String fileName)
	{
		int lastDot = lastIndexOf(fileName, ".");
		if(lastDot == -1)
			return fileName;
		
		return fileName.substring(0, lastDot);
	}
	
	public static int lastIndexOf(String string, String search)
	{
		int index = string.indexOf(search);
		if(index == -1)
			return -1;

		int lastIndex;
		do
		{
			lastIndex = index;
			index = string.indexOf(search, index + 1);
		}
		while(index != -1);

		return lastIndex;
	}
	
	/**
	 * Calculate the number of digit needed to pad a filename
	 * @param nbFiles
	 * @param increment
	 * @param startCount
	 * @return
	 */
	public static int getNbRequiredChar(int nbFiles, int increment, int startCount)
	{
		return (int)Math.log10((nbFiles - 1) * increment + startCount);
	}
	
	/**
	 * Generate a file name with the current date. yyyy-MM-dd_hh-mm-ss_[RANDOM_NUMBER*5].log
	 * @return
	 */
	public static String generateLogFileName()
	{
		return generateLogFileName("", "", ".log");
	}
	
	/**
	 * Generate a file name with the current date. [PREFIX]yyyy-MM-dd_hh-mm-ss_[RANDOM_NUMBER*5][SUFFIX].[EXTENSION]
	 * @return
	 */
	public static String generateLogFileName(String prefix, String suffix, String extension)
	{
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
		return prefix + formatter.format(new Date()) + "_" + generateRandomString(5, "0123456789".toCharArray()) + suffix + extension;
	}
	
	/**
	 * Generate a random String containing lowercase letters
	 * @param nbChar Number of chars in the String
	 * @return the random String
	 */
	public static String generateRandomString(int nbChar)
	{
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		return(generateRandomString(nbChar, chars));
	}
	
	/**
	 * Generate a random String
	 * @param nbChar Number of chars in the String
	 * @param authorizedChar array of char the String can contain
	 * @return the random String
	 */
	public static String generateRandomString(int nbChar, char[] authorizedChar)
	{
		String result = "";
		int rnd;
		int nbAuthorizedChar = authorizedChar.length-1;
		for(int i = 0; i < nbChar; i++)
		{
			rnd = (int)(Math.random() * nbAuthorizedChar);
			result += ""+authorizedChar[rnd];
		}
		return result;
	}
	
	/**
	 * Convert a Date to a String with default date format "dd.MM.yyyy"
	 * @param date
	 * @return the date
	 */
	public static String convertDateToString(Date date)
	{
		return convertDateToString(date, "dd.MM.yyyy");
	}
	
	/**
	 * Convert a Date to a String.
	 * @param date
	 * @param dateFormat
	 * @return the date
	 */
	public static String convertDateToString(Date date, String dateFormat)
	{
		return new SimpleDateFormat(dateFormat).format(date);
	}

	/**
	 * 
	 * @param str : the string to test
	 * @return true if the string is null or empty (equals to "")
	 */
	public static boolean isStringEmpty(String str)
	{
		return str == null || str.equals("");
	}
	
	/**
	 * Build a String from a list
	 * @param list
	 * @param append : append to each elements of list
	 * @return
	 */
	public static String convertListToString(ArrayList<String> list, String append)
	{
		if(list == null || list.isEmpty())
			return "";
		if(append == null)
			append = "";
		StringBuilder infos = new StringBuilder(10000);
		for(String info:list)
			infos.append(info+append);
		return infos.toString();
	}
	

}
