import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BasicBase {
	protected Connection con;
	protected String table;
	
	public BasicBase(Connection con, String table) {
		this.con = con;
		this.table = table;
	}
	
	public void updateData(String conditionType, String conditionValue, 
						   String query, String value) throws SQLException {
		String state = "update " + table + " set " + query + " = ? where " + conditionType + " = ?";
		PreparedStatement updateStmt = con.prepareStatement(state);
		updateStmt.setString(1, value);
		updateStmt.setString(2, conditionValue);
		updateStmt.execute();
	}
	
	public ResultSet getResult(String query, String value) throws SQLException {
		String sm = "select * from " + table + " where " + query + "=?";
		PreparedStatement ps = con.prepareStatement(sm);
		ps.setString(1, value);
		ResultSet res = ps.executeQuery();
		return res;
	}
}
