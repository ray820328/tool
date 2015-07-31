package com.ray.tool.util;
/**
 * 
 * @author Ray
 *
 */

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

public class RSADecryptUtil {

	public static final BouncyCastleProvider BC = new BouncyCastleProvider();
	private static final String privateKeyBase64 = 
			"MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAhoV/aMCD69R2EgfkOBAwnTmV4ebK9rbBD0" +
			"/9RQY1ELF/05rpGbNYdJ5MSaSrriV3ihQw5BJltTYRxKOCba4HoQIDAQABAkAxkPcFTgpN1xc6RZYd4jlH" +
			"dXR4rGWUynoQflTAHi1nXLcKVhkCb7iMQJcfoC3kZy1Eux4mC2jSgBq2qF81lAXBAiEAvLzHH9LSc+1LRk" +
			"5WYGcOXVfpXPOGSQZRZi1SCTBHV+0CIQC2dmV/UQxIsJ1onSGgFPS8hiAce4jxlEud/f2pQ6SQBQIhAJW7" +
			"ipojrlTMKBDx6zL17358VSYqCRn8Ci3uvIuPoroNAiAtii1Y8nyTY+lVo+oxjylqKDu5gCe0y+N3ol3+Mh" +
			"DPlQIgZ7WV/KWLaZC1AVk+OFunbJhVEhcPckOV52skO28r5x8=";
	private static PrivateKey privateKey = null;
	private static Cipher cipher = null;
	
	static{
		try{
			privateKey = getPrivateKey();
			cipher = Cipher.getInstance(RSAEncryption.padding_mode, BC);   
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * encrypt the plain content into RSA result
	 * @param raw not null
	 * @return
	 * RSA/None/PKCS1Padding
	 * RSA/None/NoPadding
	 */
	public static final byte[] decrypt(byte[] raw) throws Exception{
		if (privateKey != null) {   
            try { 
                return cipher.doFinal(raw);   
            } catch (Exception e) {   
                e.printStackTrace();   
            }   
        }
		return null;
	}
	
	/**
	 * get the RSA private key
	 * @return
	 * @throws Exception
	 */
	private static final synchronized PrivateKey getPrivateKey() throws Exception{
		if(privateKey == null){
			KeyFactory keyf = KeyFactory.getInstance("RSA", BC);
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKeyBase64.getBytes()));
			privateKey = keyf.generatePrivate(priPKCS8);
		}
		
		return privateKey;
	}
	
	public static void main(String[] args){
		try{
			String source = "swsw10010998oioioiupo9";
//			byte[] encryptSource = RSAEncryption.encrypt(source.getBytes());
//			String destBase64 = new String(Base64.encode(encryptSource));
			String destBase64 = RSAEncryption.encryptAsBase64(source);
			String decryptSource = new String(decrypt(Base64.decode(destBase64)));
//			String destBase64 = RSAEncryption.encryptAsHex(source);
//			String decryptSource = new String(decrypt(Hex.decodeHex(destBase64.toCharArray())));
			
			System.out.println("source: " + source);
			System.out.println("destBase64: " + destBase64);
			System.out.println("decryptSource: " + decryptSource);
			
//			String decryptSource = new String(decrypt(Base64.decode(
//			"FYaIB/MlVok7Qe2iNKnX0t61y6fm/U8yCqYg4zZRYz08rtgHaBMa2N9t5XIiUzqwWCYrx8pDQ8CyCkVh4pVBEA==")));
////			String decryptSource = new String(decrypt(Base64.decode(
////			"PaEKINhiJgxKqLjZOo8cBSwpP83fOxwtmKZeeS9ZRfScG68QEGYvi8kTjxs11z4SLbZVYNOsMSwhbYQRovZPBA==")));
//			System.out.println("decryptSource: " + decryptSource);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}