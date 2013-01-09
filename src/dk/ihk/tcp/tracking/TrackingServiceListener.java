package dk.ihk.tcp.tracking;

import java.util.SortedSet;

import dk.ihk.tcp.datamodel.TrackingPoint;

public interface TrackingServiceListener
{
	void onNewTrackingPoints(SortedSet<TrackingPoint> points);
}
