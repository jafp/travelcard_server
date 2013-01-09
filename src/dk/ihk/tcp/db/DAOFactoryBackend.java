package dk.ihk.tcp.db;

/**
 * Backend for the DAOFactory
 * 
 * @author Jacob Pedersen
 */
public interface DAOFactoryBackend
{
	CustomerDAO getCustomerDAO();

	JourneyDAO getJourneyDAO();
}
