/**
 * 
 */
package org.teapotech.security.cipher;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;

import org.teapotech.security.cipher.exception.CipherException;

/**
 * @author jiangl
 *
 */
public class AESCipher implements ICipher {

	private final static String CHARSET_UTF8 = "UTF-8";
	private final static String ALGORITHM = "AES/GCM/NoPadding";
	private final SecureRandom secureRandom = new SecureRandom();
	private Key key;

	public static String getCharsetUtf8() {
		return CHARSET_UTF8;
	}

	public AESCipher(Key key) {
		this.key = key;
	}

	@Override
	public final byte[] encrypt(byte[] rawData) throws CipherException {
		byte[] iv = new byte[12];
		secureRandom.nextBytes(iv);
		try {
			final Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));
			byte[] cipherData = cipher.doFinal(rawData);
			ByteBuffer byteBuffer = ByteBuffer.allocate(4 + iv.length + cipherData.length);
			byteBuffer.putInt(iv.length);
			byteBuffer.put(iv);
			byteBuffer.put(cipherData);
			return byteBuffer.array();
		} catch (Exception e) {
			throw new CipherException(e.getMessage(), e);
		}
	}

	@Override
	public final String encrypt(String clearText) throws CipherException {
		byte[] encryptedData = encrypt(clearText.getBytes(Charset.forName(CHARSET_UTF8)));
		return Base64.getEncoder().encodeToString(encryptedData);
	}

	@Override
	public final byte[] decrypt(byte[] encryptedData) throws CipherException {

		ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedData);
		int ivLength = byteBuffer.getInt();
		if (ivLength < 12 || ivLength >= 16) {
			throw new IllegalArgumentException("invalid iv length");
		}
		byte[] iv = new byte[ivLength];
		byteBuffer.get(iv);
		byte[] cipherData = new byte[byteBuffer.remaining()];
		byteBuffer.get(cipherData);
		try {
			final Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, iv));

			return cipher.doFinal(cipherData);
		} catch (Exception e) {
			throw new CipherException(e.getMessage(), e);
		}
	}

	@Override
	public final String decrypt(String encryptedText) throws CipherException {
		byte[] decryptedData = decrypt(Base64.getDecoder().decode(encryptedText));
		try {
			return new String(decryptedData, CHARSET_UTF8);
		} catch (Exception e) {
			throw new CipherException(e.getMessage(), e);
		}
	}

}
