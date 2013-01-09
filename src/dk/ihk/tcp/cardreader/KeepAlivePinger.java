package dk.ihk.tcp.cardreader;

import dk.ihk.tcp.util.ShortBuffer;

/**
 * This class is used to keep the device alive, by sending a message at regular
 * intervals.
 * 
 * @author Jacob Pedersen
 */
public class KeepAlivePinger implements Runnable
{
	private Device m_device;
	private final Thread m_thread;
	private final ShortBuffer m_buffer;
	private boolean m_run;

	public KeepAlivePinger()
	{
		m_run = true;
		m_buffer = new ShortBuffer();
		m_thread = new Thread(this);
		m_thread.setName("KEEP ALIVE PINGER");
		m_thread.start();
	}

	public void setDevice(Device device)
	{
		m_device = device;
	}

	public void start()
	{
		m_run = true;
	}

	@Override
	public void run()
	{
		while (m_run)
		{
			try
			{
				ping();

				Thread.currentThread();
				Thread.sleep(800);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void ping() throws DeviceException
	{
		if (m_device != null)
		{
			m_device.send(Command.KEEP_ALIVE.ordinal(), m_buffer);
		}
	}

	public void stop()
	{
		m_run = false;
	}

}
