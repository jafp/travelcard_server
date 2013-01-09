package dk.ihk.tcp.test.zones;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dk.ihk.tcp.zones.ZonePrice;

public class ZonePriceTest
{
	@Test
	public void testGetPrice()
	{
		assertEquals(0, ZonePrice.getPrice(0));
		assertEquals(14, ZonePrice.getPrice(1));
		assertEquals(14, ZonePrice.getPrice(2));
		assertEquals(47, ZonePrice.getPrice(9));
	}
}
