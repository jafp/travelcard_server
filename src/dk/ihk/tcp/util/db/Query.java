package dk.ihk.tcp.util.db;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import dk.ihk.tcp.db.DBException;

public class Query
{
	public static <T> QueryBuilder<T> from(String tableName)
	{
		return new QueryBuilder<T>(tableName);
	}

	public static <T> QueryBuilder<T> from(String tableName, Class<T> type)
	{
		return new QueryBuilder<T>(tableName).ofType(type);
	}

	public static class QueryBuilder<T>
	{
		private Class<T> m_type;
		private final String m_table;
		private final List<Object> m_numberedParameters = new ArrayList<Object>();
		private final List<String> m_orderBy = new ArrayList<String>();
		private final List<Where> m_wheres = new ArrayList<Where>();

		public QueryBuilder(String table)
		{
			m_table = table;
		}

		public QueryBuilder<T> where(String columnName, Object value)
		{
			m_wheres.add(new Where(columnName, value));
			return this;
		}

		public QueryBuilder<T> ofType(Class<T> type)
		{
			m_type = type;
			return this;
		}

		public QueryBuilder<T> orderBy(String columnName)
		{
			m_orderBy.add(columnName);
			return this;
		}

		public T findOne() throws DBException
		{
			return JDBCHelper.queryOne(getSql(), new AnnotationRowMapper<T>(m_type), m_numberedParameters.toArray());
		}

		public SortedSet<T> find() throws DBException
		{
			return JDBCHelper.query(getSql(), new AnnotationRowMapper<T>(m_type), m_numberedParameters.toArray());
		}

		private String getSql()
		{
			StringBuilder builder = new StringBuilder();
			builder.append("SELECT * FROM ");
			builder.append("`").append(m_table).append("` ");

			if (!m_wheres.isEmpty())
			{
				builder.append("WHERE ");
				for (int i = 0; i < m_wheres.size(); i++)
				{
					Where w = m_wheres.get(i);
					builder.append("`").append(w.m_column).append("` = ? ");
					m_numberedParameters.add(w.m_value);

					if (i < m_wheres.size() - 1)
					{
						builder.append(" AND ");
					}
				}
			}

			return builder.toString();
		}
	}

	private static class Where
	{
		private final String m_column;
		private final Object m_value;

		public Where(String col, Object val)
		{
			m_column = col;
			m_value = val;
		}
	}
}
