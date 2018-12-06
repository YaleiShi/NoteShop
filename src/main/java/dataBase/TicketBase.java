package dataBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The ticket base used to maintain the functions
 * and connection to the mysql server
 * @author yalei
 *
 */
public class TicketBase extends BasicBase{
	
	/**
	 * pass the connection and table name to build the data base
	 * @param con
	 * @param table
	 */
	public TicketBase(Connection con, String table) {
		super(con, table);
	}
	
	/**
	 * create a new ticket by entering the event id and user id
	 * loop for num times
	 * @param eventId
	 * @param userId
	 * @param num
	 * @return
	 * @throws SQLException
	 */
	public boolean newTicket(String eventId, String userId, int num) throws SQLException {
		//judge the parameter
		if(eventId == null || eventId.equals("") || userId == null || userId.equals("")) {
			return false;
		}

		//open the link insert the parameter into the table
		String state = "INSERT INTO " + table + " (eventId, userId) VALUES (?, ?)";
		PreparedStatement insertStmt = con.prepareStatement(state);
		insertStmt.setString(1, eventId);
		insertStmt.setString(2, userId);
		for(int i = 0; i < num; i++) {
			insertStmt.execute();
		}
		return true;
	}
	
	/**
	 * verify the owner of the ticket
	 * by the ticket id and user id
	 * @param userId
	 * @param ticketId
	 * @return
	 * @throws SQLException
	 */
	public boolean verifyOwner(String userId, int ticketId) throws SQLException {
		String sm = "select * from " + table + " where ticketId=?";
		PreparedStatement ps = con.prepareStatement(sm);
		ps.setInt(1, ticketId);
		ResultSet res = ps.executeQuery();
		while(res.next()) {
			String owner = res.getString("userId");
			if(owner.equals(userId)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * change the user of the ticket to the target user id
	 * @param userId
	 * @param ticketId
	 * @param target
	 * @return
	 */
	public boolean changeUser(String userId, int ticketId, String target){
		if(target == null) {
			return false;
		}
		try {
			if(verifyOwner(userId, ticketId)) {
				String state = "update " + table + " set userId = ? where ticketId = ?";
				PreparedStatement updateStmt = con.prepareStatement(state);
				updateStmt.setString(1, target);
				updateStmt.setInt(2, ticketId);
				updateStmt.execute();
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}
}
