package dk.ihk.tcp.test;

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import dk.ihk.tcp.util.db.HasIdentifier;

public class MemoryDAO<T extends HasIdentifier>
{
	protected Long m_id = 0L;
	protected SortedMap<Long, T> m_objects = new TreeMap<Long, T>();

	public synchronized void insertOrUpdate(T o)
	{
		if (o.getId() == 0)
		{
			o.setId(++m_id);
		}
		m_objects.put(o.getId(), o);
	}

	public synchronized boolean delete(T o)
	{
		return m_objects.remove(o.getId()) != null;
	}

	public synchronized void deleteAll()
	{
		m_objects.clear();
	}

	public synchronized T findById(long id)
	{
		return m_objects.get(new Long(id));
	}

	public synchronized SortedSet<T> findAll()
	{
		return getValues();
	}

	public synchronized SortedMap<Long, T> getMap()
	{
		return m_objects;
	}

	public synchronized SortedSet<T> getValues()
	{
		return new TreeSet<T>(m_objects.values());
	}
}
