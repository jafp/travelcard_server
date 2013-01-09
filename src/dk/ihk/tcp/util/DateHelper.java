package dk.ihk.tcp.util;

import java.util.Date;

public class DateHelper
{

	public static Date getNow()
	{
		/*
		 * Remove the milliseconds parts of the date, because most databases
		 * doesn't support milliseconds in their TIMESTAMP types. They simply
		 * store a timestamp as a UTF formatted string. MysSQL data types:
		 * http://dev.mysql.com/doc/refman/5.5/en/datetime.html StackOverflow
		 * topic: http://stackoverflow.com/a/3634813
		 */
		Date now = new Date();
		now.setTime((now.getTime() / 1000) * 1000);

		return now;
	}
}
