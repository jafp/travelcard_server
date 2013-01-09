package dk.ihk.tcp.cardreader;

import dk.ihk.tcp.cardreader.Response.Type;
import dk.ihk.tcp.util.ShortBuffer;
import dk.ihk.tcp.util.inject.Inject;

public class BaseCardReader implements CardReader
{
	private String m_serial;
	private final Device m_device;
	private final KeepAlivePinger m_pinger;
	private Reading m_activeReading;

	@Inject
	public BaseCardReader(Device device)
	{
		m_device = device;
		m_serial = null;
		m_pinger = new KeepAlivePinger();
	}

	@Override
	public void connect() throws DeviceException
	{
		m_device.connect(m_serial);
		m_pinger.setDevice(m_device);
		m_pinger.start();
		new Thread(m_pinger).start();
	}

	@Override
	public boolean isConnected()
	{
		return m_device.isConnected();
	}

	@Override
	public Reading readCard() throws DeviceException
	{
		if (!m_device.isConnected())
		{
			return null;
		}

		// Since we are trying to read a new card, lets assume we don't care
		// about the last reading
		if (m_activeReading != null)
		{
			m_activeReading.respondWith(Response.create(Type.ERROR));
			m_activeReading = null;
		}

		ShortBuffer buffer = new ShortBuffer();
		int n = m_device.waitForInterrupt(buffer);
		if (n == 8)
		{
			long id = buffer.getLong();
			return new Reading(id, this);
		}

		return null;
	}

	@Override
	public boolean sendResponse(Response resp)
	{
		if (m_device.isConnected())
		{
			try
			{
				m_device.send(Command.RESPONSE.ordinal(), resp.getBuffer());
				return true;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public void disconnect()
	{
		m_pinger.stop();
		m_device.disconnect();
	}

	@Override
	public void setSerialNumber(String id)
	{
		m_serial = id;
	}
}
