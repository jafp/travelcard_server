package dk.ihk.tcp.db.mysql;

import dk.ihk.tcp.db.CustomerDAO;
import dk.ihk.tcp.db.DAOFactoryBackend;
import dk.ihk.tcp.db.JourneyDAO;

public class MySQLDAOFactory implements DAOFactoryBackend
{
	@Override
	public CustomerDAO getCustomerDAO()
	{
		return new MySQLCustomerDAO();
	}

	@Override
	public JourneyDAO getJourneyDAO()
	{
		return new MySQLJourneyDAO();
	}
}
