package dk.ihk.tcp.manager.impl;

import java.util.logging.Logger;

import dk.ihk.tcp.cardreader.Reading;
import dk.ihk.tcp.cardreader.Response;
import dk.ihk.tcp.cardreader.Response.Type;
import dk.ihk.tcp.datamodel.Customer;
import dk.ihk.tcp.datamodel.Journey;
import dk.ihk.tcp.datamodel.TrackingPoint;
import dk.ihk.tcp.manager.CustomerManager;
import dk.ihk.tcp.manager.JourneyManager;
import dk.ihk.tcp.manager.TerminalManager;
import dk.ihk.tcp.manager.TrackingPointManager;
import dk.ihk.tcp.util.inject.Inject;

public class DefaultTerminalManager implements TerminalManager
{
	/**
	 * A customer manager implementation
	 */
	private final CustomerManager m_customerMgr;

	/**
	 * A journey manager implementation
	 */
	private final JourneyManager m_journeyMgr;
	
	private final TrackingPointManager m_tpMgr;

	/**
	 * The global logger
	 */
	private final Logger m_logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	@Inject
	public DefaultTerminalManager(CustomerManager customerMgr, JourneyManager journeyMgr, TrackingPointManager tpManager)
	{
		m_customerMgr = customerMgr;
		m_journeyMgr = journeyMgr;
		m_tpMgr = tpManager;
	}

	@Override
	public void handleReading(Reading reading) throws Exception
	{
		Customer customer = m_customerMgr.getCustomerByCard(reading.getCardId());
		Response response;

		if (m_customerMgr.isValid(customer))
		{
			if (m_journeyMgr.hasSufficientFundsToCheckIn(customer))
			{
				if (m_journeyMgr.isCheckedIn(customer))
				{
					if (m_journeyMgr.tooLateToCheckOut(customer))
					{
						Journey journey = m_journeyMgr.checkOutTooLate(customer);
						reading.respondWith(Response.create(Type.TOO_LATE_CHECK_OUT));

						m_logger.info(customer + ": Too late check out. " + journey.getStartZone() + " --> " + journey.getEndZone()
								+ ". Price: kr. " + journey.getPrice());
						
						m_tpMgr.addTrackingPoint(customer.getId(), journey.getEndZone(), TrackingPoint.CHECKED_OUT);
					}
					else
					{
						Journey journey = m_journeyMgr.checkOut(customer);

						response = Response.create(Type.CHECKED_OUT);
						response.setShort(1, (int) customer.getBalance());
						response.setShort(3, journey.getPrice());
						reading.respondWith(response);

						m_logger.info(customer + ": Check out. " + journey.getStartZone() + " --> " + journey.getEndZone()
								+ ". Price: kr. " + journey.getPrice());
						
						m_tpMgr.addTrackingPoint(customer.getId(), journey.getEndZone(), TrackingPoint.CHECKED_OUT);
					}
				}
				else
				{
					Journey journey = m_journeyMgr.checkIn(customer);
					response = Response.create(Type.CHECKED_IN);
					response.setShort(1, (short) customer.getBalance());
					reading.respondWith(response);

					m_logger.info(customer + ": Check in. " + journey.getStartZone());
					
					m_tpMgr.addTrackingPoint(customer.getId(), journey.getStartZone(), TrackingPoint.CHECKED_IN);
				}
			}
			else
			{
				response = Response.create(Type.INSUFFICIENT_FUNDS);
				response.setShort(1, (short) customer.getBalance());
				reading.respondWith(response);

				m_logger.info(customer + ": Insufficient funds");
			}
		}
		else
		{
			response = Response.create(Type.INVALID_CARD);
			reading.respondWith(response);
		}

		if (!reading.isResponseSent())
		{
			reading.respondWith(Response.create(Type.ERROR));
		}
	}
}
