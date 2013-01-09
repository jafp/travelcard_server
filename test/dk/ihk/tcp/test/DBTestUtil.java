package dk.ihk.tcp.test;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import dk.ihk.tcp.db.DBException;
import dk.ihk.tcp.db.DatabaseManager;

public class DBTestUtil
{
	public static void setup() throws ClassNotFoundException, SQLException, IOException, DBException
	{
		// Connect
		DatabaseManager.init("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/tcp_test?user=root&password=root",
				"root", "root");

		// Read table definitions
		Path tablesSQL = Paths.get(".", "tables.sql");
		Reader reader = new FileReader(tablesSQL.toFile());
		;

		ScriptRunner runner = new ScriptRunner(DatabaseManager.getConnection(), false, true);
		runner.runScript(reader);

		reader.close();

	}
}
