package gm.shared.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//import org.apache.log4j.Logger;

/**
 * Helps hashing strings
 * 
 * @author voidStern
 * @author stefan
 */
public class HashMaker {
//	static Logger l = Logger.getLogger(HashMaker.class);
	
	public static String md5(String clearText){

		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
//			l.warn(e.getMessage());
		}
		md5.reset();
		md5.update(clearText.getBytes());
		
		byte[] result = md5.digest(); /* Ausgabe */
		
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			hexString.append(Integer.toHexString(0xFF & result[i]));
		}
		return hexString.toString();
	}
	
	public static String sha1(String clearText){

		MessageDigest sha1 = null;
		try {
			sha1 = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
//			l.warn(e.getMessage());
		}
		sha1.reset();
		sha1.update(clearText.getBytes());
		
		byte[] result = sha1.digest(); /* Ausgabe */
		
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			hexString.append(Integer.toHexString(0xFF & result[i]));
		}
		return hexString.toString();
	}
}
