import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EventBase extends BasicBase{

	public EventBase(Connection con, String table) {
		super(con, table);
	}
	
	public boolean insertEvents(String eventId, String eventName, String description) throws SQLException {
		//judge the parameter
		if(eventId == null || eventId.equals("") || eventName == null || eventName.equals("")) {
			return false;
		}

		//open the link insert the parameter into the table
		String state = "INSERT INTO " + table + " (eventId, eventName, description) VALUES (?, ?, ?)";
		PreparedStatement insertStmt = con.prepareStatement(state);
		insertStmt.setString(1, eventId);
		insertStmt.setString(2, eventName);
		insertStmt.setString(3, description);
		insertStmt.execute();
		return true;
	}
	
	public boolean updateEventData(String eventId, String query, String value) throws SQLException {
		if(eventId.equals("") || eventId == null) {
			return false;
		}
		if(!query.equals("eventName") && !query.equals("description")) {
			return false;
		}
		if(value == null) {
			return false;
		}
		updateData("eventId", eventId, query, value);
		return true;
	}

}
