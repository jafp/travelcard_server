package dk.ihk.tcp.cardreader.fake;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import dk.ihk.tcp.cardreader.Device;
import dk.ihk.tcp.cardreader.DeviceException;
import dk.ihk.tcp.cardreader.Response;
import dk.ihk.tcp.util.ShortBuffer;

/**
 * This is a simulation device where a scanned can be simulated by calling the
 * scanCard(long) method.
 * 
 * @author Jacob Pedersen
 */
public class FakeDevice implements Device
{
	/**
	 * Queue of scanned cards waiting to be read
	 */
	private final BlockingQueue<Long> m_queue;

	/**
	 * Indicator of the connection state
	 */
	private boolean m_isConnected;

	/**
	 * The latest sent message
	 */
	private Message m_latestSent;

	/**
	 * Construct a new fake device with an empty queue
	 */
	public FakeDevice()
	{
		m_queue = new LinkedBlockingQueue<Long>();
	}

	/**
	 * Simulate a card scan
	 * 
	 * @param id The card ID
	 */
	public void scanCard(long id)
	{
		m_queue.add(id);
	}

	/**
	 * @return The latest sent message
	 */
	public Message getLatestSent()
	{
		return m_latestSent;
	}

	/**
	 * @see dk.ihk.tcp.cardreader.Device#connect(java.lang.String)
	 */
	@Override
	public void connect(String serialNumber)
	{
		m_isConnected = true;
	}

	/**
	 * @see dk.ihk.tcp.cardreader.Device#disconnect()
	 */
	@Override
	public void disconnect()
	{
		m_isConnected = false;
	}

	/**
	 * @see dk.ihk.tcp.cardreader.Device#isConnected()
	 */
	@Override
	public boolean isConnected()
	{
		return m_isConnected;
	}

	/**
	 * @see dk.ihk.tcp.cardreader.Device#waitForInterrupt(dk.ihk.tcp.util.ShortBuffer)
	 */
	@Override
	public int waitForInterrupt(ShortBuffer buffer) throws DeviceException
	{
		try
		{
			while (m_queue.isEmpty())
			{
				if (!isConnected())
				{
					throw new DeviceException("FAKE DEVICE: disconnected");
				}
			}

			Long id = m_queue.poll();
			buffer.setLong(id);

			System.out.println("FAKE DEVICE: card " + id);
			Thread.sleep(10);
			return 8;

		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * @see dk.ihk.tcp.cardreader.Device#send(int, dk.ihk.tcp.util.ShortBuffer)
	 */
	@Override
	public int send(int command, ShortBuffer buffer) throws DeviceException
	{
		System.out.println("FAKE DEVICE: host-to-device (" + command + ")");

		int response = buffer.getInt8(0);
		if (response == Response.Type.CHECKED_IN.ordinal())
		{
			System.out.println("FAKE DEVICE: [check in]");
		}
		else if (response == Response.Type.CHECKED_OUT.ordinal())
		{
			System.out.println("FAKE DEVICE: [check out] price: " + buffer.getInt16(3) + ", balance: " + buffer.getInt16(1));
		}
		else
		{
			System.out.println("FAKE DEVICE: unknown response (" + response + ")");
		}

		m_latestSent = new Message(command, buffer);

		return 0;
	}

	public class Message
	{
		public int command;
		public ShortBuffer buffer;

		public Message(int c, ShortBuffer b)
		{
			command = c;
			buffer = b;
		}
	}
}
