package dk.ihk.tcp.test;

import dk.ihk.tcp.cardreader.BaseCardReader;
import dk.ihk.tcp.cardreader.CardReader;
import dk.ihk.tcp.cardreader.Device;
import dk.ihk.tcp.cardreader.fake.FakeDevice;
import dk.ihk.tcp.db.CustomerDAO;
import dk.ihk.tcp.db.JourneyDAO;
import dk.ihk.tcp.db.TrackingPointDAO;
import dk.ihk.tcp.manager.JourneyManager;
import dk.ihk.tcp.terminal.DefaultBindings;
import dk.ihk.tcp.zones.MemoryZoneManager;
import dk.ihk.tcp.zones.ZoneManager;

public class TestBindings extends DefaultBindings
{
	@Override
	public void configure() throws Exception
	{
		super.configure();

		bind(Device.class).to(new FakeDevice());
		bind(CardReader.class).to(BaseCardReader.class);
		bind(ZoneManager.class).to(MemoryZoneManager.class);
		bind(CustomerDAO.class).to(MemoryCustomerDAO.class);
		bind(JourneyDAO.class).to(MemoryJourneyDAO.class);
		bind(TrackingPointDAO.class).to(MemoryTrackingPointDAO.class);
		bind(JourneyManager.class).to(TestJourneyManager.class);
	}
}
