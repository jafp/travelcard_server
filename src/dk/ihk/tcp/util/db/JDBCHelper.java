package dk.ihk.tcp.util.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.SortedSet;
import java.util.TreeSet;

import dk.ihk.tcp.db.DBException;
import dk.ihk.tcp.db.DatabaseManager;

/**
 * Helper class for performing the most common database actions.
 */
public class JDBCHelper
{
	public static long insert(String query, Object... args) throws DBException
	{
		PreparedStatement stmt = null;
		ResultSet keys = null;
		long generatedKey = 0;
		try
		{
			Connection conn = DatabaseManager.getConnection();
			stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			setParameters(stmt, args);

			int n = stmt.executeUpdate();

			if (n == 0)
			{
				throw new DBException("insert");
			}

			keys = stmt.getGeneratedKeys();
			if (keys.next())
			{
				generatedKey = keys.getLong(1);
			}
		}
		catch (Exception ex)
		{
			throw new DBException(ex.getMessage());
		}
		finally
		{
			try
			{
				if (stmt != null)
				{
					stmt.close();
				}
				if (keys != null)
				{
					keys.close();
				}
			}
			catch (Exception ex)
			{
			}
		}
		return generatedKey;
	}

	public static int update(String query, Object... args) throws DBException
	{
		PreparedStatement stmt = null;
		try
		{
			Connection conn = DatabaseManager.getConnection();
			stmt = conn.prepareStatement(query);
			setParameters(stmt, args);

			int n = stmt.executeUpdate();

			if (n < 0)
			{
				throw new DBException("update");
			}

			return n;
		}
		catch (Exception ex)
		{
			throw new DBException(ex.getMessage());
		}
		finally
		{
			try
			{
				if (stmt != null)
				{
					stmt.close();
				}
			}
			catch (Exception ex)
			{
			}
		}
	}

	public static <T> SortedSet<T> query(String query, RowMapper<T> mapper, Object... args) throws DBException
	{
		SortedSet<T> results = new TreeSet<T>();
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try
		{
			Connection conn = DatabaseManager.getConnection();
			stmt = conn.prepareStatement(query);
			for (int i = 0; i < args.length; i++)
			{
				stmt.setObject(i + 1, args[i]);
			}
			rset = stmt.executeQuery();
			int index = 0;
			while (rset.next())
			{
				results.add(mapper.map(rset, index++));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new DBException(e.getMessage());
		}
		finally
		{
			try
			{
				if (stmt != null)
				{
					stmt.close();
				}
				if (rset != null)
				{
					rset.close();
				}
			}
			catch (Exception e)
			{
				// Ignore
			}
		}
		return results;
	}

	public static <T> T queryOne(String query, RowMapper<T> mapper, Object... args) throws DBException
	{
		T result = null;
		PreparedStatement stmt = null;
		ResultSet rset = null;
		try
		{
			Connection conn = DatabaseManager.getConnection();
			stmt = conn.prepareStatement(query);
			for (int i = 0; i < args.length; i++)
			{
				stmt.setObject(i + 1, args[i]);
			}
			rset = stmt.executeQuery();
			if (rset.next())
			{
				result = mapper.map(rset, 0);
			}
		}
		catch (Exception e)
		{
			throw new DBException(e.getMessage());
		}
		finally
		{
			try
			{
				if (stmt != null)
				{
					stmt.close();
				}
				if (rset != null)
				{
					rset.close();
				}
			}
			catch (Exception e)
			{

			}
		}
		return result;
	}

	private static void setParameters(PreparedStatement stmt, Object args[]) throws SQLException
	{
		for (int i = 0; i < args.length; i++)
		{
			Object o = args[i];
			stmt.setObject(i + 1, o);
		}
	}

	public static interface RowMapper<T>
	{
		public T map(ResultSet rs, int rowIndex) throws SQLException;
	}
}
