package dk.ihk.tcp.test.dao;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;

import dk.ihk.tcp.db.DAOFactory;
import dk.ihk.tcp.db.DBException;
import dk.ihk.tcp.test.MemoryDAOFactoryBackend;

public class MemoryCustomerDAOTest extends CustomerDAOTest
{
	@BeforeClass
	public static void createTables() throws ClassNotFoundException, SQLException, IOException, DBException
	{
	}

	@Override
	@Before
	public void setup() throws Exception
	{
		DAOFactory.setBackend(new MemoryDAOFactoryBackend());
		super.setup();
	}
}
