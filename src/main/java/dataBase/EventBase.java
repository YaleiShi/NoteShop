package dataBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EventBase extends BasicBase{

	public EventBase(Connection con, String table) {
		super(con, table);
	}
	
	public boolean insertEvents(String eventId, String eventName, String date, 
			int num, String description, String createBy) throws SQLException {
		//judge the parameter
		if(eventId == null || eventId.equals("") || eventName == null || eventName.equals("")) {
			return false;
		}

		//open the link insert the parameter into the table
		String state = "INSERT INTO " + table + " (eventId, eventName, date, numTickets, description, createBy) VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement insertStmt = con.prepareStatement(state);
		insertStmt.setString(1, eventId);
		insertStmt.setString(2, eventName);
		insertStmt.setString(3, date);
		insertStmt.setInt(4, num);
		insertStmt.setString(5, description);
		insertStmt.setString(6, createBy);
		insertStmt.execute();
		return true;
	}
	
	public boolean buyTickets(String eventId, int num) throws SQLException {
		ResultSet res = super.getResult("eventId", eventId);
		int rest = -1;
		while(res.next()) {
			rest = res.getInt("numTickets");
		}
		if(rest < num) {
			return false;
		}
		String state = "update " + table + " set numTickets = ? where eventId = ?";
		PreparedStatement updateStmt = con.prepareStatement(state);
		updateStmt.setInt(1, rest - num);
		updateStmt.setString(2, eventId);
		updateStmt.execute();
		return true;
	}
	
	public boolean verifyCreator(String userId, String eventId) throws SQLException {
		ResultSet res = super.getResult("eventId", eventId);
		while(res.next()) {
			String creator = res.getString("createBy");
			if(creator.equals(userId)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean updateEventData(String userId, String eventId, String eventName, 
			String date, int num, String detail) throws SQLException {
		if(!verifyCreator(userId, eventId)) {
			return false;
		}
		super.updateData("eventId", eventId, "eventName", eventName);
		super.updateData("eventId", eventId, "date", date);
		super.updateData("eventId", eventId, "description", detail);
		
		String state = "update " + table + " set numTickets = ? where eventId = ?";
		PreparedStatement updateStmt = con.prepareStatement(state);
		updateStmt.setInt(1, num);
		updateStmt.setString(2, eventId);
		updateStmt.execute();
		return true;
	}

}
