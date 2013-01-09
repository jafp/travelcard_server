package dk.ihk.tcp.test.zones;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.BeforeClass;
import org.junit.Test;

import dk.ihk.tcp.zones.ZoneMap;

public class ZoneMapTest
{
	private static ZoneMap m_map;

	@BeforeClass
	public static void setup() throws IOException
	{
		m_map = new ZoneMap(Paths.get(".", "config", "zonemap.txt"));
	}

	@Test
	public void testHasZone()
	{
		assertTrue(m_map.hasZone(1));
		assertFalse(m_map.hasZone(0));
	}

	@Test
	public void testGetShortestPath()
	{
		assertEquals(7, m_map.getShortestPath(4, 53).size());
		assertEquals(1, m_map.getShortestPath(1, 1).size());
		assertNull(m_map.getShortestPath(0, 99));
	}
}
