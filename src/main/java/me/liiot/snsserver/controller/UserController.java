package me.liiot.snsserver.controller;

import me.liiot.snsserver.model.User;
import me.liiot.snsserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUpUser(User user) {

        userService.signUpUser(user);

        return new ResponseEntity<String>("회원가입 완료", HttpStatus.CREATED);
    }

    @GetMapping("/checkIdDupe")
    public ResponseEntity<String> checkIdDupe(@RequestParam String userId) {

        int result = userService.checkIdDupe(userId);

        if (result == 0) {
            return new ResponseEntity<String>("사용 가능한 아이디", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("중복된 아이디", HttpStatus.CONFLICT);
        }
    }
}
