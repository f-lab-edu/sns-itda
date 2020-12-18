package me.liiot.snsserver.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.sql.Date;

/*
@NotBlank
: 지정된 필드 값이 비어있는지 확인

@Pattern
: 지정된 필드 값이 사용자가 정의한 형식인지 확인

@Email
: 지정된 필드 값이 이메일 형식인지 확인
 */
@Getter
@Builder
@AllArgsConstructor
public class UserSignUpParam {

    @NotBlank(message = "아이디를 입력해주세요.")
    private final String userId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private final String password;

    @NotBlank(message = "이름을 입력해주세요.")
    private final String name;

    @NotBlank(message = "핸드폰 번호를 입력해주세요.")
    @Pattern(regexp = "[0-9]{10,11}", message = "핸드폰 번호는 10~11자리의 숫자만 입력해주세요.")
    private final String phoneNumber;

    @Email(message = "이메일 형식에 맞춰 입력해주세요.")
    private final String email;

    private final Date birth;
}
