package dk.ihk.tcp.test.cardreader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import dk.ihk.tcp.cardreader.CardReader;
import dk.ihk.tcp.cardreader.Command;
import dk.ihk.tcp.cardreader.Device;
import dk.ihk.tcp.cardreader.DeviceException;
import dk.ihk.tcp.cardreader.Reading;
import dk.ihk.tcp.cardreader.Response;
import dk.ihk.tcp.cardreader.Response.Type;
import dk.ihk.tcp.cardreader.fake.FakeDevice;
import dk.ihk.tcp.cardreader.fake.FakeDevice.Message;
import dk.ihk.tcp.test.TestBindings;
import dk.ihk.tcp.util.inject.Injector;

public class CardReaderTest
{
	private Injector m_injector;
	private FakeDevice m_device;
	private CardReader m_reader;

	@Before
	public void before() throws Exception
	{
		m_injector = new Injector(new TestBindings());
		m_device = (FakeDevice) m_injector.get(Device.class);
		m_reader = m_injector.get(CardReader.class);
	}

	@Test
	public void testConnectAndDisconnect() throws DeviceException
	{
		m_reader.disconnect();
		assertFalse(m_device.isConnected());
		m_reader.connect();
		assertTrue(m_device.isConnected());
		m_reader.disconnect();
		assertFalse(m_device.isConnected());
	}

	@Test
	public void testReadCard() throws DeviceException
	{
		m_reader.connect();
		m_device.scanCard(987);

		Reading reading = m_reader.readCard();
		assertNotNull(reading);
		assertEquals(987, reading.getCardId());

		reading.respondWith(Response.create(Type.OK));

		// Get the latest message sent by the device ( host to device )
		Message msg = m_device.getLatestSent();

		assertNotNull(msg);
		assertEquals(Command.RESPONSE.ordinal(), msg.command);

		int responseCode = msg.buffer.getInt8(0);
		assertEquals(Response.Type.OK.ordinal(), responseCode);
	}

	@Test
	public void testReadCardResponseWithData() throws DeviceException
	{
		m_reader.connect();
		m_device.scanCard(987);

		Reading reading = m_reader.readCard();
		Response response = Response.create(Type.CHECKED_OUT).setShort(1, 1234);
		reading.respondWith(response);

		assertEquals(Response.Type.CHECKED_OUT.ordinal(), response.getBuffer().getInt8(0));
		assertEquals(1234, response.getBuffer().getInt16(1));
	}
}
