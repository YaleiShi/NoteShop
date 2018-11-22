import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GreatDataBase {
	private UserBase users;
	private EventBase events;
	private TicketBase tickets;
	private final String TEST = "com.mysql.cj.jdbc.Driver";
	private String username, password, db, url, timezone;
	private Connection con;
	
	public GreatDataBase(String username, String password, String database, String url, String timezone, 
	        		     String userTable, String eventTable, String ticketTable) throws SQLException {
		this.username = username;
		this.password = password;
		this.db = database;
		this.url = url;
		this.timezone = timezone;
		prepareConnection();
		
		users = new UserBase(con, userTable);
		
	}

	public void prepareConnection() throws SQLException {
		try {
			// load driver
			Class.forName(TEST).newInstance();
		}
		catch (Exception e) {
			System.err.println("Can't find driver");
			System.exit(1);
		}

		con = DriverManager.getConnection(url + db + timezone, username, password);
	}
}
