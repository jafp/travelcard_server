package dk.ihk.tcp.datamodel;

import java.util.Date;

import dk.ihk.tcp.util.db.HasIdentifier;
import dk.ihk.tcp.util.db.Map;

public class TrackingPoint implements HasIdentifier, Comparable<TrackingPoint>
{
	public static final int CHECKED_IN = 1;
	public static final int CHECKED_OUT = 2;

	@Map("id")
	private long m_id;
	@Map("customer_id")
	private long m_customerId;
	private Customer m_customer;
	@Map("zone")
	private int m_zone;
	@Map("tracking_type")
	private int m_type;
	@Map("created_at")
	private Date m_createdAt;

	public TrackingPoint()
	{

	}

	public TrackingPoint(long customerId, int zone, int type)
	{
		m_customerId = customerId;
		m_zone = zone;
		m_type = type;
	}

	public long getCustomerId()
	{
		return m_customerId;
	}

	public Customer getCustomer()
	{
		return m_customer;
	}

	public void setCustomer(Customer customer)
	{
		m_customer = customer;
	}

	public int getZone()
	{
		return m_zone;
	}

	public int getType()
	{
		return m_type;
	}

	public Date getCreatedAt()
	{
		return m_createdAt;
	}

	public void setCreatedAt(Date date)
	{
		m_createdAt = date;
	}

	@Override
	public int compareTo(TrackingPoint o)
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

	@Override
	public long getId()
	{
		return m_id;
	}

	@Override
	public void setId(long id)
	{
		m_id = id;
	}
}
