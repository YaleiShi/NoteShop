package dataBase;
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
		
//		System.out.println(ub.ifUserExist("cat"));
//		System.out.println(ub.ifUserExist("mouse"));
		
		ub.saveUserData("admin", "admin", "Yalei", "Shi", "Administrator");
		ub.saveUserData("cat", "cat", "xxxxx", "xxxx", "test account 1");
		ub.saveUserData("pig", "pig", "xxx", "xxx", "test account 2");
//		System.out.println(ub.checkPass("pig", "hahaha"));
//		ub.changePassWord("pig", "huhuhu");
//		System.out.println(ub.checkPass("pig", "hahaha"));
//		System.out.println(ub.checkPass("pig", "huhuhu"));
		
		
//		events.insertEvents("black friday 2018", "black buy buy", "11/30/2018", 30, "discount", "admin");
//		events.insertEvents("cs 601 final 2018", "final exam", "11/20/2018", 50, "fight", "someOne");
//		events.insertEvents("cs 601 side", "side project", "12/11/2018", 5, "still fight", "admin");
//		events.insertEvents("christmas 2018", "christmas day", "12/25/2018", 100, "merry christmas", "cat");
//		tickets.newTicket("001", "cat");
//		tickets.newTicket("001", "cat");
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
