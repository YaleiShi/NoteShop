package dataBase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GreatDataBase {
	private static GreatDataBase instance;
	private UserBase users;
	private EventBase events;
	private TicketBase tickets;
	private final String TEST = "com.mysql.cj.jdbc.Driver";
	private String username, password, db, url, timezone;
	private String userTable, eventTable, ticketTable;
	private Connection con;
	
	private GreatDataBase() throws SQLException {
		prepareParam();
		prepareConnection();
		
		this.users = new UserBase(con, userTable);
		this.events = new EventBase(con, eventTable);
		this.tickets = new TicketBase(con, ticketTable);
	}
	
	public void prepareParam() {
		ConfigReader cr = new ConfigReader("config.json");
		this.username = cr.getConfig("username");
		this.password = cr.getConfig("password");
		this.db = cr.getConfig("database");
		this.url = cr.getConfig("url");
		this.timezone = cr.getConfig("timezone");
		this.userTable = cr.getConfig("userTable");
		this.eventTable = cr.getConfig("eventTable");
		this.ticketTable = cr.getConfig("ticketTable");
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
	
	/**
	 * get the singleton instance of AmazonDataBase
	 * @return
	 * @throws SQLException 
	 */
	public static synchronized GreatDataBase getInstance() throws SQLException {
		if(instance == null) {
			instance = new GreatDataBase();
		}
		return instance;
	}
	
	public boolean checkPass(String userId, String password) {
		return users.checkPass(userId, password);
	}
	
	public boolean ifUserExist(String userId) {
		return users.ifUserExist(userId);
	}
	
	public ResultSet getUserEvent(String userId) throws SQLException {
		String stmt = "SELECT * FROM Tickets join Events "
				+ "on Tickets.eventId=Events.eventId "
				+ "where userId=?";
		PreparedStatement select = con.prepareStatement(stmt);
		select.setString(1, userId);
		ResultSet res = select.executeQuery();
		return res;
	}
	
	public boolean createEvents(String eventId, String eventName, String date, int num, String detail, String createBy) {
		try {
			if(this.events.insertEvents(eventId, eventName, date, num, detail, createBy)) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public boolean updateEvent(String userId, String eventId, String eventName, 
			String date, int num, String detail) {
		try {
			if(this.events.updateEventData(userId, eventId, eventName, date, num, detail)) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public boolean saveUserData(String userId, String password, 
			String firstName, String lastName, String detail) throws SQLException {
		return users.saveUserData(userId, password, firstName, lastName, detail);
	}
	
	public synchronized boolean transaction(String userId, String targetId, int ticketId) {
		if(!this.users.ifUserExist(userId) || !this.users.ifUserExist(targetId)) {
			return false;
		}
		if(this.tickets.changeUser(userId, ticketId, targetId)) {
			return true;
		}
		return false;
	}

	public ResultSet getAllEvents() throws SQLException {
		return this.events.getAllResult();
	}
	
	public ResultSet getUserData(String query, String value) throws SQLException {
		return this.users.getResult(query, value);
	}
	
	public ResultSet getEventData(String query, String value) throws SQLException {
		return this.events.getResult(query, value);
	}
	
	public synchronized boolean buyTickets(String userId, String eventId, int num){
		try {
			if(this.events.buyTickets(eventId, num)) {
				if(this.tickets.newTicket(eventId, userId, num)) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}	
		return false;
	}
}
