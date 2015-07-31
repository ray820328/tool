package com.ray.tool.util;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class SHA1Util {
	// We will have the same iv and key
	public static String key = "ufj93nF9g";
	// This is what will be cut out 16 Bytes, 128bit
	public static String keySha1 = "299296c3b3752f9acf237acfb33d53bb";// "eb96eae2";
	public static String iv = "299296c3b3752f9acf237acfb33d53bb";
	private static byte[] keyBytes;
	private static byte[] ivBytes;
	private static IvParameterSpec ivSpec;

	private static final String ENCODE = "UTF-8";
	private static MessageDigest sha1MD;

	public static byte[] sha1(String text) {
		if (null == sha1MD) {
			try {
				sha1MD = MessageDigest.getInstance("SHA-1");
			} catch (NoSuchAlgorithmException e) {
				return null;
			}
		}
		try {
			sha1MD.update(text.getBytes(ENCODE), 0, text.length());
		} catch (UnsupportedEncodingException e) {
			sha1MD.update(text.getBytes(), 0, text.length());
		}
		return sha1MD.digest();
	}

	/**
	 * Encrypt AES with CBC and PKCS5
	 * 
	 * @param Data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptAES(byte[] data) throws Exception {
		if (keyBytes == null) {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			keyBytes = sha.digest(key.getBytes(ENCODE));
			keyBytes = Arrays.copyOf(keyBytes, 16); // use only first 128 bit
			ivBytes = Arrays.copyOf(keyBytes, 16);
			ivSpec = new IvParameterSpec(ivBytes);
		}

		// System.out.print(Hex.encodeHexString(keyBytes)+"\n");
		Key key = new SecretKeySpec(keyBytes, "AES");
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		byte[] encVal = c.doFinal(data);
//		String encryptedValue = new String(Hex.encodeHex(encVal));// new
		return encVal;
	}

	public static byte[] decryptAES(byte[] data) throws Exception {
		if (keyBytes == null) {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			keyBytes = sha.digest(key.getBytes(ENCODE));
			keyBytes = Arrays.copyOf(keyBytes, 16); // use only first 128 bit
			ivBytes = Arrays.copyOf(keyBytes, 16);
			ivSpec = new IvParameterSpec(ivBytes);
		}

		byte[] sourceValue = data;
		Key key = new SecretKeySpec(keyBytes, "AES");
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, key, ivSpec);
		byte[] decodeVal = c.doFinal(sourceValue);

		return decodeVal;
	}

	/**
	 * Takes arguments in order of: Mode, Original Excel File Name, Output Excel
	 * File Name
	 * 
	 * If Mode 1, Takes in Crypted File, Rows to Decrypt If Mode 2, Takes in
	 * Original Excel File, Crypts File and Output to Encrypted Excel File Name
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// 编码
			String source = "G1A0000000001奋斗";
			byte[] dest = encryptAES(("Install McDxHELLO KITTY App to scan code. "
					+ source).getBytes(StringUtil.charset_ascii.name()));
			System.out.println("source = " + source + "; dest = " + 
					new String(dest, StringUtil.charset_ascii.name()));

			// 解码
//			dest = "5b72f8f05941c465c2edd714132afda4";
			source = new String(decryptAES(dest), 
					StringUtil.charset_default.name());
			System.out.println("dest = " + dest + "; source = " + source);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Kitty Encrypter: Mode is invalid.");
			System.exit(1);
		}
	}

}
