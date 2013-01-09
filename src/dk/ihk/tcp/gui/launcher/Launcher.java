package dk.ihk.tcp.gui.launcher;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.ComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataListener;

import com.mysql.jdbc.exceptions.jdbc4.CommunicationsException;

import dk.ihk.tcp.cardreader.Device;
import dk.ihk.tcp.cardreader.fake.FakeDevice;
import dk.ihk.tcp.manager.CustomerManager;
import dk.ihk.tcp.terminal.Runner;
import dk.ihk.tcp.terminal.TerminalDriver.Mode;
import dk.ihk.tcp.terminal.TerminalDriver.State;
import dk.ihk.tcp.terminal.TerminalDriverListener;
import dk.ihk.tcp.zones.MemoryZoneManager;
import dk.ihk.tcp.zones.ZoneManager;

public class Launcher extends JFrame implements TerminalDriverListener
{
	private Runner m_runner;
	private final JPanel contentPane;
	private final JTextField m_terminalId;
	private final JTextField m_state;
	private final JButton m_btnStart;
	private final JButton m_btnStop;
	private final JScrollPane scrollPane;
	private final JTextArea m_textArea;
	private final JComboBox<Integer> m_zone;
	private final ZoneComboBoxModel m_zoneModel;

	private final Logger m_logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				try
				{
					Launcher frame = new Launcher();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws Exception
	 */
	public Launcher() throws Exception
	{
		m_logger.addHandler(new Handler() {

			@Override
			public void publish(LogRecord record)
			{
				m_textArea.setText(m_textArea.getText() + "\n\r" + record.getMessage());
				m_textArea.selectAll();
			}

			@Override
			public void flush()
			{
			}

			@Override
			public void close() throws SecurityException
			{
			}
		});

		m_runner = null;

		/**
		 * GUI init stuff
		 */
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 551, 361);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setTitle("IHK TCP Launcher");

		JLabel lblTerminalId = new JLabel("Terminal ID:");

		m_terminalId = new JTextField();
		m_terminalId.setText("1");
		m_terminalId.setColumns(10);

		m_btnStart = new JButton("Start");
		m_btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				startPressed();
			}

		});
		m_btnStart.setEnabled(true);

		m_btnStop = new JButton("Stop");
		m_btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				stopPressed();
			}

		});
		m_btnStop.setEnabled(false);

		m_state = new JTextField();
		m_state.setEditable(false);
		m_state.setColumns(10);

		scrollPane = new JScrollPane();

		m_zone = new JComboBox<Integer>();
		m_zoneModel = new ZoneComboBoxModel();
		m_zone.setModel(m_zoneModel);
		m_zone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
			}
		});

		JLabel lblZone = new JLabel("Zone:");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(
						gl_contentPane
								.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										gl_contentPane
												.createParallelGroup(Alignment.LEADING)
												.addGroup(
														gl_contentPane.createSequentialGroup()
																.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
																.addContainerGap())
												.addGroup(
														gl_contentPane
																.createSequentialGroup()
																.addGroup(
																		gl_contentPane.createParallelGroup(Alignment.LEADING)
																				.addComponent(lblTerminalId).addComponent(lblZone))
																.addPreferredGap(ComponentPlacement.RELATED)
																.addGroup(
																		gl_contentPane
																				.createParallelGroup(Alignment.TRAILING, false)
																				.addComponent(m_zone, Alignment.LEADING, 0,
																						GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																				.addComponent(m_terminalId, Alignment.LEADING)).addGap(18)
																.addComponent(m_btnStart).addPreferredGap(ComponentPlacement.RELATED)
																.addComponent(m_btnStop).addGap(26)
																.addComponent(m_state, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
																.addGap(18)))));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(
				gl_contentPane
						.createSequentialGroup()
						.addGroup(
								gl_contentPane
										.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblTerminalId)
										.addComponent(m_terminalId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(m_btnStart)
										.addComponent(m_btnStop)
										.addComponent(m_state, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(
								gl_contentPane
										.createParallelGroup(Alignment.BASELINE)
										.addComponent(m_zone, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE).addComponent(lblZone)).addGap(18)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE).addContainerGap()));

		m_textArea = new JTextArea();
		m_textArea.setEditable(false);
		scrollPane.setViewportView(m_textArea);
		contentPane.setLayout(gl_contentPane);
	}

	private void startPressed()
	{
		try
		{
			String sn = m_terminalId.getText();
			if (!"".equals(sn))
			{
				startRunnerWithSerial(sn);

				m_terminalId.setEditable(false);
				m_btnStart.setEnabled(false);
				m_btnStop.setEnabled(true);
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Angiv venligst et terminal-nummer");
			}
		}
		catch (Exception e)
		{

			if (e.getClass() == CommunicationsException.class)
			{
				JOptionPane.showMessageDialog(this, "Der kunne ikke forbindes til databasen.");
			}
			else
			{
				m_logger.info(e.getMessage());
			}
		}
	}

	private void stopPressed()
	{
		m_runner.getDriver().stop();

		m_terminalId.setEditable(true);
		m_btnStart.setEnabled(true);
		m_btnStop.setEnabled(false);

		m_state.setText("Stoppet");
		m_state.setBackground(Color.lightGray);
	}

	@Override
	public void onStateChange(State newState)
	{
		m_state.setText(newState == State.CONNECTED ? "Tilsluttet" : "Afbrudt");
		m_state.setBackground(newState == State.CONNECTED ? Color.green : Color.red);
	}

	@Override
	public void onModeChange(Mode newMode)
	{

	}

	@Override
	public void onCardScanned(long id)
	{

	}

	private void startRunnerWithSerial(String serial) throws Exception
	{
		endCurrentRunner();

		if (serial.toLowerCase().startsWith("fake"))
		{
			m_runner = new Runner(new FakeTerminalBindings());
			new FakeTerminalSelector((FakeDevice) m_runner.getInjector().get(Device.class), m_runner.getInjector().get(
					CustomerManager.class));
		}
		else
		{
			m_runner = new Runner(null);
		}

		m_runner.getDriver().addListener(this);
		m_runner.getDriver().setSerialNumber(serial);
		m_zoneModel.setZoneManager((MemoryZoneManager) m_runner.getInjector().get(ZoneManager.class));
		m_zone.updateUI();
		m_runner.getDriver().start();
	}

	private void endCurrentRunner()
	{
		if (m_runner != null)
		{
			m_runner.getDriver().removeListener(this);
			m_runner.getDriver().stop();
			m_runner = null;
		}
	}

	private class ZoneComboBoxModel implements ComboBoxModel<Integer>
	{
		private MemoryZoneManager m_zoneManager;

		public void setZoneManager(MemoryZoneManager mgr)
		{
			m_zoneManager = mgr;
		}

		@Override
		public int getSize()
		{
			return m_zoneManager != null ? m_zoneManager.getZoneMap().getZones().size() : 0;
		}

		@Override
		public Integer getElementAt(int index)
		{
			return m_zoneManager != null ? m_zoneManager.getZoneMap().getZones().get(index) : 0;
		}

		@Override
		public void addListDataListener(ListDataListener l)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void removeListDataListener(ListDataListener l)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void setSelectedItem(Object zone)
		{
			if (m_zoneManager != null)
			{
				m_zoneManager.setZone((Integer) zone);
			}
		}

		@Override
		public Object getSelectedItem()
		{
			return m_zoneManager != null ? m_zoneManager.getZone() : 0;
		}

	}
}
