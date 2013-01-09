package dk.ihk.tcp.db.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.SortedSet;

import dk.ihk.tcp.datamodel.Journey;
import dk.ihk.tcp.db.DBException;
import dk.ihk.tcp.db.JourneyDAO;
import dk.ihk.tcp.util.DateHelper;
import dk.ihk.tcp.util.db.JDBCHelper;
import dk.ihk.tcp.util.db.JDBCHelper.RowMapper;

public class MySQLJourneyDAO implements JourneyDAO
{

	@Override
	public void insert(Journey j) throws DBException
	{
		j.setCreatedAt(DateHelper.getNow());
		j.setUpdatedAt(j.getCreatedAt());
		long id = JDBCHelper
				.insert("INSERT INTO journeys (customer_id, price, status, start_zone, start_time, end_zone, end_time, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
						j.getCustomerId(), j.getPrice(), j.getStatus(), j.getStartZone(), j.getStartTime(), j.getEndZone(), j.getEndTime(),
						j.getCreatedAt(), j.getUpdatedAt());
		j.setId(id);
	}

	@Override
	public void update(Journey j) throws DBException
	{
		j.setUpdatedAt(DateHelper.getNow());
		JDBCHelper
				.insert("UPDATE journeys SET customer_id = ?, price = ?, status = ?, start_zone = ?, start_time = ?, end_zone = ?, end_time = ?, updated_at = ? WHERE id = ?",
						j.getCustomerId(), j.getPrice(), j.getStatus(), j.getStartZone(), j.getStartTime(), j.getEndZone(), j.getEndTime(),
						j.getUpdatedAt(), j.getId());
	}

	@Override
	public void deleteAll() throws DBException
	{
		JDBCHelper.update("DELETE FROM journeys");
	}

	@Override
	public SortedSet<Journey> findAllByCustomer(long customerId) throws DBException
	{
		return JDBCHelper.query("SELECT * FROM journeys WHERE customer_id = ? ORDER BY id", new JourneyMapper(), customerId);
	}

	@Override
	public Journey findLastByCustomer(long customerId) throws DBException
	{
		return JDBCHelper
				.queryOne("SELECT * FROM journeys WHERE customer_id = ? ORDER BY id DESC LIMIT 1", new JourneyMapper(), customerId);
	}

	private class JourneyMapper implements RowMapper<Journey>
	{

		@Override
		public Journey map(ResultSet rs, int rowIndex) throws SQLException
		{
			Journey j = new Journey();
			j.setId(rs.getLong("id"));
			j.setCustomerId(rs.getLong("customer_id"));
			j.setPrice(rs.getInt("price"));
			j.setStatus(rs.getInt("status"));
			j.setStartZone(rs.getInt("start_zone"));
			j.setStartTime(rs.getTimestamp("start_time"));
			j.setEndZone(rs.getInt("end_zone"));
			j.setEndTime(rs.getTimestamp("end_time"));
			j.setCreatedAt(rs.getTimestamp("created_at"));
			j.setUpdatedAt(rs.getTimestamp("updated_at"));
			return j;
		}

	}

}
