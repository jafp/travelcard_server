package dk.ihk.tcp.test.datamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Date;

import org.junit.Test;

import dk.ihk.tcp.datamodel.Journey;

public class JourneyTest
{
	@Test
	public void testEquals()
	{
		Date d = new Date();

		Journey j = new Journey();
		j.setId(123);
		j.setCustomerId(1);
		j.setStartTime(d);
		j.setStartZone(3);
		j.setEndTime(d);
		j.setEndZone(30);
		j.setPrice(20);
		j.setStatus(1);
		j.setCreatedAt(d);
		j.setUpdatedAt(d);

		Journey j2 = new Journey();
		j2.setId(123);
		j2.setCustomerId(1);
		j2.setStartTime(d);
		j2.setStartZone(3);
		j2.setEndTime(d);
		j2.setEndZone(30);
		j2.setPrice(20);
		j2.setStatus(1);
		j2.setCreatedAt(d);
		j2.setUpdatedAt(d);

		assertEquals(j, j2);
		assertFalse(j.equals(new Journey()));
	}
}
