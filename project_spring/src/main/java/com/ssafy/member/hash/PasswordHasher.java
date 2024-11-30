package com.ssafy.member.hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class PasswordHasher {
	private final static String SECRET_KEY = "mySuperSecretKey1234567890123456";
	private final static String ALGORITHM = "AES";
	
//	public String changeToHash(String password) throws NoSuchAlgorithmException {
//		MessageDigest digest = MessageDigest.getInstance("SHA-256");
//		byte[] hash = digest.digest(password.getBytes());
//		
//		StringBuilder hexString = new StringBuilder();
//		 for (byte b : hash) {
//           String hex = Integer.toHexString(0xff & b);
//           if (hex.length() == 1) hexString.append('0');
//           hexString.append(hex);
//       }
//		 return hexString.toString();
//	}
	
//	public boolean comparePassword(String inputPassword , String savePassword) {
//		return inputPassword.equals(savePassword);
//	}

	//암호화
	public String changeToHash(String password) throws Exception {
		SecretKeySpec keySpec = generateKey();
		Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        byte[] encryptedBytes = cipher.doFinal(password.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
	}
	
	public boolean comparePassword(String inputPassword , String savePassword) throws Exception {
//		String decrypt = decrypt(savePassword);
		return inputPassword.equals(savePassword);
	}
	
	
	
	
	//암호화
	public String encrypt(String plainText) throws Exception {
		SecretKeySpec keySpec = generateKey();
		Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
	}
	
	//복호화
	public String decrypt(String encryptedText) throws Exception {
		SecretKeySpec keySpec = generateKey();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

	private static SecretKeySpec generateKey() throws Exception {
        byte[] key = SECRET_KEY.getBytes("UTF-8");
        return new SecretKeySpec(key, ALGORITHM);
    }
	
	
}
