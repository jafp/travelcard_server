package dk.ihk.tcp.cardreader.usb;

import dk.ihk.tcp.cardreader.Device;
import dk.ihk.tcp.cardreader.DeviceException;
import dk.ihk.tcp.util.ShortBuffer;

public class TCPUSBDevice implements Device
{
	private final TCPUSB m_usb;
	private boolean m_isConnected;

	public TCPUSBDevice()
	{
		m_usb = new TCPUSB();
		m_usb.init();
		m_isConnected = false;
	}

	@Override
	public void connect(String serialNumber) throws DeviceException
	{
		m_usb.disconnect();

		int res = m_usb.connect(serialNumber);
		if (res < 0)
		{
			m_isConnected = false;
			throw new DeviceException("TCPUSBDevce: connect");
		}

		m_isConnected = true;
	}

	@Override
	public void disconnect()
	{
		m_usb.disconnect();
		m_isConnected = false;
	}

	@Override
	public boolean isConnected()
	{
		return m_isConnected;
	}

	@Override
	public int waitForInterrupt(ShortBuffer buffer) throws DeviceException
	{
		int res = m_usb.interrupt(buffer.getBuffer());

		if (res < 0)
		{
			throw new DeviceException("TCPUSBDevice: interrupt");
		}

		return res;
	}

	@Override
	public int send(int command, ShortBuffer buffer) throws DeviceException
	{
		return m_usb.send(command, buffer.getBuffer());
	}
}
