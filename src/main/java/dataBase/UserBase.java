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

/**
 * the user data base, 
 * maintain the functions and connection to the mysql database
 * @author yalei
 *
 */
public class UserBase extends BasicBase{
	
	/**
	 * pass in the connection and table name to build the 
	 * database
	 * @param con
	 * @param table
	 * @throws SQLException
	 */
	public UserBase(Connection con, String table) throws SQLException {
		super(con, table);
	}
	
	/**
	 * create a new user by all the parameters,
	 * generate a random salt and combine with the password to 
	 * generate the final hashed password
	 * then save everything into the mysql server
	 * @param userId
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param detail
	 * @return
	 * @throws SQLException
	 */
	public boolean saveUserData(String userId, String password, 
			String firstName, String lastName, String detail) throws SQLException {
		//judge the parameter
		if(userId == null || userId.equals("") || password == null || password.equals("")) {
			return false;
		}
		//generate the salt and final hashed password
		byte[] salt = generateSalt();
		byte[] hashPass = generateFinalPassword(salt, password);
		//open the link insert the parameter into the table
		String state = "INSERT INTO " + table + " (userId, salt, hashPass, firstName, lastName, detail) VALUES (?, ?, ?, ?, ?, ?)";
		PreparedStatement insertStmt = con.prepareStatement(state);
		insertStmt.setString(1, userId);
		insertStmt.setBytes(2, salt);
		insertStmt.setBytes(3, hashPass);
		insertStmt.setString(4, firstName);
		insertStmt.setString(5, lastName);
		insertStmt.setString(6, detail);
		insertStmt.execute();
		return true;
	}
	
	/**
	 * change the password of the user who has the user id
	 * return true if succeed
	 * @param userId
	 * @param newPassword
	 * @return
	 * @throws SQLException
	 */
	public boolean changePassWord(String userId, String newPassword) throws SQLException {
		if(userId == null || userId.equals("") || newPassword == null || newPassword.equals("")) {
			return false;
		}
		byte[] newSalt = generateSalt();
		byte[] newHashPass = generateFinalPassword(newSalt, newPassword);
		
		String state = "update " + table + " set salt = ?, hashPass = ? where userId = ?";
		PreparedStatement updateStmt = con.prepareStatement(state);
		updateStmt.setBytes(1, newSalt);
		updateStmt.setBytes(2, newHashPass);
		updateStmt.setString(3, userId);
		updateStmt.execute();
		return true;
	}
	
	/**
	 * update the data of the user
	 * can generally update any piece of data by entering
	 * the different query and value
	 * @param userId
	 * @param query the piece of data you want to change
	 * @param value the value you want to put into
	 * @return
	 * @throws SQLException
	 */
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
	
	/**
	 * check if the user id is already existed,
	 * return true if yes
	 * @param userId
	 * @return
	 */
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
	
	/**
	 * general function to get any line of user in the 
	 * user table
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public ResultSet getUser(String userId) throws SQLException {
		return super.getResult("userId", userId);
	}
	
	/**
	 * check if the password is correct
	 * by take out the salt stored in the database
	 * combine with the password user entered and generate the hashed password
	 * finally compare with the stored hashed password in the database
	 * return true if equal
	 * @param userId
	 * @param password
	 * @return
	 */
	public boolean checkPass(String userId, String password) {
		try {
			ResultSet res = getResult("userId", userId);
			while(res.next()) {
				byte[] salt = res.getBytes("salt");
				byte[] truePass = res.getBytes("hashPass");
				byte[] hashPass = generateFinalPassword(salt, password);
				System.out.println("userId: " + userId);
				System.out.println("salt: " + salt);
				System.out.println("password: " + password);
				System.out.println("truePass: " + truePass);
				System.out.println("hashPass: " + hashPass);
				if(truePass.length != hashPass.length) {
					return false;
				}
				for(int i = 0; i < truePass.length; i++) {
					if(truePass[i] != hashPass[i]) {
						return false;
					}
				}
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}	
	
	/**
	 * generate a new random salt
	 * return it as a string
	 * @return
	 */
	public byte[] generateSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return salt;
	}
	
	/**
	 * take the salt string and password string
	 * hash them together and return the final hashed password as string
	 * @param saltS
	 * @param password
	 * @return
	 */
	public byte[] generateFinalPassword(byte[] salt, String password) {
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
		SecretKeyFactory factory;
		byte[] hash = null;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			hash = factory.generateSecret(spec).getEncoded();
			return hash;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
