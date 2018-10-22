package util;

import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.util.Strings;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.UUID;

public class CryptoUtils {

    public static String generatePasswordCheckSum(String password, UUID passwordSalt) {
        String passwordWithSaltPrepended = passwordSalt + password;
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(passwordWithSaltPrepended.getBytes("UTF-8"));
            return new String(Hex.encodeHex(messageDigest.digest()));
        } catch (Exception e) {
            e.printStackTrace();

        }
        return Strings.EMPTY;
    }

    public static String generateSecureRandomToken() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }
}
