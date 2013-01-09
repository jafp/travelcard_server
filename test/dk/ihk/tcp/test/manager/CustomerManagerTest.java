package dk.ihk.tcp.test.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import dk.ihk.tcp.datamodel.Customer;
import dk.ihk.tcp.db.CustomerDAO;
import dk.ihk.tcp.manager.CustomerManager;
import dk.ihk.tcp.test.TestBindings;
import dk.ihk.tcp.util.inject.Injector;

public class CustomerManagerTest
{
	private Customer m_customer;
	private CustomerDAO m_dao;
	private CustomerManager m_manager;

	@Before
	public void before() throws Exception
	{
		Injector injector = new Injector(new TestBindings());
		m_dao = injector.get(CustomerDAO.class);
		m_manager = injector.get(CustomerManager.class);

		m_customer = new Customer();
		m_customer.setStatus(Customer.ACTIVE);
		m_customer.setName("Joe John");
		m_customer.setCardId(1234L);
		m_customer.setBalance(100);
		m_dao.insert(m_customer);
	}

	@Test
	public void testUpdateCustomer() throws Exception
	{
		assertEquals("Joe John", m_customer.getName());

		m_customer.setName("Jimmy Hendrix");
		m_manager.updateOrInsertCustomer(m_customer);

		Customer c = m_manager.getCustomerByCard(1234);
		assertEquals(m_customer.getName(), c.getName());
	}

	@Test
	public void testFindCustomerByCardId()
	{
		assertNotNull(m_manager.getCustomerByCard(1234));
		assertEquals(m_customer, m_manager.getCustomerByCard(1234));
		assertNull(m_manager.getCustomerByCard(1111));
	}

	public void testFindCustomerById()
	{
		Customer c = m_manager.getCustomerById(m_customer.getId());
		assertNotNull(c);
		assertEquals(m_customer, c);
	}

	@Test
	public void testGetCustomers()
	{
		assertEquals(1, m_manager.getCustomers().size());
	}

	@Test
	public void testIsValid()
	{
		assertTrue(m_manager.isValid(m_customer));
		m_customer.setStatus(Customer.INACTIVE);
		assertFalse(m_manager.isValid(m_customer));
		assertFalse(m_manager.isValid(null));
	}
}
