package dk.ihk.tcp.test.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import dk.ihk.tcp.cardreader.CardReader;
import dk.ihk.tcp.cardreader.Device;
import dk.ihk.tcp.cardreader.DeviceException;
import dk.ihk.tcp.cardreader.Reading;
import dk.ihk.tcp.cardreader.Response.Type;
import dk.ihk.tcp.cardreader.fake.FakeDevice;
import dk.ihk.tcp.datamodel.Customer;
import dk.ihk.tcp.db.CustomerDAO;
import dk.ihk.tcp.db.DBException;
import dk.ihk.tcp.manager.JourneyManager;
import dk.ihk.tcp.manager.TerminalManager;
import dk.ihk.tcp.test.TestBindings;
import dk.ihk.tcp.test.TestJourneyManager;
import dk.ihk.tcp.util.inject.Injector;
import dk.ihk.tcp.zones.MemoryZoneManager;
import dk.ihk.tcp.zones.ZoneManager;

public class TerminalManagerTest
{
	private FakeDevice m_device;
	private CardReader m_cardReader;
	private TerminalManager m_manager;
	private Customer m_customer;
	private CustomerDAO m_dao;
	private Injector m_injector;

	@Before
	public void before() throws Exception
	{
		m_injector = new Injector(new TestBindings());
		m_manager = m_injector.get(TerminalManager.class);

		// Some helpers for generating the readings
		m_device = (FakeDevice) m_injector.get(Device.class);
		m_cardReader = m_injector.get(CardReader.class);
		m_cardReader.connect();

		((MemoryZoneManager) m_injector.get(ZoneManager.class)).setZone(2);

		// Create a customer
		m_dao = m_injector.get(CustomerDAO.class);
		Customer c = new Customer();
		c.setCardId(1234);
		c.setName("Joe");
		c.setBalance(100);
		c.setStatus(Customer.ACTIVE);
		m_dao.insert(c);
		m_customer = c;
	}

	@Test
	public void testHandleInvalidCustomer() throws Exception
	{
		// Set customer's status to inactive
		m_customer.setStatus(Customer.INACTIVE);
		m_dao.update(m_customer);

		Reading reading = getReading(1234);
		m_manager.handleReading(reading);

		assertTrue(reading.isResponseSent());
		assertEquals(Type.INVALID_CARD, reading.getResponse().getType());
	}

	@Test
	public void testHandleReadingInvalidCard() throws Exception
	{
		Reading reading = getReading(9999);
		m_manager.handleReading(reading);

		assertTrue(reading.isResponseSent());
		assertEquals(Type.INVALID_CARD, reading.getResponse().getType());
	}

	@Test
	public void testHandleReadingCheckIn() throws Exception
	{
		Reading reading = getReading(1234);

		// First reading, should end up in a check-in
		m_manager.handleReading(reading);

		assertTrue(reading.isResponseSent());
		assertEquals(Type.CHECKED_IN, reading.getResponse().getType());
	}

	@Test
	public void testHandleReadingCheckOut() throws Exception
	{
		// Let another test method to the check-in
		testHandleReadingCheckIn();

		fetchCustomer();
		long balance = m_customer.getBalance();

		Reading reading = getReading(1234);
		m_manager.handleReading(reading);

		assertTrue(reading.isResponseSent());
		assertEquals(Type.CHECKED_OUT, reading.getResponse().getType());

		fetchCustomer();
		long expectedBalance = balance - m_injector.get(ZoneManager.class).getPrice(1, 1);
		assertEquals(expectedBalance, m_customer.getBalance());
	}

	@Test
	public void testHandleReadingCheckOutTooLate() throws Exception
	{
		((TestJourneyManager) m_injector.get(JourneyManager.class)).setMaximumTravelTimeInMinutes(0);

		testHandleReadingCheckIn();

		Reading reading = getReading(1234);
		m_manager.handleReading(reading);

		assertTrue(reading.isResponseSent());
		assertEquals(Type.TOO_LATE_CHECK_OUT, reading.getResponse().getType());
	}

	private void fetchCustomer() throws DBException
	{
		m_customer = m_dao.findByCard(1234);
	}

	private Reading getReading(long cardId) throws DeviceException
	{
		m_device.scanCard(cardId);
		return m_cardReader.readCard();
	}
}
