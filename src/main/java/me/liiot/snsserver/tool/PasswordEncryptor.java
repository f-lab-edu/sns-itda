package me.liiot.snsserver.tool;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptor {

    public String encrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean match(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
