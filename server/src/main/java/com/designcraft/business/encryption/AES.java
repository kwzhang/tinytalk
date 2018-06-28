package com.designcraft.business.encryption;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {

	private static SecretKeySpec secretKey;
	private static byte[] key;
	private static final String KEY = "designcraft12#$";

	public static void setKey(String myKey) throws UnsupportedEncodingException, NoSuchAlgorithmException 
	{
		MessageDigest sha = null;
		key = myKey.getBytes("UTF-8");
		sha = MessageDigest.getInstance("SHA-1");
		key = sha.digest(key);
		key = Arrays.copyOf(key, 16); 
		secretKey = new SecretKeySpec(key, "AES");
	}

	public static String encrypt(String strToEncrypt) throws Exception
	{
		setKey(KEY);
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
	}

	public static String decrypt(String strToDecrypt)  throws Exception
	{
		setKey(KEY);
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
	}

	public static void main(String[] args) throws Exception {
		String org = "test123#$";
		String enc = AES.encrypt(org);
		enc = "gzSq/cNy5w5c1trut5xpJg==";
		System.out.println(enc);
		System.out.println(AES.decrypt(enc));
	}
}
