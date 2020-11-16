package me.liiot.snsserver.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class UserPasswordUpdateParam {

    @NotBlank(message = "기존 비밀번호를 입력해주세요.")
    private final String existPassword;

    @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
    private final String newPassword;

    @NotBlank(message = "새로운 비밀번호를 한 번 더 입력해주세요.")
    private final String checkNewPassword;
}
