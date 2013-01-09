package dk.ihk.tcp.datamodel;

import java.util.Date;

import dk.ihk.tcp.util.db.HasIdentifier;
import dk.ihk.tcp.util.db.Map;

/**
 * Data model class representing a customer.
 * 
 * @author Jacob Pedersen
 */
public class Customer implements HasIdentifier, Comparable<Customer>
{
	public static final int ACTIVE = 0;
	public static final int INACTIVE = 1;

	@Map("id")
	private long m_id;

	@Map("card_id")
	private long m_cardId;

	@Map("status")
	private int m_status;

	@Map("balance")
	private long m_balance;

	@Map("name")
	private String m_name;

	@Map("created_at")
	private Date m_createdAt;

	@Map("updated_at")
	private Date m_updatedAt;

	public Customer()
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

	public void setCardId(long cardId)
	{
		m_cardId = cardId;
	}

	public long getCardId()
	{
		return m_cardId;
	}

	public String getName()
	{
		return m_name;
	}

	public void setName(String name)
	{
		this.m_name = name;
	}

	public long getBalance()
	{
		return m_balance;
	}

	public void setBalance(long balance)
	{
		m_balance = balance;
	}

	public int getStatus()
	{
		return m_status;
	}

	public void setStatus(int status)
	{
		m_status = status;
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

	@Override
	public String toString()
	{
		return m_id == 0 ? "<Ikke gemt kunde>" : "[" + m_id + "] " + m_name;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}

		if (!(obj instanceof Customer))
		{
			return false;
		}

		Customer other = (Customer) obj;

		return (m_id == other.m_id && m_cardId == other.m_cardId && m_balance == other.m_balance && m_status == other.m_status
				&& ((m_name == null && other.m_name == null) || (m_name != null && m_name.equals(other.m_name))) && ((m_createdAt == null && other.m_createdAt == null) || (m_createdAt
				.getTime() == other.m_createdAt.getTime())));
	}

	@Override
	public int compareTo(Customer o)
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

	public String toJSON()
	{
		return "{ \"id\": " + m_id + ", \"name\": \"" + m_name + "\"}";
	}

}
