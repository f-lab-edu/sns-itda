package me.liiot.snsserver.tool;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

/*
@Component
: 해당 클래스가 어노테이션 기반 설정과 classpath 스캐닝을 사용할 때,
  자동으로 감지되어 빈으로 등록될 컴포넌트임을 명시
 */
@Component
public class PasswordEncryptor {

    // hashpw()의 리턴값은 60바이트의 문자열이다.
    public String encrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean match(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
