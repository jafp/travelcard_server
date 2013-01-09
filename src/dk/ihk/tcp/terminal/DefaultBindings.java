package dk.ihk.tcp.terminal;

import dk.ihk.tcp.cardreader.BaseCardReader;
import dk.ihk.tcp.cardreader.CardReader;
import dk.ihk.tcp.cardreader.Device;
import dk.ihk.tcp.cardreader.usb.TCPUSBDevice;
import dk.ihk.tcp.db.CustomerDAO;
import dk.ihk.tcp.db.DAOFactory;
import dk.ihk.tcp.db.JourneyDAO;
import dk.ihk.tcp.db.TrackingPointDAO;
import dk.ihk.tcp.db.mysql.MySQLTrackingPointDAO;
import dk.ihk.tcp.manager.CustomerManager;
import dk.ihk.tcp.manager.JourneyManager;
import dk.ihk.tcp.manager.TerminalManager;
import dk.ihk.tcp.manager.TrackingPointManager;
import dk.ihk.tcp.manager.impl.DefaultCustomerManager;
import dk.ihk.tcp.manager.impl.DefaultJourneyManager;
import dk.ihk.tcp.manager.impl.DefaultTerminalManager;
import dk.ihk.tcp.manager.impl.DefaultTrackingPointManager;
import dk.ihk.tcp.util.inject.Bindings;
import dk.ihk.tcp.zones.MemoryZoneManager;
import dk.ihk.tcp.zones.ZoneManager;

public class DefaultBindings extends Bindings
{
	@Override
	public void configure() throws Exception
	{
		bind(Device.class).to(new TCPUSBDevice());
		bind(CardReader.class).to(BaseCardReader.class);
		bind(ZoneManager.class).to(MemoryZoneManager.class);
		bind(TrackingPointDAO.class).to(new MySQLTrackingPointDAO());
		bind(CustomerDAO.class).to(DAOFactory.getCustomerDAO());
		bind(JourneyDAO.class).to(DAOFactory.getJourneyDAO());
		bind(TrackingPointManager.class).to(DefaultTrackingPointManager.class);
		bind(JourneyManager.class).to(DefaultJourneyManager.class);
		bind(CustomerManager.class).to(DefaultCustomerManager.class);
		bind(TerminalManager.class).to(DefaultTerminalManager.class);
		bind(TerminalDriver.class).to(TerminalDriver.class);
	}
}
