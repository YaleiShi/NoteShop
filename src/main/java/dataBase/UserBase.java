package dataBase;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.mysql.cj.xdevapi.Statement;

public class UserBase extends BasicBase{
	
	public UserBase(Connection con, String table) throws SQLException {
		super(con, table);
	}
	
	public boolean saveUserData(String userId, String password, 
			String firstName, String lastName, String detail) throws SQLException {
		//judge the parameter
		if(userId == null || userId.equals("") || password == null || password.equals("")) {
			return false;
		}
		//generate the salt and final hashed password
		String salt = generateSalt();
		String hashPass = generateFinalPassword(salt, password);
		//open the link insert the parameter into the table
		String state = "INSERT INTO " + table + " (userId, salt, hashPass, firstName, lastName, detail) VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement insertStmt = con.prepareStatement(state);
		insertStmt.setString(1, userId);
		insertStmt.setString(2, salt);
		insertStmt.setString(3, hashPass);
		insertStmt.setString(4, firstName);
		insertStmt.setString(5, lastName);
		insertStmt.setString(6, detail);
		insertStmt.execute();
		return true;
	}
	
	public boolean changePassWord(String userId, String newPassword) throws SQLException {
		if(userId == null || userId.equals("") || newPassword == null || newPassword.equals("")) {
			return false;
		}
		String newSalt = generateSalt();
		String newHashPass = generateFinalPassword(newSalt, newPassword);
		
		String state = "update " + table + " set salt = ?, hashPass = ? where userId = ?";
		PreparedStatement updateStmt = con.prepareStatement(state);
		updateStmt.setString(1, newSalt);
		updateStmt.setString(2, newHashPass);
		updateStmt.setString(3, userId);
		updateStmt.execute();
		return true;
	}
	
	public boolean updateUserData(String userId, String query, String value) throws SQLException {
		if(userId.equals("") || userId == null) {
			return false;
		}
		if(!query.equals("detail") || value == null) {
			return false;
		}
		updateData("userId", userId, query, value);
		return true;
	}
	
	public boolean ifUserExist(String userId) {
		try {
			ResultSet res = getResult("userId", userId);
			while(res.next()) {
				String user = res.getString("userId");
				if(user.equals(userId)) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	public ResultSet getUser(String userId) throws SQLException {
		return super.getResult("userId", userId);
	}
	
	public boolean checkPass(String userId, String password) {
		try {
			ResultSet res = getResult("userId", userId);
			while(res.next()) {
				String salt = res.getString("salt");
				String truePass = res.getString("hashPass");
				String hashPass = generateFinalPassword(salt, password);
//				System.out.println("salt: " + salt);
//				System.out.println("truePass: " + truePass);
//				System.out.println("hashPass: " + hashPass);
				if(truePass.equals(hashPass)) {
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}	
	
	public String generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return new String(salt);
	}
	
	public String generateFinalPassword(String saltS, String password) {
		byte[] salt = saltS.getBytes();
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		SecretKeyFactory factory;
		byte[] hash = null;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			hash = factory.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new String(hash);
	}
}
