package real.zani.util;

import java.nio.charset.Charset;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import real.zani.exception.CryptoException;

public class AES256 {

	public static final Charset DEFAULT_CHARSET = StringUtils.DEFAULT_CHARSET;

    public static String encode(String secretKey, String clearText) throws CryptoException {

        byte[] IV = new byte[16];
        try {
            byte[] keyData = secretKey.getBytes(DEFAULT_CHARSET);

            new SecureRandom().nextBytes(IV);

            SecretKey secureKey = new SecretKeySpec(keyData, "AES");

            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(IV));
            byte[] encrypted = c.doFinal(clearText.getBytes("UTF-8"));
            String enStr = BASE64.encode(encrypted);

            String prefix = BASE64.encode(IV).substring(0, 22);
            return prefix + enStr;
        } catch (Exception e) {
            throw new CryptoException(e.getMessage());
        }
    }

    public static String decode(String secretKey, String src) throws CryptoException {
        byte[] IV = new byte[0];
        try {
            IV = BASE64.decodeToBytes(src.substring(0, 22));

            byte[] keyData = secretKey.getBytes(DEFAULT_CHARSET);
            SecretKey secureKey = new SecretKeySpec(keyData, "AES");
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV));

            byte[] byteStr = BASE64.decodeToBytes(src.substring(22));

            return new String(c.doFinal(byteStr), DEFAULT_CHARSET);
        } catch (Exception e) {
            throw new CryptoException(e.getMessage());
        }
    }


    //복호화
    public static String decodeLegacy(String iv, String secretKey, String src) throws CryptoException {
        byte[] IV;
        try {
            IV = iv.getBytes();

            byte[] keyData = secretKey.getBytes(DEFAULT_CHARSET);
            SecretKey secureKey = new SecretKeySpec(keyData, "AES");
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(IV));

            byte[] byteStr = BASE64.decodeToBytes(src);

            return new String(c.doFinal(byteStr), DEFAULT_CHARSET);
        } catch (Exception e) {
            throw new CryptoException(e.getMessage());
        }
    }


}