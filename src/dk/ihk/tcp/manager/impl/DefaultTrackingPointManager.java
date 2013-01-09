package dk.ihk.tcp.manager.impl;

import java.util.SortedSet;

import dk.ihk.tcp.datamodel.TrackingPoint;
import dk.ihk.tcp.db.TrackingPointDAO;
import dk.ihk.tcp.manager.TrackingPointManager;
import dk.ihk.tcp.util.inject.Inject;

public class DefaultTrackingPointManager implements TrackingPointManager
{

	private final TrackingPointDAO m_dao;

	@Inject
	public DefaultTrackingPointManager(TrackingPointDAO dao)
	{
		m_dao = dao;
	}

	@Override
	public void addTrackingPoint(long customerId, int zone, int type) throws Exception
	{
		m_dao.insertOrUpdate(new TrackingPoint(customerId, zone, type));
	}

	@Override
	public SortedSet<TrackingPoint> getTrackingNewerThan(long latest) throws Exception
	{
		return m_dao.findNewerThan(latest);
	}

}
