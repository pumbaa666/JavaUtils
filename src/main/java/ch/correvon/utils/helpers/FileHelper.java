package ch.correvon.utils.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileHelper
{
	public static String readFile(String filePath, String append)
	{
		if(append == null)
			append = "";
		return StringHelper.convertListToString(readFile(filePath), append);
	}
	
	public static ArrayList<String> readFile(String filePath)
	{
		ArrayList<String> result = new ArrayList<String>(500);
	    BufferedReader bufferReader = null;
	    String line;

	    try
	    {
	    	bufferReader = new BufferedReader(new FileReader(filePath)); 
		    while((line = bufferReader.readLine()) != null)
		    	result.add(line);
		    bufferReader.close();
		}
	    catch(FileNotFoundException exc)
	    {
	    }
	    catch(IOException e)
	    {
	    }

		return result;
	}

	
	public static boolean writeFile(String fileName, String text)
	{
		if(text == null || text.isEmpty())
			return true;
		File logFile = new File(fileName);
//		if(!logFile.isFile())
//			return false;
		
		logFile.getParentFile().mkdir();
		try
		{
			FileWriter fileWriter = new FileWriter(logFile);
			fileWriter.write(text);
			fileWriter.close();
		}
		catch (IOException e1)
		{
			System.err.println("Impossible d'�crire dans le fichier "+logFile.getAbsolutePath());
			return false;
		}

		return true;
	}
}
