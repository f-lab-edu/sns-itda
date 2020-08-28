package me.liiot.snsserver.controller;

import me.liiot.snsserver.annotation.CheckLogin;
import me.liiot.snsserver.annotation.CurrentUser;
import me.liiot.snsserver.service.LoginService;
import me.liiot.snsserver.exception.InValidValueException;
import me.liiot.snsserver.exception.NotUniqueIdException;
import me.liiot.snsserver.model.User;
import me.liiot.snsserver.model.UserIdAndPassword;
import me.liiot.snsserver.model.UserPasswordUpdateParam;
import me.liiot.snsserver.model.UserUpdateParam;
import me.liiot.snsserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    private static final ResponseEntity RESPONSE_OK = new ResponseEntity(HttpStatus.OK);
    private static final ResponseEntity RESPONSE_CREATED = new ResponseEntity(HttpStatus.CREATED);
    private static final ResponseEntity RESPONSE_CONFLICT = new ResponseEntity(HttpStatus.CONFLICT);
    private static final ResponseEntity RESPONSE_UNAUTHORIZED = new ResponseEntity(HttpStatus.UNAUTHORIZED);

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<Void> signUpUser(User user) {

        userService.signUpUser(user);
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
    public ResponseEntity<Void> updateUser(UserUpdateParam userUpdateParam,
                                           @CurrentUser User currentUser) {

        userService.updateUser(currentUser.getUserId(), userUpdateParam);
        return RESPONSE_OK;
    }

    @PutMapping("/my-account/password")
    @CheckLogin
    public ResponseEntity<String> updateUserPassword(UserPasswordUpdateParam userPasswordUpdateParam,
                                                     @CurrentUser User currentUser) {

        try {
            userService.updateUserPassword(currentUser, userPasswordUpdateParam);
            loginService.logoutUser();

            return RESPONSE_OK;
        } catch (InValidValueException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/my-account")
    @CheckLogin
    public ResponseEntity<Void> deleteUser(@RequestParam(name="password") String inputPassword,
                                           @CurrentUser User currentUser) {

        try {
            userService.deleteUser(currentUser, inputPassword);
            loginService.logoutUser();

            return RESPONSE_OK;
        } catch (InValidValueException e) {
            return RESPONSE_UNAUTHORIZED;
        }
    }
}
