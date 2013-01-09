package dk.ihk.tcp.manager.impl;

import java.util.SortedSet;
import java.util.TreeSet;

import dk.ihk.tcp.datamodel.Customer;
import dk.ihk.tcp.db.CustomerDAO;
import dk.ihk.tcp.db.DBException;
import dk.ihk.tcp.manager.CustomerManager;
import dk.ihk.tcp.util.inject.Inject;

/**
 * Default implementation of the customer manager according to the requirements
 * in the SRS.
 * 
 * @author Jacob Pedersen
 * 
 */
public class DefaultCustomerManager implements CustomerManager
{
	/**
	 * The customer data access object
	 */
	private final CustomerDAO m_dao;

	@Inject
	public DefaultCustomerManager(CustomerDAO dao)
	{
		m_dao = dao;
	}

	/**
	 * @see dk.ihk.tcp.manager.CustomerManager#updateOrInsertCustomer(dk.ihk.tcp.datamodel.Customer)
	 */
	@Override
	public void updateOrInsertCustomer(Customer customer) throws Exception
	{
		boolean isNew = customer.getId() == 0;
		Customer c = m_dao.findByCard(customer.getCardId());

		if (isNew)
		{
			if (c != null)
			{
				throw new Exception("Kort-ID er allerede i brug");
			}
			m_dao.insert(customer);
		}
		else
		{
			if (c != null && c.getId() != customer.getId())
			{
				throw new Exception("Kort-ID er allerede i brug");
			}
			m_dao.update(customer);
		}

	}

	/**
	 * @see dk.ihk.tcp.manager.CustomerManager#getCustomerByCard(long)
	 */
	@Override
	public Customer getCustomerByCard(long cardId)
	{
		try
		{
			return m_dao.findByCard(cardId);
		}
		catch (DBException e)
		{

		}
		return null;
	}

	/**
	 * @see dk.ihk.tcp.manager.CustomerManager#getCustomers()
	 */
	@Override
	public SortedSet<Customer> getCustomers()
	{
		try
		{
			return m_dao.find();
		}
		catch (DBException e)
		{

		}
		return new TreeSet<Customer>();
	}

	/**
	 * @see dk.ihk.tcp.manager.CustomerManager#isValid(dk.ihk.tcp.datamodel.Customer)
	 */
	@Override
	public boolean isValid(Customer customer)
	{
		return customer != null && customer.getStatus() == Customer.ACTIVE;
	}

	@Override
	public void deleteCustomer(Customer c) throws Exception
	{
		m_dao.delete(c);
	}

	@Override
	public Customer getCustomerById(long customerId)
	{
		try
		{
			return m_dao.findById(customerId);
		}
		catch (DBException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
