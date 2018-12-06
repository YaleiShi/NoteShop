package dataBase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * the data manager class for the server,
 * manage all the function which connect the server and data base
 * @author yalei
 *
 */
public class GreatDataBase {
	private static GreatDataBase instance;
	private UserBase users;
	private EventBase events;
	private TicketBase tickets;
	private final String TEST = "com.mysql.cj.jdbc.Driver";
	private String username, password, db, url, timezone;
	private String userTable, eventTable, ticketTable;
	private Connection con;
	private ReentrantReadWriteLock userLock, eventLock, ticketLock;
	
	/**
	 * the constructor of the class,
	 * call prepare param to read the parameters from config file
	 * then start up the connection
	 * finally initiate the three data bases
	 * @throws SQLException
	 */
	private GreatDataBase() throws SQLException {
		prepareParam();
		prepareConnection();
		
		this.users = new UserBase(con, userTable);
		this.events = new EventBase(con, eventTable);
		this.tickets = new TicketBase(con, ticketTable);
		
		this.userLock = new ReentrantReadWriteLock();
		this.eventLock = new ReentrantReadWriteLock();
		this.ticketLock = new ReentrantReadWriteLock();
	}
	
	/**
	 * read the config file to get the parameters
	 */
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

	/**
	 * start up the connection of the jdbc to the mysql database
	 * @throws SQLException
	 */
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
	
	/**
	 * check the if the password is right according to the user id
	 * @param userId
	 * @param password
	 * @return
	 */
	public boolean checkPass(String userId, String password) {
		try {
			userLock.readLock().lock();
			return users.checkPass(userId, password);
		}finally {
			userLock.readLock().unlock();
		}
		
	}
	
	/**
	 * return if the user is already exit
	 * @param userId
	 * @return
	 */
	public boolean ifUserExist(String userId) {
		try {
			userLock.readLock().lock();
			return users.ifUserExist(userId);
		}finally {
			userLock.readLock().unlock();
		}
	}
	
	/**
	 * call the partial search method in the event base
	 * get all the event which partially has the value
	 * return table to the server
	 * @param type
	 * @param value
	 * @return
	 */
	public ResultSet partialSearch(String type, String value) {
		try {
			eventLock.readLock().lock();
			return this.events.partialSearch(type, value);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}finally {
			eventLock.readLock().unlock();
		}
	}
	
	/**
	 * get the joined table which contains
	 * the events which user has its tickets
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public ResultSet getUserEvent(String userId) throws SQLException {
		String stmt = "SELECT * FROM Tickets join Events "
				+ "on Tickets.eventId=Events.eventId "
				+ "where userId=?";
		PreparedStatement select = con.prepareStatement(stmt);
		select.setString(1, userId);
		eventLock.readLock().lock();
		ResultSet res = select.executeQuery();
		eventLock.readLock().unlock();
		return res;
	}
	
	/**
	 * create the event
	 * enter the unique event id,
	 * the name of the event,
	 * the date of the even, the number of tickets,
	 * and the details along with the creator id
	 * @param eventId
	 * @param eventName
	 * @param date
	 * @param num
	 * @param detail
	 * @param createBy
	 * @return
	 */
	public boolean createEvents(String eventId, String eventName, String date, int num, String detail, String createBy) {
		try {
			eventLock.writeLock().lock();
			if(this.events.insertEvents(eventId, eventName, date, num, detail, createBy)) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally {
			eventLock.writeLock().unlock();
		}
		return false;
	}
	
	/**
	 * update the event by the parameters
	 * return true if succeed
	 * @param userId
	 * @param eventId
	 * @param eventName
	 * @param date
	 * @param num
	 * @param detail
	 * @return
	 */
	public boolean updateEvent(String userId, String eventId, String eventName, 
			String date, int num, String detail) {
		try {
			this.eventLock.writeLock().lock();
			if(this.events.updateEventData(userId, eventId, eventName, date, num, detail)) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally {
			this.eventLock.writeLock().unlock();
		}
		return false;
	}
	
	/**
	 * save the new user data into the data base
	 * return true if succeed
	 * @param userId
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param detail
	 * @return
	 * @throws SQLException
	 */
	public boolean saveUserData(String userId, String password, 
			String firstName, String lastName, String detail) throws SQLException {
		try {
			this.userLock.writeLock().lock();
			return users.saveUserData(userId, password, firstName, lastName, detail);
		}finally {
			this.userLock.writeLock().unlock();
		}
		
	}
	
	/**
	 * transport the ticket to the target id from the original id
	 * first check if the users exist,
	 * then try to change the user
	 * return true if succeed,
	 * return false if not
	 * @param userId
	 * @param targetId
	 * @param ticketId
	 * @return
	 */
	public boolean transaction(String userId, String targetId, int ticketId) {
		this.userLock.readLock().lock();
		if(!this.users.ifUserExist(userId) || !this.users.ifUserExist(targetId)) {
			this.userLock.readLock().unlock();
			return false;
		}
		this.ticketLock.writeLock().lock();
		if(this.tickets.changeUser(userId, ticketId, targetId)) {
			this.userLock.readLock().unlock();
			this.ticketLock.writeLock().unlock();
			return true;
		}
		this.userLock.readLock().unlock();
		this.ticketLock.writeLock().unlock();
		return false;
	}

	/**
	 * return all the events to the server
	 * @return
	 * @throws SQLException
	 */
	public ResultSet getAllEvents() throws SQLException {
		this.eventLock.readLock().lock();
		ResultSet res = this.events.getAllResult();
		this.eventLock.readLock().unlock();
		return res;
	}
	
	/**
	 * return the user data by entering the query type
	 * and the value of the query
	 * @param query
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	public ResultSet getUserData(String query, String value) throws SQLException {
		this.userLock.readLock().lock();
		ResultSet res = this.users.getResult(query, value);
		this.userLock.readLock().unlock();
		return res;
	}
	
	/**
	 * get the event data by the query and the 
	 * value of the query
	 * @param query
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	public ResultSet getEventData(String query, String value) throws SQLException {
		this.eventLock.readLock().lock();
		ResultSet res = this.events.getResult(query, value);
		this.eventLock.readLock().unlock();
		return res;
	}
	
	/**
	 * sell the ticket to the user
	 * update the event table to reduce the number of tickets
	 * and the ticket table to add new ticket
	 * @param userId
	 * @param eventId
	 * @param num
	 * @return true if succeed
	 */
	public boolean buyTickets(String userId, String eventId, int num){
		try {
			this.eventLock.writeLock().lock();
			if(this.events.buyTickets(eventId, num)) {
				this.ticketLock.writeLock().lock();
				if(this.tickets.newTicket(eventId, userId, num)) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally {
			this.eventLock.writeLock().unlock();
			this.ticketLock.writeLock().unlock();
		}
		return false;
	}
}
