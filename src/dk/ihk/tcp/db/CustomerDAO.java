package dk.ihk.tcp.db;

import java.util.SortedSet;

import dk.ihk.tcp.datamodel.Customer;

/**
 * Data access object for Customer objects.
 * 
 * @author Jacob Pedersen
 */
public interface CustomerDAO
{
	/**
	 * Insert a customer
	 * 
	 * @param c A customer
	 * @throws DBException
	 */
	void insert(Customer c) throws DBException;

	/**
	 * Update a customer
	 * 
	 * @param c A customer
	 * @throws DBException
	 */
	void update(Customer c) throws DBException;

	/**
	 * Delete a customer
	 * 
	 * @param c A customer
	 * @return True on success
	 * @throws DBException
	 */
	boolean delete(Customer c) throws DBException;

	/**
	 * Delete all customers
	 * 
	 * @return True on success
	 * @throws DBException
	 */
	boolean deleteAll() throws DBException;

	/**
	 * Find all customers
	 * 
	 * @return A set of customers
	 * @throws DBException
	 */
	SortedSet<Customer> find() throws DBException;

	/**
	 * Find a customer by ID
	 * 
	 * @param l
	 * @return A customer or null
	 * @throws DBException
	 */
	Customer findById(long l) throws DBException;

	/**
	 * Find a customer by card ID
	 * 
	 * @param cardId
	 * @return A customer or null
	 * @throws DBException
	 */
	Customer findByCard(long cardId) throws DBException;

	/**
	 * Find a customer by name
	 * 
	 * 
	 * @param name
	 * @return A customer or null
	 * @throws DBException
	 */
	Customer findByName(String name) throws DBException;
}
