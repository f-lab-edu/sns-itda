package me.liiot.snsserver.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryptor {

    // hashpw()의 리턴값은 60바이트의 문자열이다.
    public static String encrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean isMatch(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
