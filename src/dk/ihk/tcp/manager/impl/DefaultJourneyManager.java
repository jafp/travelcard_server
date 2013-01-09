package dk.ihk.tcp.manager.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.SortedSet;

import dk.ihk.tcp.datamodel.Customer;
import dk.ihk.tcp.datamodel.Journey;
import dk.ihk.tcp.db.CustomerDAO;
import dk.ihk.tcp.db.DBException;
import dk.ihk.tcp.db.DatabaseManager;
import dk.ihk.tcp.db.JourneyDAO;
import dk.ihk.tcp.manager.JourneyManager;
import dk.ihk.tcp.util.inject.Inject;
import dk.ihk.tcp.zones.ZoneManager;

/**
 * Default implementation of the journey manager. This journey manager
 * implements the logic and business rules described in the software
 * requirements specification.
 * 
 * @author Jacob Pedersen
 */
public class DefaultJourneyManager implements JourneyManager
{
	/**
	 * Maximum number of seconds to pass between a check in and a check out
	 * before a journey is considered a null-journey
	 */
	public static final int MAX_NULL_JOURNEY_LENGTH_IN_SECONDS = 5;

	/**
	 * Maximum travel time in minutes
	 */
	public static final int MAX_TRAVEL_TIME_IN_MINUTES = 1;

	/**
	 * Minimum account balance (DKK) to begin a journey.
	 */
	public static final int MIN_BALANCE_TO_BEGIN_JOURNEY = 50;

	/**
	 * A zone manager
	 */
	private final ZoneManager m_zoneManager;

	/**
	 * A customer data access object
	 */
	private final CustomerDAO m_customerDao;

	/**
	 * A journey data access object
	 */
	private final JourneyDAO m_journeyDao;

	/**
	 * Constructor with dependencies
	 */
	@Inject
	public DefaultJourneyManager(ZoneManager zoneManager, CustomerDAO customerDao, JourneyDAO journeyDao)
	{
		m_customerDao = customerDao;
		m_journeyDao = journeyDao;
		m_zoneManager = zoneManager;

	}

	/**
	 * @see dk.ihk.tcp.manager.JourneyManager#getCurrent(dk.ihk.tcp.datamodel.Customer)
	 */
	@Override
	public Journey getCurrent(Customer customer)
	{
		try
		{
			return m_journeyDao.findLastByCustomer(customer.getId());
		}
		catch (DBException e)
		{

		}
		return null;
	}

	/**
	 * @see dk.ihk.tcp.manager.JourneyManager#getJourneys(dk.ihk.tcp.datamodel.Customer)
	 */
	@Override
	public SortedSet<Journey> getJourneys(Customer customer)
	{
		try
		{
			return m_journeyDao.findAllByCustomer(customer.getId());
		}
		catch (DBException e)
		{

		}
		return null;
	}

	/**
	 * @see dk.ihk.tcp.manager.JourneyManager#checkIn(dk.ihk.tcp.datamodel.Customer)
	 */
	@Override
	public Journey checkIn(Customer customer) throws Exception
	{
		Journey journey = new Journey();
		journey.setStatus(Journey.CHECKED_IN);
		journey.setCustomerId(customer.getId());
		journey.setStartZone(m_zoneManager.getZone());
		journey.setStartTime(new Date());

		m_journeyDao.insert(journey);

		return journey;
	}

	/**
	 * @see dk.ihk.tcp.manager.JourneyManager#checkOut(dk.ihk.tcp.datamodel.Customer)
	 */
	@Override
	public Journey checkOut(Customer customer) throws Exception
	{
		Journey journey = null;
		DatabaseManager.beginTransaction();

		try
		{
			journey = getCurrent(customer);

			journey.setEndZone(m_zoneManager.getZone());
			journey.setEndTime(new Date());
			journey.setStatus(Journey.CHECKED_OUT);

			int price = 0;
			if (!isNullJourney(journey))
			{
				price = m_zoneManager.getPrice(journey.getStartZone(), journey.getEndZone());
			}
			journey.setPrice(price);

			m_journeyDao.update(journey);

			customer.setBalance(customer.getBalance() - journey.getPrice());
			m_customerDao.update(customer);

			DatabaseManager.commitTransaction();
		}
		catch (DBException e)
		{
			DatabaseManager.rollbackTransaction();
			throw e;
		}
		finally
		{
			DatabaseManager.endTransaction();
		}

		return journey;
	}

	/**
	 * @see dk.ihk.tcp.manager.JourneyManager#checkOutTooLate(dk.ihk.tcp.datamodel.Customer)
	 */
	@Override
	public Journey checkOutTooLate(Customer customer) throws Exception
	{
		Journey journey = null;
		DatabaseManager.beginTransaction();

		try
		{
			journey = getCurrent(customer);

			journey.setEndZone(0);
			journey.setEndTime(new Date());
			journey.setStatus(Journey.CHECKED_OUT_TOO_LATE);
			journey.setPrice(50);

			m_journeyDao.update(journey);

			customer.setBalance(customer.getBalance() - journey.getPrice());
			m_customerDao.update(customer);

			DatabaseManager.commitTransaction();
		}
		catch (DBException e)
		{
			DatabaseManager.rollbackTransaction();
			throw e;
		}
		finally
		{
			DatabaseManager.endTransaction();
		}
		return journey;
	}

	/**
	 * @see dk.ihk.tcp.manager.JourneyManager#isCheckedIn(dk.ihk.tcp.datamodel.Customer)
	 */
	@Override
	public boolean isCheckedIn(Customer customer)
	{
		Journey journey = getCurrent(customer);
		return journey != null && journey.getStatus() == Journey.CHECKED_IN;
	}

	/**
	 * @see dk.ihk.tcp.manager.JourneyManager#tooLateToCheckOut(dk.ihk.tcp.datamodel.Customer)
	 */
	@Override
	public boolean tooLateToCheckOut(Customer customer)
	{
		Journey journey = getCurrent(customer);
		Calendar cal = Calendar.getInstance();
		cal.setTime(journey.getStartTime());
		// cal.add(Calendar.MINUTE, getMaximumTravelTimeInMinutes());
		cal.add(Calendar.SECOND, 30);
		Date checkOutThreshold = cal.getTime();
		return (new Date()).after(checkOutThreshold);
	}

	/**
	 * @see dk.ihk.tcp.manager.JourneyManager#hasSufficientFundsToCheckIn(dk.ihk.tcp.datamodel.Customer)
	 */
	@Override
	public boolean hasSufficientFundsToCheckIn(Customer customer)
	{
		return customer.getBalance() >= 50;
	}

	/**
	 * @see dk.ihk.tcp.manager.JourneyManager#getMaximumTravelTimeInMinutes()
	 */
	@Override
	public int getMaximumTravelTimeInMinutes()
	{
		return MAX_TRAVEL_TIME_IN_MINUTES;
	}

	/**
	 * @see dk.ihk.tcp.manager.JourneyManager#isNullJourney(dk.ihk.tcp.datamodel.Journey)
	 */
	@Override
	public boolean isNullJourney(Journey journey)
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, -MAX_NULL_JOURNEY_LENGTH_IN_SECONDS);

		return journey.getStartTime().after(cal.getTime());
	}

}
