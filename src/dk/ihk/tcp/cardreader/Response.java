package dk.ihk.tcp.cardreader;

import dk.ihk.tcp.util.ShortBuffer;

/**
 * Response codes
 */
public class Response
{
	/**
	 * Types of response
	 */
	public enum Type
	{
		UNKNOWN, ERROR, CARD_NOT_FOUND, INSUFFICIENT_FUNDS, TOO_LATE_CHECK_OUT, INVALID_CARD, CHECKED_IN, CHECKED_OUT, OK
	}

	/**
	 * The response type
	 */
	private final Type m_type;

	/**
	 * The response data
	 */
	private final ShortBuffer m_buffer;

	/**
	 * Construct a response with the given type and empty data.
	 * 
	 * @param type The type
	 */
	private Response(Type type)
	{
		m_type = type;
		m_buffer = new ShortBuffer();
	}

	/**
	 * Static helper method for creating responses
	 * 
	 * @param type The type of response
	 * @return The newly created response
	 */
	public static Response create(Type type)
	{
		return new Response(type);
	}

	public Response setShort(int index, int value)
	{
		m_buffer.setInt16(index, (short) value);
		return this;
	}

	public Response setChar(int index, int value)
	{
		m_buffer.setInt16(index, (char) value);
		return this;
	}

	public Type getType()
	{
		return m_type;
	}

	public ShortBuffer getBuffer()
	{
		m_buffer.setInt8(0, m_type.ordinal());
		return m_buffer;
	}
}