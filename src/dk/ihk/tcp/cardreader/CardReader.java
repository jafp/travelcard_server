package dk.ihk.tcp.cardreader;

/**
 * Interface representing a card reader.
 * 
 * @author Jacob Pedersen
 */
public interface CardReader
{
	/**
	 * @param id The serial number of this card reader
	 */
	void setSerialNumber(String id);

	/**
	 * Connects to the card reader
	 * 
	 * @throws DeviceException If connection fails
	 */
	void connect() throws DeviceException;

	/**
	 * @return True if the card reader is connected
	 */
	boolean isConnected();

	/**
	 * Reads a card. This method block until a card is read.
	 * 
	 * @return The reading
	 * @throws DeviceException If connection fails
	 */
	Reading readCard() throws DeviceException;

	/**
	 * Sends a response back to the card reader
	 * 
	 * @param resp The response to send
	 * @return True on success
	 */
	boolean sendResponse(Response resp);

	/**
	 * Disconnect from the card reader
	 */
	void disconnect();
}
