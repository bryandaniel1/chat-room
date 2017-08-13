import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/* 
 * Copyright 2017 Bryan Daniel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * This class provides convenient utility to determine values for encryption
 * keys and encrypted strings.
 * 
 * @author Bryan Daniel
 *
 */
public class PasswordEncryptor {

	/**
	 * The algorithm used for encryption
	 */
	private static final String ALGORITHM = "AES";

	/**
	 * The key used for encryption
	 */
	private static final String ENCRYPTION_KEY = "16-character key";

	/**
	 * The password in plaintext
	 */
	private static final String PLAINTEXT_PASSWORD = "testpassword";

	/**
	 * The starting point for program execution
	 * 
	 * @param args
	 *            the program arguments
	 */
	public static void main(String[] args) {

		String encryptedPassword;
		String decryptedPassword;
		try {
			encryptedPassword = encrypt(PLAINTEXT_PASSWORD);
			decryptedPassword = decrypt(encryptedPassword);
			System.out.println("Plaintext string: " + PLAINTEXT_PASSWORD);
			System.out.println("Encrypted string : " + encryptedPassword);
			System.out.println("Decrypted string : " + decryptedPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method encrypts the given string.
	 * 
	 * @param plaintextString
	 *            the string to encrypt
	 * @return the encrypted string
	 * @throws Exception
	 */
	public static String encrypt(String plaintextString) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(ENCRYPTION_KEY.getBytes(), ALGORITHM));
		byte[] encryptedBytes = cipher.doFinal(plaintextString.getBytes());
		return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	/**
	 * This method decrypts the given string.
	 * 
	 * @param encryptedString
	 *            the string to decrypt
	 * @return the decrypted string
	 * @throws Exception
	 */
	public static String decrypt(String encryptedString) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(ENCRYPTION_KEY.getBytes(), ALGORITHM));
		byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedString));
		return new String(decryptedBytes);
	}
}
