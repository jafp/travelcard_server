package dk.ihk.tcp.manager;

import java.util.SortedSet;

import dk.ihk.tcp.datamodel.Customer;
import dk.ihk.tcp.datamodel.Journey;

/**
 * Interface for the journey manager. The journey manager is responsible for
 * logic and business rules regarding journeys.
 * 
 * @author Jacob Pedersen
 */
public interface JourneyManager
{
	/**
	 * Finds the current journey by the given customer, is any.
	 * 
	 * @param customer The customer
	 * @return The current journey, or null
	 */
	Journey getCurrent(Customer customer);

	/**
	 * Finds all journeys by the given customer, or an empty set
	 * 
	 * @param customer The customer
	 * @return Sorted set of journeys
	 */
	SortedSet<Journey> getJourneys(Customer customer);

	/**
	 * Check in the given customer by creating and logging a new journey.
	 * 
	 * @param customer The customer to check in
	 * @return The newly started journey
	 * @throws Exception If a new journey could not be started (due to database
	 *             connection problem or problems like that)
	 */
	Journey checkIn(Customer customer) throws Exception;

	/**
	 * Check the given customer out, and end the current journey.
	 * 
	 * @param customer The customer to check out
	 * @return The ended journey
	 * @throws Exception If the check out fails
	 */
	Journey checkOut(Customer customer) throws Exception;

	/**
	 * Check out the customer from the current journey with an end status of
	 * "too late check out".
	 * 
	 * @param customer The customer to check out
	 * @return The ended journey
	 * @throws Exception If the check out fails
	 */
	Journey checkOutTooLate(Customer customer) throws Exception;

	/**
	 * Checks if the given customer is checked in / has a journey that isn't
	 * ended yet.
	 * 
	 * @param customer The customer to check
	 * @return True if the customer is checked in
	 */
	boolean isCheckedIn(Customer customer);

	/**
	 * Checks if it is too long since check in to check out of the customer
	 * current journey.
	 * 
	 * @param customer The customer to check
	 * @return True if it's too late to do an ordinary check out
	 */
	boolean tooLateToCheckOut(Customer customer);

	/**
	 * Checks if the current customer has sufficient funds on its account to do
	 * a check in.
	 * 
	 * @param customer The customer to check
	 * @return True if the customer has sufficient funds
	 */
	boolean hasSufficientFundsToCheckIn(Customer customer);

	/**
	 * @return The maximum length of a journey in minutes.
	 */
	int getMaximumTravelTimeInMinutes();

	/**
	 * Decides whether the journey is a null-journey at the moment.
	 * 
	 * @param journey The journey to check
	 * @return True if the journey is a null-journey
	 */
	boolean isNullJourney(Journey journey);

}
