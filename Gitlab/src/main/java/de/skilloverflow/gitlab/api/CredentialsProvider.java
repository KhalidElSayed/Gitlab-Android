package de.skilloverflow.gitlab.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CredentialsProvider {
    private CredentialsProvider() {
    }

    private static final String CREDENTIALS = "credentials";

    /**
     * Saves the URL for accessing the API.
     *
     * @param context A Context for accessing the {@link SharedPreferences}.
     * @param url     The base URL for the Gitlab instance (e.g http://demo.gitlab.com).
     */
    public static void setUrl(Context context, String url) {
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        url += "/api/v3";
        SharedPreferences prefs = context.getSharedPreferences(CREDENTIALS, 0);
        prefs.edit().putString("url", url).apply();
    }

    /**
     * Saves the private token of the user for accessing the API.
     * <p/>
     * NOTICE: Always check the return value, due to a lot of possible exceptions.
     *
     * @param context      A Context for accessing the {@link SharedPreferences}.
     * @param privateToken The private token, acquired with the POST /session call.
     */
    public static Boolean setPrivateToken(Context context, String privateToken) {
        SharedPreferences prefs = context.getSharedPreferences(CREDENTIALS, 0);
        try {
            prefs.edit().putString("privateToken", encrypt(context, privateToken)).apply();
            return true;

        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (InvalidKeySpecException e) {
            return null;
        } catch (BadPaddingException e) {
            return null;
        } catch (IllegalBlockSizeException e) {
            return null;
        } catch (InvalidKeyException e) {
            return null;
        } catch (NoSuchPaddingException e) {
            return null;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * @param context A Context for accessing the {@link SharedPreferences}.
     * @return The URL for API access.
     */
    public static String getUrl(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(CREDENTIALS, 0);
        return prefs.getString("url", "");
    }

    /**
     * NOTICE: Always check the return value, due to a lot of possible exceptions.
     *
     * @param context A Context for accessing the {@link SharedPreferences}.
     * @return The private token for API access.
     */
    public static String getPrivateToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(CREDENTIALS, 0);
        String encrypted = prefs.getString("privateToken", "");
        try {
            return decrypt(context, encrypted);

        } catch (NoSuchPaddingException e) {
            return null;
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (InvalidKeyException e) {
            return null;
        } catch (BadPaddingException e) {
            return null;
        } catch (IllegalBlockSizeException e) {
            return null;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * @return A random generated 256-bit key for encryption.
     * @throws NoSuchAlgorithmException
     */
    private static SecretKey generateKey() throws NoSuchAlgorithmException {
        // Generate a 256-bit key.
        final int outputKeyLength = 256;

        SecureRandom secureRandom = new SecureRandom();
        // Do *not* seed secureRandom! Automatically seeded from system entropy.
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(outputKeyLength, secureRandom);
        return keyGenerator.generateKey();
    }

    /**
     * @param context A Context for accessing the {@link SharedPreferences}.
     * @return A {@link Byte} salt.
     * @throws NoSuchAlgorithmException
     */
    private static byte[] getSalt(Context context) throws NoSuchAlgorithmException {
        SharedPreferences prefs = context.getSharedPreferences(CREDENTIALS, 0);
        String savedKey = prefs.getString("salt", "");
        if (!TextUtils.equals("", savedKey)) {
            return Base64.decode(savedKey, Base64.NO_WRAP);
        }

        byte[] keyBytes = generateKey().getEncoded();
        String key = Base64.encodeToString(keyBytes, Base64.NO_WRAP);
        prefs.edit().putString("salt", key).apply();
        return keyBytes;
    }

    /**
     * @param context A Context for accessing the {@link SharedPreferences}.
     * @return The saved {@link SecretKey}.
     */
    private static SecretKey getSecretKey(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(CREDENTIALS, 0);
        String key = prefs.getString("key", "");
        if (TextUtils.equals("", key)) {
            return null;
        }

        byte[] keyBytes = Base64.decode(key, Base64.NO_WRAP);
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");
    }

    /**
     * @param context A Context for accessing the {@link SharedPreferences}.
     * @return The created {@link SecretKey}.
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static SecretKey createSecretKey(Context context) throws NoSuchAlgorithmException, InvalidKeySpecException {
        char[] pw = "123456".toCharArray();
        byte[] salt = getSalt(context);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(pw, salt, 32768, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

        SharedPreferences prefs = context.getSharedPreferences(CREDENTIALS, 0);
        prefs.edit().putString("key", Base64.encodeToString(secretKey.getEncoded(), Base64.NO_WRAP)).apply();

        return secretKey;
    }

    /**
     * @param context A Context for accessing the {@link SharedPreferences}.
     * @param input   The The String which has to be encrypted.
     * @return The encrypted String
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws UnsupportedEncodingException
     */
    private static String encrypt(Context context, String input) throws NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException,
            IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, UnsupportedEncodingException {
        SecretKey secretKey = getSecretKey(context);
        if (secretKey == null) {
            secretKey = createSecretKey(context);
        }

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return new String(cipher.doFinal(input.getBytes("UTF-8")));
    }

    /**
     * @param context A Context for accessing the {@link SharedPreferences}.
     * @param input   The String which has to be decrypted.
     * @return The decrypted String.
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws UnsupportedEncodingException
     */
    private static String decrypt(Context context, String input) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(context));
        return new String(cipher.doFinal(input.getBytes("UTF-8")), "UTF-8");
    }
}
