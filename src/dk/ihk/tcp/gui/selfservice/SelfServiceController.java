package dk.ihk.tcp.gui.selfservice;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import dk.ihk.tcp.datamodel.Customer;
import dk.ihk.tcp.gui.admin.GUIBindings;
import dk.ihk.tcp.gui.admin.JourneyTableModel;
import dk.ihk.tcp.manager.CustomerManager;
import dk.ihk.tcp.terminal.Runner;
import dk.ihk.tcp.terminal.TerminalDriver;
import dk.ihk.tcp.terminal.TerminalDriver.Mode;
import dk.ihk.tcp.terminal.TerminalDriver.State;
import dk.ihk.tcp.terminal.TerminalDriverListener;
import dk.ihk.tcp.util.inject.Injector;

/**
 * This class acts as controller for the self service program.
 * 
 * @author Jacob Pedersen
 */
public class SelfServiceController implements TerminalDriverListener
{
	/**
	 * Translated strings
	 */
	public static final String WELCOME_TEXT = "Velkommen - scan dit kort for at logge ind";
	public static final String OUT_OF_ORDER_TEXT = "Automaten er ude af drift!";
	public static final String SERIAL_NUMBER_TEXT = "Angiv serienummeret på den terminal der skal søges efter.";
	public static final String COULD_NOT_CONNECT_TO_DB = "Der kunne ikke forbindes til databasen.";
	/**
	 * Member variables
	 */
	private Runner m_runner;
	private Injector m_injector;
	private TerminalDriver m_driver;
	private CustomerManager m_customers;
	private LoginView m_loginView;
	private MainView m_mainView;
	private JourneyTableModel m_tableModel;
	private Customer m_activeCustomer;

	/**
	 * Entry point
	 */
	public static void main(String[] args) throws Exception
	{
		new SelfServiceController();
	}

	/**
	 * Constructor. When starting the program, it asks for the serial number of
	 * the terminal to look for.
	 */
	public SelfServiceController()
	{
		try
		{
			m_runner = new Runner(new GUIBindings());
			m_injector = m_runner.getInjector();
			m_driver = m_runner.getDriver();
			m_loginView = new LoginView();
			m_mainView = new MainView(this);
			m_mainView.addWindowListener(new MainViewWindowListener());
			m_tableModel = m_injector.get(JourneyTableModel.class);
			m_customers = m_injector.get(CustomerManager.class);
			m_mainView.setJourneyTableModel(m_tableModel);
			m_loginView.setVisible(true);
			m_driver.setMode(Mode.SCANNER);
			m_driver.addListener(this);

			String serial = JOptionPane.showInputDialog(SERIAL_NUMBER_TEXT);
			m_driver.setSerialNumber(serial);

			m_driver.start();
		}
		catch (Exception e)
		{
			if (e.getClass() == CommunicationsException.class)
			{
				JOptionPane.showMessageDialog(null, COULD_NOT_CONNECT_TO_DB);
			}
		}
	}

	@Override
	public void onStateChange(State newState)
	{
		JLabel label = m_loginView.getWelcomeLabel();
		if (newState == State.CONNECTED)
		{
			label.setText(WELCOME_TEXT);
		}
		else
		{
			label.setText(OUT_OF_ORDER_TEXT);
		}
	}

	@Override
	public void onModeChange(Mode newMode)
	{
	}

	@Override
	public void onCardScanned(long id)
	{
		Customer customer = m_customers.getCustomerByCard(id);
		if (m_customers.isValid(customer))
		{
			showCustomerWindow(customer);
		}
		else
		{
			JOptionPane.showMessageDialog(m_loginView, "Ugyldigt kort!");
		}
	}

	private void showCustomerWindow(Customer customer)
	{
		if (m_loginView.isVisible())
		{
			m_loginView.setVisible(false);
		}

		m_activeCustomer = customer;
		updateCustomerNameLabel();
		m_tableModel.setCustomer(m_activeCustomer);
		m_mainView.setVisible(true);
	}

	private void updateCustomerNameLabel()
	{
		m_mainView.getWelcomeLabel().setText("Velkommen " + m_activeCustomer.getName() + ", saldo: kr. " + m_activeCustomer.getBalance());
	}

	public void depositPressed(String text)
	{
		double amount = 0;
		boolean success = true;

		try
		{
			amount = Double.valueOf(text);
			if (amount % 1 != 0)
			{
				JOptionPane.showMessageDialog(m_mainView, "Der kan kun indsætte hele beløb, prøv igen!");
				success = false;
			}
			if (amount < 0)
			{
				JOptionPane.showMessageDialog(m_mainView, "Der kan ikke indsættes negative beløb, prøv igen!");
				success = false;
			}
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(m_mainView, "Der kan kun indtastes tal, prøv igen!");
			success = false;
		}
		if (success)
		{
			try
			{
				m_activeCustomer.setBalance(m_activeCustomer.getBalance() + (int) amount);
				m_customers.updateOrInsertCustomer(m_activeCustomer);

				updateCustomerNameLabel();
				m_mainView.getAmonutTextField().setText("");
				JOptionPane.showMessageDialog(m_mainView, "Beløb indsat!");
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(m_mainView, "Der opstod en fejl, prøv igen!");
			}
		}
	}

	private class MainViewWindowListener implements WindowListener
	{
		@Override
		public void windowOpened(WindowEvent e)
		{
		}

		@Override
		public void windowClosing(WindowEvent e)
		{
			m_loginView.setVisible(true);
		}

		@Override
		public void windowClosed(WindowEvent e)
		{
		}

		@Override
		public void windowIconified(WindowEvent e)
		{
		}

		@Override
		public void windowDeiconified(WindowEvent e)
		{
		}

		@Override
		public void windowActivated(WindowEvent e)
		{
		}

		@Override
		public void windowDeactivated(WindowEvent e)
		{
		}
	}
}
