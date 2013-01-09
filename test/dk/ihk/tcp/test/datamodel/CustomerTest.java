package dk.ihk.tcp.test.datamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

import dk.ihk.tcp.datamodel.Customer;

public class CustomerTest
{
	@Test
	public void testEquals()
	{
		Customer a = new Customer();
		a.setId(123);
		a.setCardId(987);
		a.setBalance(50);
		a.setName("Joe");

		Customer b = new Customer();
		b.setId(123);
		b.setCardId(987);
		b.setBalance(50);
		b.setName("Joe");

		assertEquals(a, b);

		b.setId(1);
		assertNotSame(a, b);
	}
}
