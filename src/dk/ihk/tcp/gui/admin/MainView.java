package dk.ihk.tcp.gui.admin;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dk.ihk.tcp.datamodel.Customer;

public class MainView extends JFrame
{
	public static final String TITLE = "IHK TCP Administrationssystem ";

	private final AdminController m_controller;

	private final JPanel contentPane;
	private final JButton m_btnDelete;
	private final JButton m_btnSave;
	private final JButton m_btnDeposit;
	private final JTextField m_textName;
	private final JTextField m_textCard;
	private final JTextField m_textBalance;
	private final JComboBox<String> m_comboStatus;
	private final JList<Customer> m_list;
	private final JTable m_table;

	/**
	 * Create the frame.
	 * 
	 * @throws Exception
	 */
	public MainView(AdminController controller) throws Exception
	{
		m_controller = controller;

		/**
		 * GUI init stuff
		 */

		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 623, 426);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setTitle(TITLE);

		JButton m_btnNew = new JButton("Ny kunde");
		m_btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				newPressed(arg0);
			}
		});

		m_list = new JList<Customer>();
		m_list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				if (!e.getValueIsAdjusting())
				{
					customerSelected(m_list.getSelectedValue());
				}
			}
		});
		m_list.setBorder(new LineBorder(Color.LIGHT_GRAY));

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Oplysninger", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Rejser", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		m_btnDelete = new JButton("Slet");
		m_btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				deletePressed(e);
			}
		});

		m_btnSave = new JButton("Gem");
		m_btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				savePressed(e);
			}
		});

		m_btnDeposit = new JButton("Indsæt beløb");
		m_btnDeposit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				depositPressed(e);
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(
				gl_contentPane
						.createSequentialGroup()
						.addGroup(
								gl_contentPane
										.createParallelGroup(Alignment.LEADING)
										.addComponent(m_btnNew)
										.addGroup(
												gl_contentPane
														.createSequentialGroup()
														.addComponent(m_list, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE)
														.addGap(16)
														.addGroup(
																gl_contentPane
																		.createParallelGroup(Alignment.TRAILING)
																		.addComponent(panel_1, GroupLayout.DEFAULT_SIZE,
																				GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																		.addGroup(
																				gl_contentPane.createSequentialGroup()
																						.addComponent(m_btnDeposit).addGap(18)
																						.addComponent(m_btnSave)
																						.addPreferredGap(ComponentPlacement.RELATED)
																						.addComponent(m_btnDelete))
																		.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
																				440, Short.MAX_VALUE)))).addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(
						gl_contentPane
								.createSequentialGroup()
								.addGroup(
										gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(m_btnNew)
												.addComponent(m_btnDelete).addComponent(m_btnSave).addComponent(m_btnDeposit))
								.addGroup(
										gl_contentPane
												.createParallelGroup(Alignment.LEADING)
												.addGroup(
														gl_contentPane
																.createSequentialGroup()
																.addGap(8)
																.addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE)
																.addGap(12)
																.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 140,
																		GroupLayout.PREFERRED_SIZE))
												.addGroup(
														gl_contentPane.createSequentialGroup()
																.addPreferredGap(ComponentPlacement.UNRELATED)
																.addComponent(m_list, GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)))
								.addContainerGap()));

		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(scrollPane, Alignment.TRAILING,
				GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE,
				117, Short.MAX_VALUE));

		m_table = new JTable();

		scrollPane.setViewportView(m_table);
		panel_1.setLayout(gl_panel_1);

		m_textName = new JTextField();
		m_textName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0)
			{
				m_controller.markChanges();
			}
		});
		m_textName.setForeground(Color.BLACK);
		m_textName.setBackground(Color.WHITE);
		m_textName.setColumns(10);

		m_textCard = new JTextField();
		m_textCard.setEditable(false);
		m_textCard.setColumns(10);

		m_textBalance = new JTextField();
		m_textBalance.setEditable(false);
		m_textBalance.setColumns(10);

		JLabel lblNavn = new JLabel("Navn:");

		JLabel lblKortnr = new JLabel("Kortnr.:");

		JLabel lblSaldo = new JLabel("Saldo:");

		m_comboStatus = new JComboBox<String>();
		m_comboStatus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				m_controller.markChanges();
			}
		});
		m_comboStatus.setModel(new DefaultComboBoxModel<String>(new String[] { "Aktiv", "Inaktiv" }));

		JLabel lblStatus = new JLabel("Status:");

		JLabel lblBemrkScanEt = new JLabel("Bemærk! Scan et kort for at angive kortnr.");
		lblBemrkScanEt.setFont(new Font("Tahoma", Font.BOLD, 11));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(
				gl_panel.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_panel.createParallelGroup(Alignment.LEADING)
										.addGroup(
												gl_panel.createSequentialGroup()
														.addGroup(
																gl_panel.createParallelGroup(Alignment.TRAILING)
																		.addGroup(
																				gl_panel.createParallelGroup(Alignment.LEADING)
																						.addComponent(lblKortnr).addComponent(lblSaldo)
																						.addComponent(lblNavn))
																		.addComponent(lblStatus, GroupLayout.DEFAULT_SIZE, 39,
																				Short.MAX_VALUE))
														.addPreferredGap(ComponentPlacement.RELATED)
														.addGroup(
																gl_panel.createParallelGroup(Alignment.LEADING)
																		.addComponent(m_textName, GroupLayout.DEFAULT_SIZE, 359,
																				Short.MAX_VALUE)
																		.addComponent(m_textCard, GroupLayout.DEFAULT_SIZE, 359,
																				Short.MAX_VALUE)
																		.addComponent(m_textBalance, GroupLayout.DEFAULT_SIZE, 359,
																				Short.MAX_VALUE)
																		.addComponent(m_comboStatus, 0, 359, Short.MAX_VALUE)))
										.addComponent(lblBemrkScanEt)).addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(
				gl_panel.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(m_textName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE).addComponent(lblNavn))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(
								gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(m_textCard, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE).addComponent(lblKortnr))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(
								gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(m_textBalance, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE).addComponent(lblSaldo))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(
								gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(m_comboStatus, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE).addComponent(lblStatus))
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblBemrkScanEt)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);

		m_btnSave.setEnabled(false);
		m_btnDelete.setEnabled(false);
		m_btnDeposit.setEnabled(false);

		setVisible(true);
	}

	private void customerSelected(Customer c)
	{
		m_controller.customerSelected(c);
		m_btnSave.setEnabled(c != null);
		m_btnDelete.setEnabled(c != null);
		m_btnDeposit.setEnabled(c != null);
	}

	private void newPressed(ActionEvent e)
	{
		m_controller.newPressed(e);
	}

	private void deletePressed(ActionEvent e)
	{
		m_controller.deletePressed(e);
	}

	private void savePressed(ActionEvent e)
	{
		m_controller.savePressed(e);
	}

	public void setCustomerListModel(CustomerListModel model)
	{
		m_list.setModel(model);
	}

	public void setJourneyTableModel(JourneyTableModel model)
	{
		m_table.setModel(model);
		m_table.getColumnModel().getColumn(0).setPreferredWidth(15);
		m_table.getColumnModel().getColumn(1).setPreferredWidth(100);
		m_table.getColumnModel().getColumn(2).setPreferredWidth(15);
		m_table.getColumnModel().getColumn(3).setPreferredWidth(15);
		m_table.getColumnModel().getColumn(4).setPreferredWidth(20);
	}

	public void depositPressed(ActionEvent e)
	{
		m_controller.depositPressed();
	}

	@Override
	public void setName(String n)
	{
		m_textName.setText(n);
	}

	@Override
	public String getName()
	{
		return m_textName.getText();
	}

	public void setBalance(double balance)
	{
		m_textBalance.setText(String.valueOf(balance));
	}

	public double getBalance()
	{
		return Double.valueOf(m_textBalance.getText());
	}

	public void setCard(long card)
	{
		m_textCard.setText(String.valueOf(card));
	}

	public long getCard()
	{
		return Long.valueOf(m_textCard.getText());
	}

	public int getStatus()
	{
		String selected = (String) m_comboStatus.getSelectedItem();
		return "Aktiv".equals(selected) ? Customer.ACTIVE : Customer.INACTIVE;
	}

	public void setStatus(int status)
	{
		String selected = status == Customer.ACTIVE ? "Aktiv" : "Inaktiv";
		m_comboStatus.setSelectedItem(selected);
	}

	public Customer getSelected()
	{
		return m_list.getSelectedValue();
	}

	public void setSelected(Customer c)
	{
		m_list.setSelectedValue(c, true);
	}

	public JTextField getNameText()
	{
		return m_textName;
	}

	public JTextField getCardText()
	{
		return m_textCard;
	}

	public JComboBox<String> getStatusCombo()
	{
		return m_comboStatus;
	}

	public JTextField getBalanceText()
	{
		return m_textBalance;
	}

	public JList<Customer> getCustomerList()
	{
		return m_list;
	}
}
