import java.sql.SQLException;

public class Driver {
	
	public static void main(String[] args) throws SQLException {
		String username  = "user45";
		String password  = "user45";
		String db  = "user45";
		String urlString = "jdbc:mysql://127.0.0.1:3306/";
		String timeZoneSettings = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
		
		UserBase ub = new UserBase(username, password, db, "Users", urlString, timeZoneSettings);
		ub.saveUserData("pig", "hahaha", "test account 2");
		System.out.println(ub.checkPass("pig", "hahaha"));
		ub.changePassWord("pig", "huhuhu");
		System.out.println(ub.checkPass("pig", "hahaha"));
		System.out.println(ub.checkPass("pig", "huhuhu"));
	}

}
