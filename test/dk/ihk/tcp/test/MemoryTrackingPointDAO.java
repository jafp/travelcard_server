package dk.ihk.tcp.test;

import java.util.SortedSet;
import java.util.TreeSet;

import dk.ihk.tcp.datamodel.TrackingPoint;
import dk.ihk.tcp.db.DBException;
import dk.ihk.tcp.db.TrackingPointDAO;
import dk.ihk.tcp.util.DateHelper;
import dk.ihk.tcp.util.inject.Inject;

public class MemoryTrackingPointDAO implements TrackingPointDAO
{
	private final MemoryDAO<TrackingPoint> m_dao = new MemoryDAO<TrackingPoint>();

	@Inject
	public MemoryTrackingPointDAO()
	{

	}

	@Override
	public void insertOrUpdate(TrackingPoint p) throws DBException
	{
		p.setCreatedAt(DateHelper.getNow());
		m_dao.insertOrUpdate(p);
	}

	@Override
	public SortedSet<TrackingPoint> findNewerThan(long latest) throws DBException
	{
		SortedSet<TrackingPoint> set = new TreeSet<TrackingPoint>();
		for (TrackingPoint p : m_dao.findAll())
		{
			if (p.getId() > latest)
			{
				set.add(p);
			}
		}
		return set;
	}

}
