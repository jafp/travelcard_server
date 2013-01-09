package dk.ihk.tcp.util;

public class ShortBuffer
{
	private final short[] m_buffer;

	public static void main(String[] args)
	{
		ShortBuffer b = new ShortBuffer();
		b.setInt8(0, 5);
		b.setInt16(1, 1230);

		b.setLong(0xe2f5ed4cL);
		long l = b.getLong();
		System.out.println("l: " + l);

		b.setInt8(0, 5);
		b.setInt16(1, 1230);
		b.setInt16(3, 657);

		short s = (short) (b.getBuffer()[1] << 8 | b.getBuffer()[2]);
		System.out.println(b.getBuffer()[0] + " " + s + " " + b.getInt16(1) + " - - " + b.getInt16(3));
	}

	public ShortBuffer()
	{
		m_buffer = new short[8];
	}

	public void setInt8(int idx, int s)
	{
		m_buffer[idx] = (short) (s & 0xff);
	}

	public void setInt16(int idx, int s)
	{
		m_buffer[idx] = (short) ((s & 0xff00) >> 8);
		m_buffer[idx + 1] = (short) (s & 0xff);
	}

	public void setLong(long l)
	{
		for (int i = 0; i < 8; i++)
		{
			m_buffer[7 - i] = (short) (l >> ((7 - i) * 8));
		}
	}

	public long getLong()
	{
		long l = 0;
		for (int i = 0; i < 8; i++)
		{
			l += (m_buffer[i] & 0xff) << (i * 8);
		}
		l = l & 0xffffffffL;
		return l;
	}

	public int getInt8(int idx)
	{
		return m_buffer[idx] & 0xff;
	}

	public int getInt16(int idx)
	{
		return (m_buffer[idx] << 8 | m_buffer[idx + 1]) & 0xffff;
	}

	public short[] getBuffer()
	{
		return m_buffer;
	}
}
