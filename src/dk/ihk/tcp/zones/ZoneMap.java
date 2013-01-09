package dk.ihk.tcp.zones;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ZoneMap
{

	private final List<Integer> m_zones = new ArrayList<Integer>();
	private final List<Link> m_links = new ArrayList<Link>();

	/**
	 * Initializes this zone map with data from the given file
	 * 
	 * @param file The zone map file
	 * @throws IOException
	 */
	public ZoneMap(Path file) throws IOException
	{
		parseAndLoad(file.toFile());
	}

	/**
	 * @param zone The zone to check
	 * @return True if the zone is valid
	 */
	public boolean hasZone(Integer zone)
	{
		return m_zones.contains(zone);
	}

	public List<Integer> getZones()
	{
		return m_zones;
	}

	/**
	 * Calculates and finds the shortest path using Dijkstra's algorithm.
	 * 
	 * @param source The start zone
	 * @param destination The end zone
	 * @return The shortest path of zones
	 */
	public List<Integer> getShortestPath(int source, int destination)
	{
		int u;

		// Return null if one of the zones are unknown
		if (!hasZone(source) || !hasZone(destination))
		{
			return null;
		}

		Map<Integer, Integer> dist = new HashMap<Integer, Integer>();
		Map<Integer, Integer> prev = new HashMap<Integer, Integer>();
		List<Integer> q = new ArrayList<Integer>(m_zones);

		for (int z : m_zones)
		{
			dist.put(z, 99);
		}

		u = source;
		dist.put(source, 0);

		while (!q.isEmpty())
		{

			int smallest = 99;
			for (Map.Entry<Integer, Integer> e : dist.entrySet())
			{
				if (q.contains(e.getKey()))
				{
					if (e.getValue() <= smallest)
					{
						smallest = e.getValue();
						u = e.getKey();
					}
				}
			}

			Iterator<Integer> it = q.iterator();
			while (it.hasNext())
			{
				int n = it.next();
				if (n == u)
				{
					it.remove();
				}
			}

			List<Integer> adjecent = getAdjacent(u);

			for (int v : adjecent)
			{
				if (q.contains(v))
				{
					int alt = dist.get(u) + 1;
					if (alt < dist.get(v))
					{
						dist.put(v, alt);
						prev.put(v, u);
					}
				}
			}
		}

		List<Integer> path = new ArrayList<Integer>();
		u = destination;
		while (prev.containsKey(u))
		{
			path.add(u);
			u = prev.get(u);
		}
		path.add(source);

		return path;
	}

	private List<Integer> getAdjacent(int zone)
	{
		List<Integer> adjacent = new ArrayList<Integer>();
		for (Link l : m_links)
		{
			if (l.a == zone || l.b == zone)
			{
				adjacent.add(l.a == zone ? l.b : l.a);
			}
		}
		return adjacent;
	}

	private void parseAndLoad(File file) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));

		String line;
		while ((line = reader.readLine()) != null)
		{
			String[] parts = line.split(":");
			int zone = Integer.valueOf(parts[0].trim());

			if (parts.length == 2)
			{
				String[] lnks = parts[1].split(",");
				for (String l : lnks)
				{
					int v = Integer.valueOf(l.trim());
					m_links.add(new Link(zone, v));
				}
			}

			m_zones.add(zone);
		}

		reader.close();
	}

	private class Link
	{
		private final int a, b;

		public Link(int _a, int _b)
		{
			a = _a;
			b = _b;
		}
	}

}
