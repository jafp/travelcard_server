package dk.ihk.tcp.test;

import dk.ihk.tcp.db.CustomerDAO;
import dk.ihk.tcp.db.DAOFactoryBackend;
import dk.ihk.tcp.db.JourneyDAO;

public class MemoryDAOFactoryBackend implements DAOFactoryBackend
{
	private final CustomerDAO m_customerDao;
	private final JourneyDAO m_journeyDao;

	public MemoryDAOFactoryBackend()
	{
		m_customerDao = new MemoryCustomerDAO();
		m_journeyDao = new MemoryJourneyDAO();
	}

	@Override
	public CustomerDAO getCustomerDAO()
	{
		return m_customerDao;
	}

	@Override
	public JourneyDAO getJourneyDAO()
	{
		return m_journeyDao;
	}

}
