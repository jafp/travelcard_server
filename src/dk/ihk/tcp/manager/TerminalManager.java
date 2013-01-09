package dk.ihk.tcp.manager;

import dk.ihk.tcp.cardreader.Reading;

/**
 * Handles the highest level of logic in the terminal.
 * 
 * @author Jacob Pedersen
 */
public interface TerminalManager
{
	/**
	 * Handle a reading from a card reader.
	 * 
	 * @param reading The reading
	 * @throws Exception
	 */
	void handleReading(Reading reading) throws Exception;
}
