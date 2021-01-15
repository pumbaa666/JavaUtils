package ch.correvon.utils.extendedObjects;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

public class BlinkLabel extends JLabel
{
	public BlinkLabel()
	{
		this.timer = new Timer(DEFAULT_BLINKING_RATE, new TimerListener(this));
		this.timer.setInitialDelay(0);
		super.setVisible(false);
	}
	
	/**
	 * Set the blinking delay.
	 * @param delay in milli second
	 */
	public void setBlinkDelay(int delay)
	{
		this.timer.setDelay(delay);
	}
	
	/**
	 * 
	 * @return the blinking delay in milli second
	 */
	public int getBlinkDelay()
	{
		return this.timer.getDelay();
	}
	
	/**
	 * Show the label and start blinking
	 */
	public void startBlinking()
	{
		if(!this.timer.isRunning())
		{
			this.setVisible(true);
			this.timer.restart();
		}
	}

	/**
	 * Hide the label and stop blinking
	 */
	public void stopBlinking()
	{
		if(this.timer.isRunning())
		{
			this.setVisible(false);
			this.timer.stop();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override public void setText(String text)
	{
		super.setText(text);
	}

	private class TimerListener implements ActionListener
	{
		public TimerListener(BlinkLabel bl)
		{
			this.blinkLabel = bl;
			this.foregroundColor = bl.getForeground();
			this.backgroundColor = bl.getBackground();
		}
	 
	    public void actionPerformed(ActionEvent e)
	    {
			if(this.isForeground)
				this.blinkLabel.setForeground(this.foregroundColor);
			else
				this.blinkLabel.setForeground(this.backgroundColor);
			this.isForeground = !this.isForeground;
		}
	    
		private BlinkLabel blinkLabel;
		private Color backgroundColor;
		private Color foregroundColor;
		private boolean isForeground = true;
	}
	
	private static final long serialVersionUID = 97455851326L;
	private static final int DEFAULT_BLINKING_RATE = 1000; // in ms
	private Timer timer;
}