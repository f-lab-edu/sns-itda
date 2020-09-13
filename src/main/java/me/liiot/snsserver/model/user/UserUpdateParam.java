package me.liiot.snsserver.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

@Getter
@Builder
@AllArgsConstructor
public class UserUpdateParam {

    private final String name;

    private final String phoneNumber;

    private final String email;

    private final Date birth;

    private final String profileMessage;
}
