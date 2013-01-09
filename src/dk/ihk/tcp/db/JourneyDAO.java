package dk.ihk.tcp.db;

import java.util.SortedSet;

import dk.ihk.tcp.datamodel.Journey;

/**
 * Data access object Journey objects
 * 
 * @author Jacob Pedersen
 */
public interface JourneyDAO
{
	/**
	 * Insert a journey
	 * 
	 * @param j A journey
	 * @throws DBException
	 */
	void insert(Journey j) throws DBException;

	/**
	 * Update a journey
	 * 
	 * @param j A journey
	 * @throws DBException
	 */
	void update(Journey j) throws DBException;

	/**
	 * Delete all journeys.
	 * 
	 * @throws DBException
	 */
	void deleteAll() throws DBException;

	/**
	 * Find the last journey by the given customer id.
	 * 
	 * @param customerId A customer id
	 * @return The last journey or null
	 * @throws DBException
	 */
	Journey findLastByCustomer(long customerId) throws DBException;

	/**
	 * Find all journeys by the given customer.
	 * 
	 * @param customerId The customer id
	 * @return A sorted set of all journeys
	 * @throws DBException
	 */
	SortedSet<Journey> findAllByCustomer(long customerId) throws DBException;
}
