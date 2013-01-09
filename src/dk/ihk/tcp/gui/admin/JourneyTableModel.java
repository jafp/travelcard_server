package dk.ihk.tcp.gui.admin;

import java.util.SortedSet;

import javax.swing.table.AbstractTableModel;

import dk.ihk.tcp.datamodel.Customer;
import dk.ihk.tcp.datamodel.Journey;
import dk.ihk.tcp.manager.JourneyManager;
import dk.ihk.tcp.util.inject.Inject;

public class JourneyTableModel extends AbstractTableModel
{
	private static final String[] COLUMN_NAMES = new String[] { "ID", "Dato", "Start", "Slut", "Pris", "Status" };

	private final JourneyManager m_manager;
	private SortedSet<Journey> m_cache;

	@Inject
	public JourneyTableModel(JourneyManager manager)
	{
		m_manager = manager;
	}

	@Override
	public String getColumnName(int column)
	{
		return COLUMN_NAMES[column];
	}

	@Override
	public int getColumnCount()
	{
		return COLUMN_NAMES.length;
	}

	@Override
	public int getRowCount()
	{
		return m_cache != null ? m_cache.size() : 0;
	}

	@Override
	public Object getValueAt(int row, int col)
	{
		Journey j = (Journey) m_cache.toArray()[row];
		if (col == 0)
		{
			return j.getId();
		}
		else if (col == 1)
		{
			return j.getStartTime();
		}
		else if (col == 2)
		{
			return j.getStartZone();
		}
		else if (col == 3)
		{
			return j.getEndZone();
		}
		else if (col == 4)
		{
			return "kr. " + j.getPrice();
		}
		else if (col == 5)
		{
			int status = j.getStatus();
			if (status == Journey.CHECKED_IN)
			{
				return "Tjekket ind";
			}
			else if (status == Journey.CHECKED_OUT)
			{
				return "Tjekket ud";
			}
			else if (status == Journey.CHECKED_OUT_TOO_LATE)
			{
				return "For sent";
			}
			else
			{
				return "Ukendt";
			}
		}
		return null;
	}

	public void setCustomer(Customer c)
	{
		if (c != null)
		{
			m_cache = m_manager.getJourneys(c);
		}
		else
		{
			m_cache.clear();
		}
		fireTableDataChanged();
	}

}
