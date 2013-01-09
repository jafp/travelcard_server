package dk.ihk.tcp.datamodel;

import java.util.Date;

import dk.ihk.tcp.util.db.HasIdentifier;

/**
 * Data model class representing a journey.
 * 
 * @author Jacob Pedersen
 */
public class Journey implements HasIdentifier, Comparable<Journey>
{
	public static final int UNKNOWN = 0;
	public static final int CHECKED_IN = 1;
	public static final int CHECKED_OUT = 2;
	public static final int CHECKED_OUT_TOO_LATE = 3;

	private long m_id;
	private long m_customerId;
	private int m_startZone;
	private Date m_startTime;
	private int m_endZone;
	private Date m_endTime;
	private int m_price;
	private int m_status;
	private Date m_createdAt;
	private Date m_updatedAt;

	public Journey()
	{
	}

	@Override
	public long getId()
	{
		return m_id;
	}

	@Override
	public void setId(long id)
	{
		this.m_id = id;
	}

	public void setCustomerId(long id)
	{
		m_customerId = id;
	}

	public long getCustomerId()
	{
		return m_customerId;
	}

	public void setStartZone(int startZone)
	{
		m_startZone = startZone;
	}

	public int getStartZone()
	{
		return m_startZone;
	}

	public void setStartTime(Date startTime)
	{
		m_startTime = startTime;
	}

	public Date getStartTime()
	{
		return m_startTime;
	}

	public void setEndZone(int endZone)
	{
		m_endZone = endZone;
	}

	public int getEndZone()
	{
		return m_endZone;
	}

	public void setEndTime(Date endTime)
	{
		m_endTime = endTime;
	}

	public Date getEndTime()
	{
		return m_endTime;
	}

	public int getPrice()
	{
		return m_price;
	}

	public void setPrice(Integer price)
	{
		this.m_price = price;
	}

	public Date getCreatedAt()
	{
		return m_createdAt;
	}

	public void setCreatedAt(Date createdAt)
	{
		this.m_createdAt = createdAt;
	}

	public Date getUpdatedAt()
	{
		return m_updatedAt;
	}

	public void setUpdatedAt(Date updatedAt)
	{
		m_updatedAt = updatedAt;
	}

	public void setStatus(int status)
	{
		m_status = status;
	}

	public int getStatus()
	{
		return m_status;
	}

	public boolean isCheckedOut()
	{
		return m_status == CHECKED_OUT || m_status == CHECKED_OUT_TOO_LATE;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}

		if (!(obj instanceof Journey))
		{
			return false;
		}

		Journey other = (Journey) obj;

		return (m_id == other.m_id && m_customerId == other.m_customerId && m_startZone == other.m_startZone
				&& m_endZone == other.m_endZone && m_price == other.m_price && m_status == other.m_status
				&& m_createdAt.equals(other.m_createdAt) && m_updatedAt.equals(other.m_updatedAt));
	}

	@Override
	public int compareTo(Journey o)
	{
		if (o.getId() < m_id)
		{
			return -1;
		}
		if (o.getId() > m_id)
		{
			return 1;
		}
		return 0;
	}
}
