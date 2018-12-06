package dataBase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * basic class of all the database
 * @author yalei
 *
 */
public abstract class BasicBase {
	protected Connection con;
	protected String table;
	
	/**
	 * pass in the JDBC connection and the table name
	 * then build up the database
	 * @param con
	 * @param table
	 */
	public BasicBase(Connection con, String table) {
		this.con = con;
		this.table = table;
	}
	
	/**
	 * the general function to update any element
	 * in the mysql data base;
	 * by pass in the condition type and condition value,
	 * also with the query and value
	 * @param conditionType
	 * @param conditionValue
	 * @param query
	 * @param value
	 * @throws SQLException
	 */
	public void updateData(String conditionType, String conditionValue, 
						   String query, String value) throws SQLException {
		String state = "update " + table + " set " + query + " = ? where " + conditionType + " = ?";
		PreparedStatement updateStmt = con.prepareStatement(state);
		updateStmt.setString(1, value);
		updateStmt.setString(2, conditionValue);
		updateStmt.execute();
	}
	
	public void updateData(String conditionType, int index, 
			   String query, String value) throws SQLException {
		String state = "update " + table + " set " + query + " = ? where " + conditionType + " = ?";
		PreparedStatement updateStmt = con.prepareStatement(state);
		updateStmt.setString(1, value);
		updateStmt.setInt(2, index);
		updateStmt.execute();
	}
	
	/**
	 * return the whole table as the result set
	 * @return
	 * @throws SQLException
	 */
	public ResultSet getAllResult() throws SQLException {
		String sm = "select * from " + table;
		PreparedStatement ps = con.prepareStatement(sm);
		ResultSet res = ps.executeQuery();
		return res;
	}
	
	/**
	 * the general function to get the result of the
	 * query = value;
	 * @param query
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	public ResultSet getResult(String query, String value) throws SQLException {
		String sm = "select * from " + table + " where " + query + "=?";
		PreparedStatement ps = con.prepareStatement(sm);
		ps.setString(1, value);
		ResultSet res = ps.executeQuery();
		return res;
	}
	
	/**
	 * the general function to get the result of the
	 * query = value;
	 * version of int
	 * @param query
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	public ResultSet getResult(String query, int value) throws SQLException {
		String sm = "select * from " + table + " where " + query + "=?";
		PreparedStatement ps = con.prepareStatement(sm);
		ps.setInt(1, value);
		ResultSet res = ps.executeQuery();
		return res;
	}
}
