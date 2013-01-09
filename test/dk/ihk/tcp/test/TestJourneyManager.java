package dk.ihk.tcp.test;

import dk.ihk.tcp.datamodel.Journey;
import dk.ihk.tcp.db.CustomerDAO;
import dk.ihk.tcp.db.JourneyDAO;
import dk.ihk.tcp.manager.impl.DefaultJourneyManager;
import dk.ihk.tcp.util.inject.Inject;
import dk.ihk.tcp.zones.ZoneManager;

public class TestJourneyManager extends DefaultJourneyManager
{
	private int m_maxTravelTime;

	@Inject
	public TestJourneyManager(ZoneManager zoneManager, CustomerDAO customerDao, JourneyDAO journeyDao)
	{
		super(zoneManager, customerDao, journeyDao);
		m_maxTravelTime = super.getMaximumTravelTimeInMinutes();
	}

	public void setMaximumTravelTimeInMinutes(int minutes)
	{
		m_maxTravelTime = minutes;
	}

	@Override
	public int getMaximumTravelTimeInMinutes()
	{
		return m_maxTravelTime;
	}

	@Override
	public boolean isNullJourney(Journey journey)
	{
		return false;
	}

}
