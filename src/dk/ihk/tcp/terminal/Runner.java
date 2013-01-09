package dk.ihk.tcp.terminal;

import java.io.FileReader;
import java.nio.file.Paths;
import java.util.Properties;

import dk.ihk.tcp.db.DAOFactory;
import dk.ihk.tcp.db.DatabaseManager;
import dk.ihk.tcp.db.mysql.MySQLDAOFactory;
import dk.ihk.tcp.terminal.TerminalDriver.Mode;
import dk.ihk.tcp.util.inject.Bindings;
import dk.ihk.tcp.util.inject.Injector;

/**
 * Helps starting up a terminal driver.
 * 
 * @author Jacob Pedersen
 */
public class Runner
{
	/**
	 * The terminal driver
	 */
	private final TerminalDriver m_driver;

	/**
	 * The injector used to initialize the driver
	 */
	private final Injector m_injector;

	/**
	 * @param bindings Additional bindings
	 * @throws Exception
	 */
	public Runner(Bindings bindings) throws Exception
	{
		Properties properties = new Properties();
		properties.load(new FileReader(Paths.get(".", "config", "database.properties").toFile()));

		// Connect to database
		DatabaseManager.init(properties.getProperty("db.driver"), properties.getProperty("db.url"), properties.getProperty("db.user"),
				properties.getProperty("db.password"));

		DAOFactory.setBackend(new MySQLDAOFactory());

		m_injector = new Injector(new DefaultBindings());
		if (bindings != null)
		{
			m_injector.use(bindings);
		}

		m_driver = m_injector.get(TerminalDriver.class);
		m_driver.setMode(Mode.NORMAL);
	}

	public TerminalDriver getDriver()
	{
		return m_driver;
	}

	public Injector getInjector()
	{
		return m_injector;
	}
}
