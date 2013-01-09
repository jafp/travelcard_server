package dk.ihk.tcp.db;

import dk.ihk.tcp.db.mysql.MySQLDAOFactory;

/**
 * Factory for creating DAO objects. The factory depends on a DAOFactoryBackend
 * which makes it easy to deliver different DAO objects.
 * 
 * @author Jacob Pedersen
 */
public class DAOFactory
{
	/**
	 * The backend - defaults to MySQL backend
	 */
	private static DAOFactoryBackend m_backend = new MySQLDAOFactory();

	/**
	 * Change the factory backend
	 * 
	 * @param backend The new backend
	 */
	public static void setBackend(DAOFactoryBackend backend)
	{
		m_backend = backend;
	}

	/**
	 * @return A customer DAO
	 */
	public static CustomerDAO getCustomerDAO()
	{
		return m_backend.getCustomerDAO();
	}

	/**
	 * @return A journey DAO
	 */
	public static JourneyDAO getJourneyDAO()
	{
		return m_backend.getJourneyDAO();
	}

}
