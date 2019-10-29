package org.teapotech.security.cipher;

import org.teapotech.security.cipher.exception.CipherException;

public interface ICipher {

	byte[] encrypt(byte[] rawData) throws CipherException;

	String encrypt(String clearText) throws CipherException;

	byte[] decrypt(byte[] encryptedData) throws CipherException;

	String decrypt(String encryptedText) throws CipherException;
}
