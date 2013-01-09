package dk.ihk.tcp.gui.selfservice;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import dk.ihk.tcp.gui.admin.JourneyTableModel;

public class MainView extends JFrame
{
	private final SelfServiceController m_controller;
	private final JPanel m_contentPane;
	private final JTextField m_amountTextField;
	private final JTable m_journeyTable;
	private final JLabel m_welcomeLabel;
	private final JButton m_depositButton;
	private final JScrollPane m_scrollPane;

	/**
	 * Create the frame.
	 */
	public MainView(SelfServiceController controller)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnsupportedLookAndFeelException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		m_controller = controller;

		setBounds(100, 100, 449, 300);
		m_contentPane = new JPanel();
		m_contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(m_contentPane);

		m_welcomeLabel = new JLabel("Velkommen NAVN");
		m_welcomeLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));

		JSeparator separator = new JSeparator();

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Inds\u00E6t penge", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Rejser", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_contentPane = new GroupLayout(m_contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(
				gl_contentPane
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_contentPane
										.createParallelGroup(Alignment.LEADING)
										.addComponent(m_welcomeLabel)
										.addGroup(
												gl_contentPane
														.createSequentialGroup()
														.addGroup(
																gl_contentPane
																		.createParallelGroup(Alignment.TRAILING, false)
																		.addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
																				GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																		.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
																				426, Short.MAX_VALUE))
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(separator, GroupLayout.PREFERRED_SIZE, 430,
																GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(
				gl_contentPane
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(m_welcomeLabel)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(
								gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(separator, GroupLayout.PREFERRED_SIZE, 4, GroupLayout.PREFERRED_SIZE)
										.addComponent(panel, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE)));

		m_scrollPane = new JScrollPane();
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(
				gl_panel_1.createSequentialGroup().addContainerGap()
						.addComponent(m_scrollPane, GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE).addContainerGap()));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(
				gl_panel_1.createSequentialGroup().addComponent(m_scrollPane, GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
						.addContainerGap()));

		m_journeyTable = new JTable();
		m_scrollPane.setViewportView(m_journeyTable);
		panel_1.setLayout(gl_panel_1);

		JLabel lblIndtastBelb = new JLabel("Indtast bel\u00F8b:");

		m_amountTextField = new JTextField();
		m_amountTextField.setColumns(10);

		m_depositButton = new JButton("Inds\u00E6t");
		m_depositButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				depositPressed();
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(
				gl_panel.createSequentialGroup().addContainerGap().addComponent(lblIndtastBelb).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(m_amountTextField, GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(m_depositButton).addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(
				gl_panel.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblIndtastBelb)
										.addComponent(m_amountTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE).addComponent(m_depositButton))
						.addContainerGap(19, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);
		m_contentPane.setLayout(gl_contentPane);
	}

	private void depositPressed()
	{
		m_controller.depositPressed(m_amountTextField.getText());
	}

	public JTextField getAmonutTextField()
	{
		return m_amountTextField;
	}

	public JLabel getWelcomeLabel()
	{
		return m_welcomeLabel;
	}

	public void setJourneyTableModel(JourneyTableModel model)
	{
		m_journeyTable.setModel(model);
		m_journeyTable.getColumnModel().getColumn(0).setPreferredWidth(15);
		m_journeyTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		m_journeyTable.getColumnModel().getColumn(2).setPreferredWidth(15);
		m_journeyTable.getColumnModel().getColumn(3).setPreferredWidth(15);
		m_journeyTable.getColumnModel().getColumn(4).setPreferredWidth(20);
	}
}
