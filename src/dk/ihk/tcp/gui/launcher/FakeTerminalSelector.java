package dk.ihk.tcp.gui.launcher;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.SortedSet;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import dk.ihk.tcp.cardreader.fake.FakeDevice;
import dk.ihk.tcp.datamodel.Customer;
import dk.ihk.tcp.manager.CustomerManager;

public class FakeTerminalSelector extends JFrame
{
	private final FakeDevice m_device;
	private final JPanel m_contentPane;
	private final JList<Customer> m_list;

	/**
	 * Create the frame.
	 */
	public FakeTerminalSelector(FakeDevice device, CustomerManager manager)
	{
		m_device = device;

		setBounds(100, 100, 380, 300);
		m_contentPane = new JPanel();
		m_contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(m_contentPane);

		JLabel lblDobbeltklikPEn = new JLabel("Dobbelt-klik p\u00E5 en kunde for at scanne dennes kort.");

		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_contentPane = new GroupLayout(m_contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(
				gl_contentPane
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 354, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblDobbeltklikPEn)).addContainerGap(80, Short.MAX_VALUE)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(
				gl_contentPane.createSequentialGroup().addContainerGap().addComponent(lblDobbeltklikPEn)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 234, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		m_list = new JList<Customer>();
		m_list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					Customer customer = m_list.getSelectedValue();
					m_device.scanCard(customer.getCardId());
				}
			}
		});
		m_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		m_list.setModel(new CustomerListModel(manager));

		scrollPane.setViewportView(m_list);
		m_contentPane.setLayout(gl_contentPane);

		setVisible(true);
	}

	private class CustomerListModel extends DefaultListModel<Customer>
	{
		private final SortedSet<Customer> m_customers;

		public CustomerListModel(CustomerManager manager)
		{
			m_customers = manager.getCustomers();
		}

		@Override
		public int getSize()
		{
			return m_customers.size();
		}

		@Override
		public Customer getElementAt(int index)
		{
			return (Customer) m_customers.toArray()[index];
		}

	}

}
