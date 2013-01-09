package dk.ihk.tcp.test.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import dk.ihk.tcp.datamodel.Customer;
import dk.ihk.tcp.datamodel.Journey;
import dk.ihk.tcp.db.CustomerDAO;
import dk.ihk.tcp.manager.JourneyManager;
import dk.ihk.tcp.test.TestBindings;
import dk.ihk.tcp.test.TestJourneyManager;
import dk.ihk.tcp.util.inject.Injector;
import dk.ihk.tcp.zones.MemoryZoneManager;
import dk.ihk.tcp.zones.ZoneManager;

public class JourneyManagerTest
{
	private MemoryZoneManager m_zones;
	private JourneyManager m_mgr;
	private Customer m_c;

	@Before
	public void before() throws Exception
	{
		Injector injector = new Injector(new TestBindings());

		m_zones = (MemoryZoneManager) injector.get(ZoneManager.class);
		m_zones.setZone(1);

		m_mgr = injector.get(JourneyManager.class);

		m_c = new Customer();
		m_c.setCardId(10);
		m_c.setName("Joe");
		m_c.setBalance(100);
		m_c.setStatus(Customer.ACTIVE);

		injector.get(CustomerDAO.class).insert(m_c);
	}

	@Test
	public void testGetCurrent()
	{
		assertNull(m_mgr.getCurrent(m_c));
	}

	@Test
	public void testCheckIn() throws Exception
	{
		assertNull(m_mgr.getCurrent(m_c));

		// Perform a "check in" in zone 2
		m_zones.setZone(2);
		System.out.println("zones  " + m_zones.hashCode());
		Journey journey = m_mgr.checkIn(m_c);

		assertNotNull(journey);
		assertEquals(m_c.getId(), journey.getCustomerId());
		assertEquals(2, journey.getStartZone());
		assertEquals(Journey.CHECKED_IN, journey.getStatus());

		assertEquals(journey, m_mgr.getCurrent(m_c));
	}

	@Test
	public void testCheckOut() throws Exception
	{
		long balanceBefore = m_c.getBalance();

		m_zones.setZone(1);
		Journey j = m_mgr.checkIn(m_c);

		m_zones.setZone(4);
		j = m_mgr.checkOut(m_c);

		assertEquals(1, j.getStartZone());
		assertEquals(4, j.getEndZone());
		assertEquals(Journey.CHECKED_OUT, j.getStatus());

		// Zone 1 --> 4 = 3 zones - price: kr. 19
		assertEquals(m_zones.getPrice(1, 4), j.getPrice());

		long expectedBalanceAfter = balanceBefore - j.getPrice();
		assertEquals(expectedBalanceAfter, m_c.getBalance());
	}

	@Test
	public void testCheckOutTooLate() throws Exception
	{
		long before = m_c.getBalance();

		m_mgr.checkIn(m_c);
		Journey j = m_mgr.checkOutTooLate(m_c);

		assertNotNull(j);
		assertEquals(Journey.CHECKED_OUT_TOO_LATE, j.getStatus());
		assertEquals(50, j.getPrice());
		assertEquals(before - 50, m_c.getBalance());
	}

	@Test
	public void testIsCheckedIn() throws Exception
	{
		m_zones.setZone(1);
		assertFalse(m_mgr.isCheckedIn(m_c));
		m_mgr.checkIn(m_c);
		assertTrue(m_mgr.isCheckedIn(m_c));
		m_mgr.checkOut(m_c);
		assertFalse(m_mgr.isCheckedIn(m_c));
	}

	@Test
	public void testTooLateToCheckOut() throws Exception
	{
		m_mgr.checkIn(m_c);
		Thread.sleep(10);

		((TestJourneyManager) m_mgr).setMaximumTravelTimeInMinutes(1);
		assertFalse(m_mgr.tooLateToCheckOut(m_c));

		((TestJourneyManager) m_mgr).setMaximumTravelTimeInMinutes(0);
		assertTrue(m_mgr.tooLateToCheckOut(m_c));
	}

	@Test
	public void testHasSufficientFunds()
	{
		m_c.setBalance(100);
		assertTrue(m_mgr.hasSufficientFundsToCheckIn(m_c));

		m_c.setBalance(0);
		assertFalse(m_mgr.hasSufficientFundsToCheckIn(m_c));

		m_c.setBalance(50);
		assertTrue(m_mgr.hasSufficientFundsToCheckIn(m_c));

		m_c.setBalance(49);
		assertFalse(m_mgr.hasSufficientFundsToCheckIn(m_c));
	}

}
