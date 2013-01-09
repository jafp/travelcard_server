package dk.ihk.tcp.test.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dk.ihk.tcp.datamodel.Customer;
import dk.ihk.tcp.db.CustomerDAO;
import dk.ihk.tcp.db.DAOFactory;
import dk.ihk.tcp.db.DBException;
import dk.ihk.tcp.db.mysql.MySQLDAOFactory;
import dk.ihk.tcp.test.DBTestUtil;

public class CustomerDAOTest
{
	private CustomerDAO m_dao;

	@BeforeClass
	public static void createTables() throws ClassNotFoundException, SQLException, IOException, DBException
	{
		DAOFactory.setBackend(new MySQLDAOFactory());
		DBTestUtil.setup();
	}

	@Before
	public void setup() throws Exception
	{
		m_dao = DAOFactory.getCustomerDAO();
		m_dao.deleteAll();
	}

	@Test
	public void testInsert() throws DBException
	{
		Customer c = new Customer();
		c.setName("Jacob");

		assertEquals(0, c.getId());

		m_dao.insert(c);

		// Assert that an ID has been generated and the created at and updated
		// at dates are generated
		assertTrue(c.getId() > 0);
		assertNotNull(c.getCreatedAt());
		assertEquals(c.getCreatedAt(), c.getUpdatedAt());
	}

	@Test
	public void testFindById() throws DBException
	{
		Customer c, c2;

		c = new Customer();
		c.setName("Joe");
		c.setStatus(Customer.ACTIVE);
		c.setCardId(1234);
		c.setBalance(300);
		m_dao.insert(c);

		// Quick assert to make the sure the object has been inserted correctly
		assertNotNull(c.getId());

		c2 = m_dao.findById(c.getId());

		// The Customer class implements a equals(Object) method, to we just use
		// that to assert that the two objects are equals (contain the same
		// data)
		assertEquals(c, c2);
	}

	@Test
	public void testFindByIdNotExisting() throws DBException
	{
		Customer c = m_dao.findById(9999);
		assertNull(c);
	}

	@Test(expected = DBException.class)
	public void testInsertWithNullName() throws DBException
	{
		m_dao.insert(new Customer());
	}

	@Test
	public void testFindByCard() throws DBException
	{
		Customer c = new Customer();
		c.setName("Joe");
		c.setCardId(1234);
		m_dao.insert(c);

		Customer c2 = m_dao.findByCard(1234);
		assertEquals(c, c2);
	}

	@Test
	public void testFindByName() throws DBException
	{
		Customer c = new Customer();
		c.setName("Joe joe");
		m_dao.insert(c);
		assertEquals(c, m_dao.findByName("Joe joe"));
		assertNull(m_dao.findByName("No name"));
	}

	@Test
	public void testFindAll() throws DBException
	{
		assertEquals(0, m_dao.find().size());

		Customer c = new Customer();
		c.setName("Jacob");
		m_dao.insert(c);

		assertEquals(1, m_dao.find().size());

		c = new Customer();
		c.setName("Hans");
		m_dao.insert(c);

		assertEquals(2, m_dao.find().size());
	}

	@Test
	public void testUpdate() throws DBException
	{
		Customer c = new Customer();
		c.setName("Jacob");
		m_dao.insert(c);

		assertEquals("Jacob", m_dao.findById(c.getId()).getName());

		c.setName("Hansi Hinterseer");
		m_dao.update(c);

		assertEquals("Hansi Hinterseer", m_dao.findById(c.getId()).getName());
		assertNotNull(m_dao.findByName("Hansi Hinterseer"));
	}

	@Test
	public void testDeleteASingleCustomer() throws DBException
	{
		Customer c = new Customer();
		c.setName("Jacob");
		m_dao.insert(c);

		assertNotNull(m_dao.findByName("Jacob"));

		m_dao.delete(c);

		assertNull(m_dao.findByName("Jacob"));
	}

	@Test
	public void testDeleteAll() throws DBException
	{
		assertEquals(0, m_dao.find().size());

		Customer c = new Customer();
		c.setName("Jacob");
		m_dao.insert(c);
		assertEquals(1, m_dao.find().size());

		m_dao.deleteAll();

		assertEquals(0, m_dao.find().size());
	}
}
