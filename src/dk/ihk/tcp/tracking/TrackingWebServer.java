package dk.ihk.tcp.tracking;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import dk.ihk.tcp.db.DatabaseManager;
import dk.ihk.tcp.terminal.DefaultBindings;
import dk.ihk.tcp.util.inject.Injector;

public class TrackingWebServer extends Server
{
	public static void main(String... args) throws Exception
	{
		TrackingWebServer server = new TrackingWebServer();
		server.start();
	}

	private final Injector m_injector;
	private final TrackingService m_service;
	private final WebAppContext m_context;

	public TrackingWebServer() throws Exception
	{
		super(27015);

		DatabaseManager.init("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/tcp?user=root&password=root", "root", "root");

		m_injector = new Injector(new DefaultBindings());
		m_injector.bind(TrackingService.class).to(TrackingService.class);

		m_context = new WebAppContext();
		m_context.setDescriptor("tracking/web/WEB-INF/web.xml");
		m_context.setResourceBase("tracking/web");
		m_context.setContextPath("/");
		m_context.setParentLoaderPriority(true);

		// Set the instance of the tracking service on the context
		// so it is accessible from all requests
		m_service = m_injector.get(TrackingService.class);
		m_context.setAttribute("trackingService", m_service);

		// Start the polling service
		m_service.start();

		setHandler(m_context);

	}
}
