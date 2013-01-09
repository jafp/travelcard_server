package dk.ihk.tcp.zones;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class responsible for obtaining the terminals current zone from the zone file
 * and providing it to the services. It also delegates the calculation of
 * journey price.
 */
public class FileZoneManager implements ZoneManager
{

	private static final String ZONE_FILE = "zone.txt";
	private static final String ZONE_MAP_FILE = "zonemap.txt";

	private int m_zone;
	private final Thread m_watcherThread;
	private final ZoneMap m_map;
	private final ZoneFileWatcher m_watcher;
	private final List<ZoneListener> m_listeners = new ArrayList<ZoneListener>();

	public FileZoneManager(Path dir) throws IOException
	{
		m_map = new ZoneMap(Paths.get(".", "config", ZONE_MAP_FILE));
		m_watcher = new ZoneFileWatcher(dir);
		m_watcher.readZone(Paths.get(".", "config", ZONE_FILE));

		m_watcherThread = new Thread(m_watcher);
		m_watcherThread.start();
	}

	@Override
	public int getZone()
	{
		return m_zone;
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
	public int getPrice(int startZone, int endZone)
	{
		return ZonePrice.getPrice(getLength(startZone, endZone));
	}

	private class ZoneFileWatcher implements Runnable
	{

		private final WatchService m_watcher;
		private final Map<WatchKey, Path> m_keys;
		private final boolean m_running;

		public ZoneFileWatcher(Path dir) throws IOException
		{
			m_running = true;
			m_watcher = FileSystems.getDefault().newWatchService();
			m_keys = new HashMap<WatchKey, Path>();

			WatchKey key = dir.register(m_watcher, StandardWatchEventKinds.ENTRY_MODIFY);
			m_keys.put(key, dir);
		}

		public void readZone(Path path)
		{
			BufferedReader reader = null;
			try
			{
				reader = new BufferedReader(new FileReader(path.toFile()));
				String line = reader.readLine();
				if (line != null)
				{
					int zone = Integer.valueOf(line.trim());
					if (m_map.hasZone(zone))
					{
						int oldZone = m_zone;
						m_zone = zone;
						System.out.println("FILEZONEMANAGER: " + m_zone);
						for (ZoneListener listener : m_listeners)
						{
							listener.onChange(m_zone, oldZone);
						}
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				if (reader != null)
				{
					try
					{
						reader.close();
					}
					catch (Exception e)
					{
						// Ignore
					}
				}
			}

		}

		@Override
		public void run()
		{
			while (m_running)
			{

				WatchKey key;
				try
				{
					key = m_watcher.take();
				}
				catch (InterruptedException e)
				{
					return;
				}

				Path dir = m_keys.get(key);
				if (dir == null)
				{
					continue;
				}

				for (WatchEvent<?> event : key.pollEvents())
				{
					@SuppressWarnings("unchecked")
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path name = ev.context();

					if (name.toString().equalsIgnoreCase(ZONE_FILE))
					{
						readZone(Paths.get(".", "config", ZONE_FILE));
					}
				}

				key.reset();

			}
		}

	}

	@Override
	public int getLength(int start, int end)
	{
		System.out.println(start + " " + end);
		return m_map.getShortestPath(start, end).size();
	}

	@Override
	public ZoneMap getZoneMap()
	{
		return m_map;
	}

}
