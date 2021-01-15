package ch.correvon.utils.helpers;

import javax.swing.JSpinner;
import javax.swing.JTextField;

public class ComponentHelper
{
	/**
	 * Return the first char of a JTextField
	 * @param textField
	 * @return the first char of a JTextField
	 */
	public static String charValueOf(JTextField textField)
	{
		String text = textField.getText();
		if(text.equals(""))
			return "";
		return "" + text.charAt(0);
	}

	/**
	 * Return the int value of a JSpinner
	 * @param spin The JSpinner
	 * @return the int value of a JSpinner
	 */
	public static int intValueOf(JSpinner spin)
	{
		return ((Integer)(spin.getValue())).intValue();
	}
	
	/**
	 * Return the JFormatedTextField contained in the JSpinner
	 * @param spin
	 * @return the JFormatedTextField contained in the JSpinner
	 */
	public static JTextField extractTextField(JSpinner spin)
	{
		return ((JSpinner.DefaultEditor)spin.getEditor()).getTextField();
	}
}
