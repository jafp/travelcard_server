package dk.ihk.tcp.zones;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import dk.ihk.tcp.util.inject.Inject;

public class MemoryZoneManager implements ZoneManager
{

	private int m_zone;
	private final ZoneMap m_map;
	private final List<ZoneListener> m_listeners = new ArrayList<ZoneListener>();

	@Inject
	public MemoryZoneManager() throws IOException
	{
		m_zone = 1;
		m_map = new ZoneMap(Paths.get(".", "config", "zonemap.txt"));
	}

	public void setZone(int z)
	{
		int old = m_zone;
		m_zone = z;

		for (ZoneListener listener : m_listeners)
		{
			listener.onChange(m_zone, old);
		}
	}

	@Override
	public int getZone()
	{
		return m_zone;
	}

	@Override
	public int getPrice(int start, int end)
	{
		return ZonePrice.getPrice(getLength(start, end));
	}

	@Override
	public void addListener(ZoneListener listener)
	{
		m_listeners.add(listener);
	}

	@Override
	public void removeListener(ZoneListener listener)
	{
		m_listeners.remove(listener);
	}

	@Override
	public int getLength(int start, int end)
	{
		return m_map.getShortestPath(start, end).size();
	}

	@Override
	public ZoneMap getZoneMap()
	{
		return m_map;
	}

}
