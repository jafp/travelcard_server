package dk.ihk.tcp.test;

import java.util.SortedSet;
import java.util.TreeSet;

import dk.ihk.tcp.datamodel.Journey;
import dk.ihk.tcp.db.DBException;
import dk.ihk.tcp.db.JourneyDAO;
import dk.ihk.tcp.util.DateHelper;
import dk.ihk.tcp.util.inject.Inject;

public class MemoryJourneyDAO implements JourneyDAO
{
	private final MemoryDAO<Journey> m_dao = new MemoryDAO<Journey>();

	@Inject
	public MemoryJourneyDAO()
	{

	}

	@Override
	public void insert(Journey j) throws DBException
	{
		j.setCreatedAt(DateHelper.getNow());
		j.setUpdatedAt(j.getCreatedAt());
		m_dao.insertOrUpdate(j);
	}

	@Override
	public void update(Journey j) throws DBException
	{
		j.setUpdatedAt(DateHelper.getNow());
		m_dao.insertOrUpdate(j);
	}

	@Override
	public void deleteAll() throws DBException
	{
		m_dao.deleteAll();
	}

	@Override
	public Journey findLastByCustomer(long customerId) throws DBException
	{
		SortedSet<Journey> set = findAllByCustomer(customerId);
		return set.size() > 0 ? set.first() : null;
	}

	@Override
	public SortedSet<Journey> findAllByCustomer(long customerId) throws DBException
	{
		SortedSet<Journey> journeys = new TreeSet<Journey>();
		for (Journey j : m_dao.findAll())
		{
			if (j.getCustomerId() == customerId)
			{
				journeys.add(j);
			}
		}
		return journeys;
	}
}
