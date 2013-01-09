package dk.ihk.tcp.tracking;

import javax.servlet.http.HttpServletRequest;

public class TrackingEventSourceServlet extends EventSourceServlet
{
	@Override
	protected EventSource newEventSource(HttpServletRequest request)
	{
		TrackingService service = (TrackingService) request.getServletContext().getAttribute("trackingService");
		return service.newEventSource();
	}
}
