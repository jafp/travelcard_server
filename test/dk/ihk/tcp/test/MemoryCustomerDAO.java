package dk.ihk.tcp.test;

import java.util.SortedSet;

import dk.ihk.tcp.datamodel.Customer;
import dk.ihk.tcp.db.CustomerDAO;
import dk.ihk.tcp.db.DBException;
import dk.ihk.tcp.util.DateHelper;
import dk.ihk.tcp.util.inject.Inject;

public class MemoryCustomerDAO implements CustomerDAO
{
	private final MemoryDAO<Customer> m_dao = new MemoryDAO<Customer>();

	@Inject
	public MemoryCustomerDAO()
	{

	}

	@Override
	public void insert(Customer c) throws DBException
	{
		if (c.getName() == null)
		{
			throw new DBException("Name null");
		}

		c.setCreatedAt(DateHelper.getNow());
		c.setUpdatedAt(c.getCreatedAt());
		m_dao.insertOrUpdate(c);
	}

	@Override
	public void update(Customer c) throws DBException
	{
		c.setUpdatedAt(DateHelper.getNow());
		m_dao.insertOrUpdate(c);
	}

	@Override
	public boolean delete(Customer c) throws DBException
	{
		return m_dao.delete(c);
	}

	@Override
	public boolean deleteAll() throws DBException
	{
		m_dao.deleteAll();
		return true;
	}

	@Override
	public SortedSet<Customer> find() throws DBException
	{
		return m_dao.getValues();
	}

	@Override
	public Customer findById(long l) throws DBException
	{
		return m_dao.findById(l);
	}

	@Override
	public Customer findByCard(long cardId) throws DBException
	{
		for (Customer c : find())
		{
			if (c.getCardId() == cardId)
			{
				return c;
			}
		}
		return null;
	}

	@Override
	public Customer findByName(String name) throws DBException
	{
		for (Customer c : find())
		{
			if (c.getName().equals(name))
			{
				return c;
			}
		}
		return null;
	}

}
