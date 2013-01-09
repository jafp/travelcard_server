package dk.ihk.tcp.tracking;

import java.io.IOException;
import java.util.SortedSet;

import dk.ihk.tcp.datamodel.TrackingPoint;

public class TrackingEventSource implements EventSource, TrackingServiceListener
{
	private Emitter m_emitter;
	private final TrackingService m_service;

	public TrackingEventSource(TrackingService service)
	{
		m_service = service;
	}

	@Override
	public void onOpen(Emitter emitter) throws IOException
	{
		m_emitter = emitter;
		m_service.addListener(this);
	}

	@Override
	public void onClose()
	{
		m_service.removeListener(this);
	}

	@Override
	public void onNewTrackingPoints(SortedSet<TrackingPoint> points)
	{
		StringBuilder builder = new StringBuilder("[");

		for (TrackingPoint p : points)
		{
			builder.append(trackingPointToJSON(p));
			if (p != points.last())
			{
				builder.append(",");
			}
		}
		builder.append("]");

		try
		{
			m_emitter.event("events", builder.toString());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private String trackingPointToJSON(TrackingPoint p)
	{
		return "{ \"id\": " + p.getId() + ", \"customer_id\": " + p.getCustomerId() + ", \"zone\": " + p.getZone() + ", \"type\": "
				+ p.getType() + ", \"date\": \"" + p.getCreatedAt().toString() + "\", \"customer\": " + p.getCustomer().toJSON() + " }";
	}

}
