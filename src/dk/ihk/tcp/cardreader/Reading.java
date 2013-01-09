package dk.ihk.tcp.cardreader;

/**
 * A reading read by a card reader.
 * 
 * @author Jacob Pedersen
 */
public class Reading
{
	/**
	 * The card ID
	 */
	private final long m_id;

	/**
	 * The sent response
	 */
	private Response m_response;

	/**
	 * The reader that read this reading
	 */
	private final CardReader m_reader;

	/**
	 * Construct a reading with the given ID.
	 * 
	 * @param id The card id
	 * @param reader The reader to respond to
	 */
	public Reading(long id, CardReader reader)
	{
		m_id = id;
		m_response = null;
		m_reader = reader;
	}

	public long getCardId()
	{
		return m_id;
	}

	/**
	 * Respond back to the card reader with the given response.
	 * 
	 * @param resp The response
	 */
	public void respondWith(Response resp)
	{
		m_response = resp;
		m_reader.sendResponse(m_response);
	}

	/**
	 * @return The possibly sent response
	 */
	public Response getResponse()
	{
		return m_response;
	}

	/**
	 * @return True if a response has been sent
	 */
	public boolean isResponseSent()
	{
		return m_response != null;
	}
}
