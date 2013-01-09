package dk.ihk.tcp.gui.admin;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import dk.ihk.tcp.datamodel.Customer;
import dk.ihk.tcp.gui.selfservice.SelfServiceController;
import dk.ihk.tcp.manager.CustomerManager;
import dk.ihk.tcp.terminal.Runner;
import dk.ihk.tcp.terminal.TerminalDriver;
import dk.ihk.tcp.terminal.TerminalDriver.Mode;
import dk.ihk.tcp.terminal.TerminalDriver.State;
import dk.ihk.tcp.terminal.TerminalDriverListener;
import dk.ihk.tcp.util.inject.Injector;

public class AdminController implements TerminalDriverListener
{
	private MainView m_view;
	private Runner m_runner;
	private TerminalDriver m_terminal;
	private CustomerManager m_manager;
	private CustomerListModel m_customerListModel;
	private JourneyTableModel m_journeyTableModel;

	public static void main(String... args) throws Exception
	{
		new AdminController();
	}

	public AdminController() throws Exception
	{
		m_runner = null;
		try
		{
			m_runner = new Runner(new GUIBindings());
		}
		catch (Exception e)
		{
			if (e.getClass() == CommunicationsException.class)
			{
				JOptionPane.showMessageDialog(null, "Der kunne ikke forbindes til databasen.");
			}
		}

		if (m_runner != null)
		{
			Injector injector = m_runner.getInjector();
			m_manager = injector.get(CustomerManager.class);
			m_terminal = injector.get(TerminalDriver.class);
			m_customerListModel = injector.get(CustomerListModel.class);
			m_journeyTableModel = injector.get(JourneyTableModel.class);
			m_view = new MainView(this);
			m_view.setCustomerListModel(m_customerListModel);
			m_view.setJourneyTableModel(m_journeyTableModel);
			m_terminal.setMode(TerminalDriver.Mode.SCANNER);
			String serial = JOptionPane.showInputDialog(SelfServiceController.SERIAL_NUMBER_TEXT);
			m_terminal.setSerialNumber(serial);
			m_terminal.addListener(this);
			m_terminal.start();
		}
	}

	public void customerSelected(Customer c)
	{
		if (c != null)
		{
			m_view.setName(c.getName());
			m_view.setCard(c.getCardId());
			m_view.setBalance(c.getBalance());
			m_view.setStatus(c.getStatus());

			m_journeyTableModel.setCustomer(c);
		}
		else
		{
			m_view.getNameText().setText("");
			m_view.getCardText().setText("");
			m_view.getBalanceText().setText("");
			m_view.getStatusCombo().setSelectedIndex(0);
		}
	}

	public void newPressed(ActionEvent e)
	{
		Customer c = new Customer();
		m_customerListModel.addToUnsaved(c);
		m_view.getCustomerList().setSelectedValue(c, true);
	}

	public void deletePressed(ActionEvent e)
	{
		Customer c = m_view.getSelected();
		if (c != null)
		{
			if (JOptionPane.showConfirmDialog(m_view, "Bekræft at du vil slette denne kunde") == 0)
			{
				try
				{
					m_manager.deleteCustomer(c);
					m_customerListModel.update();

					customerSelected(m_view.getCustomerList().getSelectedValue());
					JOptionPane.showMessageDialog(m_view, "Kunden blev slettet!");
				}
				catch (Exception e1)
				{
					JOptionPane.showMessageDialog(m_view, "Kunden kunne ikke slettes.");
				}
			}
		}
	}

	public void savePressed(ActionEvent e)
	{
		if (JOptionPane.showConfirmDialog(m_view, "Bekr�ft at du vil gemme �ndringerne") == 0)
		{
			Customer c = m_view.getSelected();
			if (c != null)
			{
				c.setName(m_view.getName());
				c.setStatus(m_view.getStatus());
				c.setCardId(m_view.getCard());

				try
				{
					m_manager.updateOrInsertCustomer(c);
					JOptionPane.showMessageDialog(m_view, "Kunden er gemt!");

					m_customerListModel.removeFromUnsaved(c);
					m_view.getCustomerList().setSelectedValue(c, true);
				}
				catch (Exception ex)
				{
					JOptionPane.showMessageDialog(m_view, "Der opstod en fejl. Kunden er ikke gemt! (" + ex.getMessage() + ")", "Fejl",
							JOptionPane.ERROR_MESSAGE);
				}

				m_customerListModel.update();
				markChanges();
			}
		}
	}

	@Override
	public void onCardScanned(long id)
	{
		Customer c = m_manager.getCustomerByCard(id);
		if (c != null)
		{
			m_view.setSelected(c);
		}
		else if (m_view.getSelected() != null)
		{
			m_view.setCard(id);
		}
		markChanges();
	}

	public void markChanges()
	{
		Customer c = m_view.getSelected();
		if (c != null)
		{
			Customer o = m_manager.getCustomerByCard(c.getCardId());
			if (o != null)
			{
				m_view.getNameText().setForeground((o.getName() != null && o.getName().equals(m_view.getName())) ? Color.black : Color.red);
				m_view.getCardText().setForeground(o.getCardId() == m_view.getCard() ? Color.black : Color.red);
				m_view.getStatusCombo().setForeground(o.getStatus() == m_view.getStatus() ? Color.black : Color.red);
			}
		}
	}

	@Override
	public void onStateChange(State newState)
	{
		System.out.println("Scanner: " + newState.toString());
	}

	@Override
	public void onModeChange(Mode newMode)
	{
	}

	public void depositPressed()
	{
		boolean success = true;
		double amount = 0;
		Customer c = m_view.getSelected();
		if (c != null)
		{
			String str = JOptionPane.showInputDialog(m_view, "Indtast beløb der skal indsættes på " + c.getName() + "s konto");
			try
			{
				amount = Double.valueOf(str);
				if (amount % 1 != 0)
				{
					JOptionPane.showMessageDialog(m_view, "Der kan kun indsætte hele beløb, prøv igen!");
					success = false;
				}
				if (amount < 0)
				{
					JOptionPane.showMessageDialog(m_view, "Der kan ikke indsættes negative beløb, prøv igen!");
					success = false;
				}
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(m_view, "Der kan kun indtastes tal, prøv igen!");
				success = false;
			}
			if (success)
			{
				try
				{
					c.setBalance(c.getBalance() + (int) amount);
					m_manager.updateOrInsertCustomer(c);
					customerSelected(c);
					JOptionPane
							.showMessageDialog(m_view, "Beløbet (kr. " + ((int) amount) + ") blev indsat på " + c.getName() + "s konto.");
				}
				catch (Exception e)
				{
					JOptionPane.showMessageDialog(m_view, "Der opstod en fejl, prøv igen!");
				}
			}
		}
	}

}
