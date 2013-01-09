package dk.ihk.tcp.test.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dk.ihk.tcp.datamodel.Journey;
import dk.ihk.tcp.db.DAOFactory;
import dk.ihk.tcp.db.DBException;
import dk.ihk.tcp.db.JourneyDAO;
import dk.ihk.tcp.test.DBTestUtil;
import dk.ihk.tcp.util.DateHelper;

public class JourneyDAOTest
{
	private JourneyDAO m_dao;

	@BeforeClass
	public static void createTables() throws ClassNotFoundException, SQLException, IOException, DBException
	{
		DBTestUtil.setup();
	}

	@Before
	public void setup() throws Exception
	{
		m_dao = DAOFactory.getJourneyDAO();
		m_dao.deleteAll();
	}

	@Test
	public void testInsertAndFindByCustomer() throws DBException
	{
		Journey j = new Journey();
		j.setCustomerId(1);
		j.setStartTime(DateHelper.getNow());
		j.setStartZone(3);
		j.setEndTime(DateHelper.getNow());
		j.setEndZone(30);
		j.setPrice(20);
		j.setStatus(1);

		assertEquals(0, j.getId());

		m_dao.insert(j);

		assertTrue(j.getId() > 0);

		assertEquals(j, m_dao.findLastByCustomer(1));
	}

	@Test
	public void testUpdate() throws DBException
	{
		Journey j = new Journey();
		j.setCustomerId(123);
		j.setPrice(30);

		m_dao.insert(j);

		assertTrue(j.getId() > 0);
		assertEquals(j, m_dao.findLastByCustomer(123));

		j.setPrice(40);
		m_dao.update(j);

		assertEquals(j, m_dao.findLastByCustomer(123));
	}
}
