package security;

import org.apache.commons.lang.RandomStringUtils;


public class Criptografia {

    public static String hashearContrasenia(String contrasenia, String salt) {
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(contrasenia + salt);
    }

    public static String generateSalt() {
        return RandomStringUtils.randomAscii(20);
    }
}
