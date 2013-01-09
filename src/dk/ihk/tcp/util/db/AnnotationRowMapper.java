package dk.ihk.tcp.util.db;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

public class AnnotationRowMapper<T> implements JDBCHelper.RowMapper<T>
{
	private final Class<T> m_clazz;
	private final java.util.Map<String, Field> m_fields;

	public AnnotationRowMapper(Class<T> clazz)
	{
		m_clazz = clazz;
		m_fields = new HashMap<String, Field>();
		for (Field f : m_clazz.getDeclaredFields())
		{
			if (f.isAnnotationPresent(Map.class))
			{
				Map mapAnnotation = f.getAnnotation(Map.class);
				m_fields.put(mapAnnotation.value(), f);
			}
		}
	}

	@Override
	public T map(ResultSet rs, int rowIndex) throws SQLException
	{
		try
		{
			T o = m_clazz.newInstance();
			for (java.util.Map.Entry<String, Field> e : m_fields.entrySet())
			{
				setFieldValue(rs, o, e.getKey(), e.getValue());
			}
			return o;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			// ...
		}
		return null;
	}

	private void setFieldValue(ResultSet rs, T o, String name, Field field) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException
	{
		field.setAccessible(true);
		Class<?> type = field.getType();
		if (type == Long.TYPE)
		{
			field.setLong(o, rs.getLong(name));
		}
		else if (type == Integer.TYPE)
		{
			field.setInt(o, rs.getInt(name));
		}
		else if (type == String.class)
		{
			field.set(o, rs.getString(name));
		}
		else if (type == Date.class)
		{
			field.set(o, rs.getTimestamp(name));
		}
	}
}
