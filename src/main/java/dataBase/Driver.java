package dataBase;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * the initialize function of the server
 * by add the original users and events and tickets
 * @author yalei
 *
 */
public class Driver {
	private static Connection con;
	
	/**
	 * prepare the connection and add the original data
	 * into the mysql base
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException {
		prepareConnection();
		
		UserBase ub = new UserBase(con, "Users");
		EventBase events = new EventBase(con, "Events");
		TicketBase tickets = new TicketBase(con, "Tickets");

		
		ub.saveUserData("admin", "admin", "Yalei", "Shi", "Administrator");
		ub.saveUserData("cat", "cat", "xxxxx", "xxxx", "test account 1");
		ub.saveUserData("pig", "pig", "xxx", "xxx", "test account 2");
		ub.changePassWord("pig", "huhuhu");
		
		
		events.insertEvents("black friday 2018", "black buy buy", "11/30/2018", 30, "discount", "admin");
		events.insertEvents("cs 601 final 2018", "final exam", "11/20/2018", 50, "fight", "someOne");
		events.insertEvents("cs 601 side", "side project", "12/11/2018", 5, "still fight", "admin");
		events.insertEvents("christmas 2018", "christmas day", "12/25/2018", 100, "merry christmas", "cat");
		tickets.newTicket("001", "cat", 2);
	}
	
	/**
	 * prepare the connection to the mysql
	 * @throws SQLException
	 */
	public static void prepareConnection() throws SQLException {
		final String username  = "user45";
		final String password  = "user45";
		final String db  = "user45";
		String url = "jdbc:mysql://127.0.0.1:3306/";
		String timezone = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
		
		
		try {
			// load driver
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		}
		catch (Exception e) {
			System.err.println("Can't find driver");
			System.exit(1);
		}

		con = DriverManager.getConnection(url + db + timezone, username, password);
	}
}
