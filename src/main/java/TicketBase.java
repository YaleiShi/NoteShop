import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TicketBase extends BasicBase{
	
	public TicketBase(Connection con, String table) {
		super(con, table);
	}
	
	public boolean newTicket(String eventId, String userId) throws SQLException {
		//judge the parameter
		if(eventId == null || eventId.equals("") || userId == null || userId.equals("")) {
			return false;
		}

		//open the link insert the parameter into the table
		String state = "INSERT INTO " + table + " (eventId, userId) VALUES (?, ?)";
		PreparedStatement insertStmt = con.prepareStatement(state);
		insertStmt.setString(1, eventId);
		insertStmt.setString(2, userId);
		insertStmt.execute();
		return true;
	}
	
	public boolean changeUser(String ticketId, String value) throws SQLException {
		if(ticketId.equals("") || ticketId == null) {
			return false;
		}
		if(value == null) {
			return false;
		}
		updateData("ticketId", ticketId, "userId", value);
		return true;
	}
}
