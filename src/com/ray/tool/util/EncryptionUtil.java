package com.ray.tool.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class EncryptionUtil {
	public static String KEY_VAULE = "tw7946WApskl081";//
	private static final String ENCRYPTION_ALGORITHM_DES = "DES";// DES/ECB/PKCS5Padding for SunJCE
//	private static String KEY_VAULE = "0123456789";//at least 8 characters for DES
	private static SecretKey desKey;
	
	static{
		try{
			resetDesKey(KEY_VAULE);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	/**
	 * @param keyValue 大于等于8个字符
	 * @throws Exception
	 */
	public static void resetDesKey(String keyValue) throws Exception {
		DESKeySpec dks = new DESKeySpec(keyValue.getBytes());
		SecretKeyFactory skf = SecretKeyFactory.getInstance(ENCRYPTION_ALGORITHM_DES);
		desKey = skf.generateSecret(dks);
	}

	public static byte[] encryptDES(byte[] bytes) throws Exception {
		Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM_DES); // DES/ECB/PKCS5Padding for SunJCE

		cipher.init(Cipher.ENCRYPT_MODE, desKey);
		return cipher.doFinal(bytes);
	}

	public static String encryptDESToBase64(byte[] bytes) throws Exception {
		return new String(Base64.encodeBase64(encryptDES(bytes), false));
	}

	public static String encryptDESToHex(byte[] bytes) throws Exception {
		return new String(Hex.encodeHex(encryptDES(bytes)));
	}

	public static byte[] decryptDES(byte[] encryptedBytes) throws Exception {
		Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM_DES);
		cipher.init(Cipher.DECRYPT_MODE, desKey);
		return cipher.doFinal(encryptedBytes);
	}

	public static String decryptDESFromBase64(byte[] bytes) throws Exception {
		return new String(decryptDES(Base64.decodeBase64(bytes)));
	}

	public static String decryptDESFromHex(char[] hexChars) throws Exception {
		return new String(decryptDES(Hex.decodeHex(hexChars)));
	}

	public static String hashMD5(String plainText) throws Exception {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		byte[] digestedBytes = messageDigest
				.digest(plainText.getBytes("UTF-8"));
		BigInteger number = new BigInteger(1, digestedBytes);
		String result = number.toString(16);
		while(result.length() < 32) { //40 for SHA-1
            result = "0" + result;
        }
		return result;
	}

	public static String hashMD5Base64(String text) throws Exception {
		return new String(Base64.encodeBase64(hashMD5(text).getBytes(), false));
	}

	public static String encryptMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
	public static String MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	public static String xor(String inStr, char[] key) {
		// String s = new String(inStr);
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ key[0]);
		}
		String s = new String(a);
		return s;
	}

	public static void main(String[] args) throws Exception {
//		String source = "aaaaa222222222211111154jk", dest = "1a456a48bab7e9a25461290ca20071acc670c607401cd3ff1c2ada225b4e61de";
//		String destBase64 = "GkVqSLq36aJUYSkMogBxrMZwxgdAHNP/HCraIltOYd4=";
//		 System.out.println(encryptDESToHex(source.getBytes()));
//		 System.out.println(decryptDESFromHex(dest.toCharArray()));
//		 System.out.println(encryptDESToBase64(source.getBytes()));
//		 System.out.println(decryptDESFromBase64(destBase64.getBytes()));
		
		
		String s = new String("12345");
		System.out.println("原始：" + s);
		System.out.println("MD5后：" + encryptMD5(s));
		System.out.println("MD5后(Base64)：" + hashMD5Base64(s));
		System.out.println("MD5后再xor：" + xor(encryptMD5(s), new char[]{'f', 'u'}));
		
		String source = "{\n "+
  "\"channelSdkClass\": \"com.sqwan.msdk.SQwanCore\","+
  "\"channelAdapterClass\": \"com.cmge.cge.sdk.channel.Channel37\","+
  "\"channelParams\": {"+
    "\"appKey\": \"cC9PS5AOpUt87KreYunDH&d$whJFEyRm\","+
    "\"currencyName\": \"钻石\""+
  "}"+
"}";
		String dest = new String(Base64.encodeBase64(source.getBytes()));
		System.out.println(dest);
	}
}