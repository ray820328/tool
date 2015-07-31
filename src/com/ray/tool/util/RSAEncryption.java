package com.ray.tool.util;
/**
 * 
 * 
 * @author Ray
 *
 */
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

public class RSAEncryption {

	public static final String padding_mode = "RSA/None/PKCS1Padding";
	private static final String publicKeyBase64 = 
			"MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIaFf2jAg+vUdhIH5DgQMJ05leHmyva2wQ9P/UUGNRCxf9Oa6RmzWHSeTEmkq64ld4oUMOQSZbU2EcSjgm2uB6ECAwEAAQ==";
	private static PublicKey publicKey = null;
	private static Cipher cipher = null;
	
	static{
		try{
			cipher = Cipher.getInstance(padding_mode, RSADecryptUtil.BC);
			cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * encrypt the plain content into RSA result
	 * the result is encoded as Base64
	 * 
	 * @param source
	 * @return @param source not null
	 * @throws Exception
	 */
	public static final String encryptAsBase64(String source) throws Exception{
		if(source == null){
			return null;
		}
		
		return new String(Base64.encode((encrypt(source.getBytes()))));
	}
	
	public static final String encryptAsHex(String source) throws Exception{
		if(source == null){
			return null;
		}
		
		return new String(Hex.encode((encrypt(source.getBytes()))));
	}
	
	/**
	 * encrypt the plain content into RSA result
	 * 
	 * @param source not null
	 * @return
	 */
	public static final byte[] encrypt(byte[] source) throws Exception{
		if(source == null){
			return null;
		}
		
		byte[] encryptedSource = null;
		try {
//			action
			encryptedSource = cipher.doFinal(source);
		} catch (Exception e) {
			throw e;
		}

		return encryptedSource;
	}
	
	/**
	 * get the RSA public key
	 * 
	 * @return
	 * @throws Exception
	 */
	private static final synchronized PublicKey getPublicKey() throws Exception{
		if(publicKey == null){
			KeyFactory keyf = KeyFactory.getInstance("RSA", RSADecryptUtil.BC);
			X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(Base64.decode(publicKeyBase64));
			publicKey = keyf.generatePublic(pubX509);
		}
		
		return publicKey;
	}
}
