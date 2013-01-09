package dk.ihk.tcp.manager;

import java.util.SortedSet;

import dk.ihk.tcp.datamodel.TrackingPoint;

public interface TrackingPointManager
{
	void addTrackingPoint(long customerId, int zone, int type) throws Exception;

	SortedSet<TrackingPoint> getTrackingNewerThan(long latest) throws Exception;
}
