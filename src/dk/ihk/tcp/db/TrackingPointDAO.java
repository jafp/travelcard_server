package dk.ihk.tcp.db;

import java.util.SortedSet;

import dk.ihk.tcp.datamodel.TrackingPoint;

public interface TrackingPointDAO
{
	void insertOrUpdate(TrackingPoint p) throws DBException;

	SortedSet<TrackingPoint> findNewerThan(long latest) throws DBException;
}
