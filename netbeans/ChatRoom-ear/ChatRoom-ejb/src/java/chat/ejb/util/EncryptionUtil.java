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
package chat.ejb.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * This utility class provides methods for encryption, decryption, and hashing
 * of character strings.
 *
 * @author Bryan Daniel
 */
public class EncryptionUtil {

    /**
     * The value of the encryption key is a property set in GlassFish System
     * Properties with this key.
     */
    private static final String ENCRYPTION_KEY = "chatroom-encryption-key";

    /**
     * The algorithm used for encryption and decryption
     */
    private static final String ALGORITHM = "AES";

    /**
     * The algorithm used for hashing
     */
    private static final String HASHING_ALGORITHM = "SHA-256";

    /**
     * This method retrieves the encryption key from system properties,
     * initializes a cipher for encryption, and encrypts the given string.
     *
     * @param Data the string to encrypt
     * @return the encrypted string or null if an exception occurs
     */
    public static String encrypt(String Data) {

        try {
            Key encryptionKey = new SecretKeySpec(System.getProperty(ENCRYPTION_KEY).getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);
            byte[] encryptedValue = cipher.doFinal(Data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedValue);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(EncryptionUtil.class.getName()).log(Level.SEVERE,
                    "An exception occurred in the encrypt method.", ex);
        }
        return null;
    }

    /**
     * This method retrieves the encryption key from system properties,
     * initializes a cipher for decryption, and decrypts the given string.
     *
     * @param encryptedData the string to decrypt
     * @return the decrypted string or null if an exception occurs
     */
    public static String decrypt(String encryptedData) {

        try {
            Key encryptionKey = new SecretKeySpec(System.getProperty(ENCRYPTION_KEY).getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, encryptionKey);
            byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedValue = cipher.doFinal(decodedValue);
            return new String(decryptedValue);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(EncryptionUtil.class.getName()).log(Level.SEVERE,
                    "An exception occurred in the decrypt method.", ex);
        }
        return null;
    }

    /**
     * This method hashes a given string and returns the hash. This process is
     * used to create a unique code to identify data associated with the given
     * string. The purpose of this method is not to secure sensitive information
     * as no salt is added to the given string.
     *
     * @param string the string to hash
     * @return the hashed string
     * @throws NoSuchAlgorithmException if no Provider supports a
     * MessageDigestSpi implementation for the specified algorithm
     */
    public static String simpleHash(String string)
            throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(HASHING_ALGORITHM);

        //The update method specifies the array of bytes to hash.
        messageDigest.update(string.getBytes());

        //The digest method hashes the array.
        byte[] messageDigestArray = messageDigest.digest();

        //The array of bytes is converted to a string.
        //Each byte is represented by 2 hexadecimal characters.
        StringBuilder stringBuilder
                = new StringBuilder(messageDigestArray.length * 2);
        for (byte b : messageDigestArray) {

            //The bitwise AND operation discards all but the 
            //lowest 8 bits.
            int i = b & 0xff;
            if (i < 16) {
                stringBuilder.append('0');
            }
            stringBuilder.append(Integer.toHexString(i));
        }
        return stringBuilder.toString();
    }
}
