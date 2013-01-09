package dk.ihk.tcp.db.mysql;

import java.util.SortedSet;

import dk.ihk.tcp.datamodel.Customer;
import dk.ihk.tcp.db.CustomerDAO;
import dk.ihk.tcp.db.DBException;
import dk.ihk.tcp.util.DateHelper;
import dk.ihk.tcp.util.db.JDBCHelper;
import dk.ihk.tcp.util.db.Query;

public class MySQLCustomerDAO implements CustomerDAO
{
	@Override
	public void insert(Customer c) throws DBException
	{
		c.setCreatedAt(DateHelper.getNow());
		c.setUpdatedAt(c.getCreatedAt());

		c.setId(JDBCHelper.insert("INSERT INTO customers (card_id, balance, name, created_at, updated_at) VALUES (?, ?, ?, ?, ?)",
				c.getCardId(), c.getBalance(), c.getName(), c.getCreatedAt(), c.getUpdatedAt()));
	}

	@Override
	public void update(Customer c) throws DBException
	{
		if (c.getId() == 0)
		{
			insert(c);
		}
		else
		{
			c.setUpdatedAt(DateHelper.getNow());
			JDBCHelper.update("UPDATE customers SET card_id = ?, name = ?, balance = ?, status = ?, updated_at = ? WHERE id = ?",
					c.getCardId(), c.getName(), c.getBalance(), c.getStatus(), c.getUpdatedAt(), c.getId());
		}
	}

	@Override
	public SortedSet<Customer> find() throws DBException
	{
		return Query.<Customer> from("customers", Customer.class).orderBy("id").find();
	}

	@Override
	public Customer findById(long id) throws DBException
	{
		return Query.<Customer> from("customers", Customer.class).where("id", id).findOne();
	}

	@Override
	public Customer findByCard(long id) throws DBException
	{
		return Query.<Customer> from("customers").where("card_id", id).ofType(Customer.class).findOne();
	}

	@Override
	public Customer findByName(String name) throws DBException
	{
		return Query.<Customer> from("customers", Customer.class).where("name", name).findOne();
	}

	@Override
	public boolean delete(Customer c) throws DBException
	{
		return JDBCHelper.update("DELETE FROM customers WHERE id = ?", c.getId()) == 1;
	}

	@Override
	public boolean deleteAll() throws DBException
	{
		JDBCHelper.update("DELETE FROM customers");
		return true;
	}
}
