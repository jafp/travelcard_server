package dk.ihk.tcp.manager;

import java.util.SortedSet;

import dk.ihk.tcp.datamodel.Customer;

/**
 * Interface for the customer manager. The customer manager should implement
 * logic and business rules regarding the customers, their accounts and so on.
 * 
 * How and where the actual implementation stores the customer is dependent on
 * the implementation. In this project all database access is based on Data
 * Access Objects that talks to a database (MySQL in our case).
 * 
 * @author Jacob Pedersen
 * 
 */
public interface CustomerManager
{
	/**
	 * Updates or inserts the given customer in the underlying database.
	 * 
	 * @param customer The customer to insert or update
	 * @throws Exception If insert or update fails
	 */
	void updateOrInsertCustomer(Customer customer) throws Exception;

	/**
	 * Deletes the given customer from the underlying database.
	 * 
	 * @param customer The customer to delete
	 * @throws Exception If delete fails
	 */
	void deleteCustomer(Customer customer) throws Exception;

	/**
	 * Finds the customer that has a travel card with the given ID. If no such
	 * customer could be found, the method should return null.
	 * 
	 * @param cardId The travel card ID to look for
	 * @return The customer, or null
	 */
	Customer getCustomerByCard(long cardId);

	/**
	 * Finds all customers in the underlying database
	 * 
	 * @return A sorted set of customers or an empty set if no customers exists
	 */
	SortedSet<Customer> getCustomers();

	/**
	 * Determinate whether a customer is valid.
	 * 
	 * @param customer The customer to check
	 * @return True if customer is valid
	 */
	boolean isValid(Customer customer);

	/**
	 * Finds a customer with the given ID
	 * 
	 * @param customerId The customer ID
	 * @return The customer or null
	 */
	Customer getCustomerById(long customerId);
}
