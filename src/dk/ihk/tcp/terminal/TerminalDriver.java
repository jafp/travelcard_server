package dk.ihk.tcp.terminal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import dk.ihk.tcp.cardreader.CardReader;
import dk.ihk.tcp.cardreader.DeviceException;
import dk.ihk.tcp.cardreader.Reading;
import dk.ihk.tcp.cardreader.Response;
import dk.ihk.tcp.cardreader.Response.Type;
import dk.ihk.tcp.datamodel.Customer;
import dk.ihk.tcp.db.CustomerDAO;
import dk.ihk.tcp.db.DAOFactory;
import dk.ihk.tcp.db.DBException;
import dk.ihk.tcp.manager.TerminalManager;
import dk.ihk.tcp.util.inject.Inject;

/**
 * 
 */
public class TerminalDriver implements Runnable
{
	/**
	 * Terminal state
	 */
	public enum State
	{
		/**
		 * The terminal hardware is connected
		 */
		CONNECTED,
		/**
		 * The terminal hardware is disconnected
		 */
		DISCONNECTED
	};

	/**
	 * The mode this terminal is operating in.
	 */
	public enum Mode
	{
		/**
		 * Normal travel card customer mode
		 */
		NORMAL,
		/**
		 * Mode for scanning cards for administration purposes.
		 */
		SCANNER
	};

	/**
	 * The logger object
	 */
	private final Logger m_logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * The terminal serial number loaded from the properties.
	 */
	private String m_serialNumber;

	/**
	 * The card reader used for reading cards
	 */
	private final CardReader m_cardReader;

	/**
	 * The terminal manager that handles the logic
	 */
	private final TerminalManager m_terminalManager;

	/**
	 * The terminal mode
	 */
	private Mode m_mode = Mode.NORMAL;

	/**
	 * The connection state to the terminal
	 */
	private State m_state = State.DISCONNECTED;

	/**
	 * Thread for this driver
	 */
	private Thread m_thread;

	/**
	 * List of terminal driver listeners
	 */
	private final List<TerminalDriverListener> m_listeners = new ArrayList<TerminalDriverListener>();

	/**
	 * Flag indicating whether the driver is running
	 */
	private boolean m_running;

	@Inject
	public TerminalDriver(TerminalManager terminalMgr, CardReader cardReader) throws FileNotFoundException, IOException,
			ClassNotFoundException, SQLException
	{
		m_logger.info("IHK TCP - starting");

		m_cardReader = cardReader;
		m_terminalManager = terminalMgr;
		m_running = true;
	}

	public void setupTestData() throws DBException
	{
		DAOFactory.getJourneyDAO().deleteAll();
		CustomerDAO customerDao = DAOFactory.getCustomerDAO();
		customerDao.deleteAll();

		Customer c = new Customer();
		c.setName("Joe");
		c.setCardId(3611177931L);
		c.setBalance(100);
		c.setStatus(Customer.ACTIVE);
		customerDao.insert(c);

		c = new Customer();
		c.setName("Hans");
		c.setCardId(12111);
		c.setBalance(200);
		c.setStatus(Customer.ACTIVE);
		customerDao.insert(c);

	}

	/**
	 * Starts the driver.
	 * 
	 * @throws Exception
	 */
	public void start() throws Exception
	{
		m_running = true;
		notifyOnStateChange(m_state);
		notifyOnModeChange(m_mode);

		m_thread = new Thread(this);
		m_thread.setName("TERMINAL " + m_serialNumber);
		m_thread.start();

		// m_cardReader.connect();
	}

	public void join() throws InterruptedException
	{
		m_thread.join();
	}

	public void stop()
	{
		if (m_running)
		{
			m_running = false;
			m_cardReader.disconnect();
		}
	}

	public State getState()
	{
		return m_state;
	}

	public void setSerialNumber(String sn)
	{
		m_serialNumber = sn;
		m_cardReader.setSerialNumber(sn);
	}

	public String getSerialNumber()
	{
		return m_serialNumber;
	}

	public Mode getMode()
	{
		return m_mode;
	}

	public void setMode(Mode mode)
	{
		m_mode = mode;
		notifyOnModeChange(m_mode);
	}

	public void addListener(TerminalDriverListener listener)
	{
		m_listeners.add(listener);
	}

	public void removeListener(TerminalDriverListener listener)
	{
		m_listeners.remove(listener);
	}

	@Override
	public void run()
	{
		while (m_running)
		{
			try
			{
				// Connect should throw an exception if connection fails, the
				// device is busy or something like
				m_cardReader.connect();
			}
			catch (DeviceException e)
			{
				m_logger.info("Could not connect: " + e.getMessage());
			}
			// Go into main loop if we connected successfully
			if (m_cardReader.isConnected())
			{
				m_state = State.CONNECTED;
				m_logger.info("Terminal with ID " + m_serialNumber + " found, connected");
				notifyOnStateChange(m_state);

				try
				{
					loop();
				}
				catch (Exception e)
				{
					m_logger.info("Terminal error, retrying connection");
				}
			}
			else
			{
				if (m_state != State.DISCONNECTED)
				{
					m_state = State.DISCONNECTED;
					notifyOnStateChange(m_state);
				}

				m_logger.info("Could not find terminal with ID " + m_serialNumber);
				try
				{
					Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
				}
			}
		}
	}

	private void loop() throws Exception
	{
		while (m_running)
		{
			Reading reading = m_cardReader.readCard();

			if (reading == null)
			{
				continue;
			}

			if (m_mode == Mode.SCANNER)
			{
				notifyOnCardScanned(reading.getCardId());
				reading.respondWith(Response.create(Type.OK));
			}
			else
			{
				// Normal travel card terminal mode
				try
				{
					m_terminalManager.handleReading(reading);

				}
				catch (Exception e)
				{
					// Something went wrong - do nothing and hope the user tries
					// one more time.
					// e.printStackTrace();

					m_logger.info("Exception during handle reading: " + e.getMessage());

					reading.respondWith(Response.create(Type.ERROR));
				}
			}
		}
	}

	private void notifyOnCardScanned(long id)
	{
		for (TerminalDriverListener l : m_listeners)
		{
			l.onCardScanned(id);
		}
	}

	private void notifyOnStateChange(State state)
	{
		for (TerminalDriverListener l : m_listeners)
		{
			l.onStateChange(state);
		}
	}

	private void notifyOnModeChange(Mode mode)
	{
		for (TerminalDriverListener l : m_listeners)
		{
			l.onModeChange(mode);
		}
	}

}
