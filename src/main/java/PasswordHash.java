import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHash {

	public static String getSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return new String(salt);
	}
	
	public static String getFinalPassword(String salt, String password) {
		byte[] sb = salt.getBytes();
		KeySpec spec = new PBEKeySpec(password.toCharArray(), sb, 65536, 128);
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
