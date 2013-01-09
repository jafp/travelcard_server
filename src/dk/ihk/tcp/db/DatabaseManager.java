package dk.ihk.tcp.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Helper class for making database connections and storing them for later use.
 * It also helps with handling transactions.
 * 
 * @author Jacob Pedersen
 */
public class DatabaseManager
{
	/**
	 * The cached connection
	 */
	private static Connection m_connection;

	/**
	 * Create a new connection with the given parameters.
	 * 
	 * @param driver The driver class name
	 * @param url The url
	 * @param user The username
	 * @param password The password
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static void init(String driver, String url, String user, String password) throws SQLException, ClassNotFoundException,
			IOException
	{
		m_connection = getNewConnection(driver, url, user, password);
	}

	public static Connection getConnection() throws SQLException, ClassNotFoundException
	{
		return m_connection;
	}

	public static void beginTransaction() throws SQLException
	{
		if (m_connection != null)
		{
			m_connection.setAutoCommit(false);
		}
	}

	public static void commitTransaction() throws SQLException
	{
		if (m_connection != null)
		{
			m_connection.commit();
		}
	}

	public static void rollbackTransaction() throws SQLException
	{
		if (m_connection != null)
		{
			m_connection.rollback();
		}
	}

	public static void endTransaction() throws SQLException
	{
		if (m_connection != null)
		{
			m_connection.setAutoCommit(true);
		}
	}

	private static Connection getNewConnection(String driver, String url, String user, String password) throws SQLException,
			ClassNotFoundException
	{
		Class.forName(driver);
		return DriverManager.getConnection(url, user, password);
	}
}
