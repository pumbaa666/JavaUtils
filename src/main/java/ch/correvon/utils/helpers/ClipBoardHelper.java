package ch.correvon.utils.helpers;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;

/**
 * from http://java.developpez.com/faq/java/?page=systeme#SYSTEME_pressePapier 
 */
public class ClipBoardHelper
{
	/**
	 * Try to find a String in the clipboard
	 * @return empty String if nothing found. Else return the String
	 */
	public static String readClipboard()
	{
		String result = "";
		
		Transferable toolkit = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		try
		{
			// Vérification que le contenu est de type texte
			if(toolkit != null && toolkit.isDataFlavorSupported(DataFlavor.stringFlavor))
			{
				String txt = (String)toolkit.getTransferData(DataFlavor.stringFlavor);
				if(new File(txt).isDirectory())
					result = txt;
			}
		}
		catch(UnsupportedFlavorException e1){}
		catch(IOException e2){}

		return result;
	}

	/**
	 * Write a String in the clipboard
	 * @param text
	 * @return return false if IllegalStateException occurs. Else return true
	 */
	public static boolean writeClipboard(String text)
	{
		return writeClipboard(text, true);
	}
	
	/**
	 * Write a String in the clipboard.
	 * @param text
	 * @param permission If false, don't write anything.
	 * @return return false if IllegalStateException occurs. Else return true
	 */
	public static boolean writeClipboard(String text, boolean permission)
	{
		if(permission)
		{
			try
			{
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
			}
			catch(IllegalStateException e1)
			{
				System.err.println("Le presse papier n'est pas disponible");
				e1.printStackTrace();
				return false;
			}
		}
		return true;
	}

}
