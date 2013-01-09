package dk.ihk.tcp.gui.selfservice;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

public class LoginView extends JFrame
{

	private final JPanel contentPane;
	private final JLabel m_welcomeLabel;

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
					LoginView frame = new LoginView();
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
	 */
	public LoginView()
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

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		m_welcomeLabel = new JLabel("Velkommen - scan dit kort for at logge ind");
		m_welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		m_welcomeLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));

		JLabel lblIhkTcp = new JLabel("IHK TCP");
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING).addGroup(
				gl_contentPane
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addComponent(m_welcomeLabel, GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
										.addComponent(lblIhkTcp)).addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(
				Alignment.TRAILING,
				gl_contentPane.createSequentialGroup().addGap(55).addComponent(m_welcomeLabel)
						.addPreferredGap(ComponentPlacement.RELATED, 171, Short.MAX_VALUE).addComponent(lblIhkTcp).addContainerGap()));
		contentPane.setLayout(gl_contentPane);
	}

	public JLabel getWelcomeLabel()
	{
		return m_welcomeLabel;
	}
}
