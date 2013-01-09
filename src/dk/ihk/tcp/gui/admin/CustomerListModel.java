package dk.ihk.tcp.gui.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.swing.AbstractListModel;

import dk.ihk.tcp.datamodel.Customer;
import dk.ihk.tcp.manager.CustomerManager;
import dk.ihk.tcp.util.inject.Inject;

public class CustomerListModel extends AbstractListModel<Customer>
{
	private final CustomerManager m_manager;
	private final List<Customer> m_unsaved;
	private SortedSet<Customer> m_cache;

	@Inject
	public CustomerListModel(CustomerManager manager)
	{
		m_manager = manager;
		m_unsaved = new ArrayList<Customer>();
		update();
	}

	@Override
	public Customer getElementAt(int i)
	{
		return (Customer) m_cache.toArray()[i];
	}

	@Override
	public int getSize()
	{
		return m_cache.size();
	}

	public void update()
	{
		m_cache = m_manager.getCustomers();
		m_cache.addAll(m_unsaved);
		fireContentsChanged(this, 0, m_cache.size() - 1);
	}

	public void addToUnsaved(Customer c)
	{
		m_unsaved.add(c);
		update();
	}

	public void removeFromUnsaved(Customer c)
	{
		m_unsaved.remove(c);
		update();
	}
}
