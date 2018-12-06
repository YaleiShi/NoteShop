package dataBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * the individual data base of events
 * @author yalei
 *
 */
public class EventBase extends BasicBase{

	/**
	 * construct the data base by the connection to
	 * the mysql and the table name
	 * @param con
	 * @param table
	 */
	public EventBase(Connection con, String table) {
		super(con, table);
	}
	
	/**
	 * the function to insert a new event into the
	 * events data base
	 * @param eventId
	 * @param eventName
	 * @param date
	 * @param num
	 * @param description
	 * @param createBy
	 * @return
	 * @throws SQLException
	 */
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
	
	/**
	 * the search function which allow the user to search the events;
	 * if the type is num, then call available search function
	 * if not, use the like syntax in the mysql to partial search the term
	 * of the specific column
	 * @param type
	 * @param query
	 * @return
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	public ResultSet partialSearch(String type, String query) throws NumberFormatException, SQLException {
		if(type.equals("num")) {
			return availableSearch(Integer.parseInt(query));
		}
		String sm = "select * from " + table + " where " + type + " like ?";
		PreparedStatement ps = con.prepareStatement(sm);
		ps.setString(1, "%" + query + "%");
		ResultSet res = ps.executeQuery();
		return res;
	}
	
	/**
	 * this function will let the user enter the number of tickets
	 * and return the events which have more amount of tickets than the 
	 * number user has entered
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	public ResultSet availableSearch(int value) throws SQLException {
		String sm = "select * from " + table + " where numTickets>=?";
		PreparedStatement ps = con.prepareStatement(sm);
		ps.setInt(1, value);
		ResultSet res = ps.executeQuery();
		return res;
	}
	
	/**
	 * search the event by the event id
	 * then check if the event has that many tickets
	 * if not, return false
	 * if yes, reduce the number of tickets of the event and return true
	 * @param eventId
	 * @param num
	 * @return
	 * @throws SQLException
	 */
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
	
	/**
	 * verify the creator of the event
	 * if the event creator is equal to the user id
	 * return true
	 * if not, return false;
	 * @param userId
	 * @param eventId
	 * @return
	 * @throws SQLException
	 */
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
	
	/**
	 * update event data function,
	 * first call verify creator function to see if the user has the 
	 * right to modify it,
	 * then update the data in the data base
	 * @param userId
	 * @param eventId
	 * @param eventName
	 * @param date
	 * @param num
	 * @param detail
	 * @return
	 * @throws SQLException
	 */
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
