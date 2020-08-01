package me.liiot.snsserver;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;

/*
@Test
: 해당 메소드를 테스트 대상으로 지정
*/
public class MySQLConnectionTests {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/itda?autoreconnect=true&serverTimezone=Asia/Seoul&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @Test
    public void connectionTest() throws Exception {
        Class.forName(DRIVER);
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
