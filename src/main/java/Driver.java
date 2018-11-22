import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Driver {
	private static Connection con;
	
	public static void main(String[] args) throws SQLException {
		prepareConnection();
		
		UserBase ub = new UserBase(con, "Users");
		EventBase events = new EventBase(con, "Events");
		TicketBase tickets = new TicketBase(con, "Tickets");
		
		ub.saveUserData("pig", "hahaha", "test account 2");
		System.out.println(ub.checkPass("pig", "hahaha"));
		ub.changePassWord("pig", "huhuhu");
		System.out.println(ub.checkPass("pig", "hahaha"));
		System.out.println(ub.checkPass("pig", "huhuhu"));
		
		events.insertEvents("001", "black friday", "discount");
		tickets.newTicket("001", "cat");
		tickets.newTicket("002", "cat");
		tickets.newTicket("001", "monkey");
	}
	
	public static void prepareConnection() throws SQLException {
		String username  = "user45";
		String password  = "user45";
		String db  = "user45";
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
