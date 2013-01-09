package dk.ihk.tcp.db.mysql;

import java.util.SortedSet;

import dk.ihk.tcp.datamodel.TrackingPoint;
import dk.ihk.tcp.db.DBException;
import dk.ihk.tcp.db.TrackingPointDAO;
import dk.ihk.tcp.util.DateHelper;
import dk.ihk.tcp.util.db.AnnotationRowMapper;
import dk.ihk.tcp.util.db.JDBCHelper;

public class MySQLTrackingPointDAO implements TrackingPointDAO
{
	@Override
	public void insertOrUpdate(TrackingPoint p) throws DBException
	{
		p.setCreatedAt(DateHelper.getNow());
		p.setId(JDBCHelper.insert("INSERT INTO tracking_points (customer_id, zone, tracking_type, created_at) VALUES (?, ?, ?, ?)",
				p.getCustomerId(), p.getZone(), p.getType(), p.getCreatedAt()));
	}

	@Override
	public SortedSet<TrackingPoint> findNewerThan(long latest) throws DBException
	{
		return JDBCHelper.query("SELECT * FROM tracking_points WHERE id > ?", new AnnotationRowMapper<TrackingPoint>(TrackingPoint.class),
				latest);
	}
}
