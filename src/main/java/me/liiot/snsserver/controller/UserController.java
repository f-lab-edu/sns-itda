package me.liiot.snsserver.controller;

import me.liiot.snsserver.annotation.CheckLogin;
import me.liiot.snsserver.annotation.CurrentUser;
import me.liiot.snsserver.exception.FileDeleteException;
import me.liiot.snsserver.exception.FileUploadException;
import me.liiot.snsserver.model.user.*;
import me.liiot.snsserver.service.LoginService;
import me.liiot.snsserver.exception.InvalidValueException;
import me.liiot.snsserver.exception.NotUniqueIdException;
import me.liiot.snsserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static me.liiot.snsserver.util.HttpResponses.*;

/*
@RestController
: @Controller에 @ResponseBody가 추가된 어노테이션
  @RequestMapping 메소드를 처리할 때 @ResponseBody가 기본적으로 붙으면서 처리

@RequestMapping
: 요청 URL과 해당 URL을 처리할 클래스나 메소드에 연결
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    private LoginService loginService;

    @Autowired
    public UserController(UserService userService, LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<Void> signUpUser(UserSignUpParam userSignUpParam) {

        userService.signUpUser(userSignUpParam);
        return RESPONSE_CREATED;
    }

    @GetMapping("/{userId}/exists")
    public ResponseEntity<Void> checkUserIdDupe(@PathVariable String userId) {

        try {
            userService.checkUserIdDupe(userId);
        } catch (NotUniqueIdException e) {
            return RESPONSE_CONFLICT;
        }
        return RESPONSE_OK;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(UserIdAndPassword userIdAndPassword) {

        User user = userService.getLoginUser(userIdAndPassword);

        if (user == null) {
            return RESPONSE_UNAUTHORIZED;
        }

        loginService.loginUser(user);
        return RESPONSE_OK;
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logoutUser() {

        loginService.logoutUser();
        return RESPONSE_OK;
    }

    @PutMapping("/my-account")
    @CheckLogin
    public ResponseEntity<String> updateUser(UserUpdateParam userUpdateParam,
                                             @RequestPart("profileImage") MultipartFile profileImage,
                                             @CurrentUser User currentUser) {

        try {
            User updatedUser = userService.updateUser(currentUser, userUpdateParam, profileImage);
            loginService.loginUser(updatedUser);

            return RESPONSE_OK;
        } catch (FileUploadException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/my-account/password")
    @CheckLogin
    public ResponseEntity<String> updateUserPassword(UserPasswordUpdateParam userPasswordUpdateParam,
                                                     @CurrentUser User currentUser) {

        try {
            userService.updateUserPassword(currentUser, userPasswordUpdateParam);
            loginService.logoutUser();

            return RESPONSE_OK;
        } catch (InvalidValueException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/my-account")
    @CheckLogin
    public ResponseEntity<String> deleteUser(@RequestParam(name="password") String inputPassword,
                                           @CurrentUser User currentUser) {

        try {
            userService.deleteUser(currentUser, inputPassword);
            loginService.logoutUser();

            return RESPONSE_OK;
        } catch (InvalidValueException ive) {
            return RESPONSE_UNAUTHORIZED;
        } catch (FileDeleteException fde) {
            return new ResponseEntity<>(fde.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
