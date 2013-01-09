package dk.ihk.tcp.tracking;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import dk.ihk.tcp.datamodel.TrackingPoint;
import dk.ihk.tcp.manager.CustomerManager;
import dk.ihk.tcp.manager.TrackingPointManager;
import dk.ihk.tcp.util.inject.Inject;

public class TrackingService implements Runnable
{
	private final Thread m_thread;
	private boolean m_running;

	private final CustomerManager m_customerManager;
	private final TrackingPointManager m_manager;
	private final List<TrackingServiceListener> m_listeners;

	@Inject
	public TrackingService(TrackingPointManager manager, CustomerManager customerManager)
	{
		m_manager = manager;
		m_customerManager = customerManager;
		m_thread = new Thread(this);
		m_listeners = new ArrayList<TrackingServiceListener>();
		m_running = false;
	}

	public void start()
	{
		m_running = true;
		m_thread.start();
	}

	public void stop()
	{
		m_running = false;
	}

	public void addListener(TrackingServiceListener listener)
	{
		m_listeners.add(listener);
	}

	public void removeListener(TrackingServiceListener listener)
	{
		m_listeners.remove(listener);
	}

	@Override
	public void run()
	{
		long latest = 0;

		while (m_running)
		{
			try
			{
				SortedSet<TrackingPoint> points = m_manager.getTrackingNewerThan(latest);
				if (points.size() > 0)
				{
					// If not first round
					if (latest > 0)
					{
						for (TrackingPoint p : points)
						{
							p.setCustomer(m_customerManager.getCustomerById(p.getCustomerId()));
						}

						for (TrackingServiceListener listener : m_listeners)
						{
							listener.onNewTrackingPoints(points);
						}
					}

					latest = points.first().getId();
				}

				Thread.sleep(500);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public EventSource newEventSource()
	{
		return new TrackingEventSource(this);
	}
}
