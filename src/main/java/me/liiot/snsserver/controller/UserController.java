package me.liiot.snsserver.controller;

import me.liiot.snsserver.exception.NotUniqueIdException;
import me.liiot.snsserver.model.User;
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

    private final ResponseEntity RESPONSEOK = new ResponseEntity(HttpStatus.OK);
    private final ResponseEntity RESPONSECREATED = new ResponseEntity(HttpStatus.CREATED);
    private final ResponseEntity RESPONSECONFLICT = new ResponseEntity(HttpStatus.CONFLICT);

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> signUpUser(User user) {

        userService.signUpUser(user);

        return RESPONSECREATED;
    }

    @GetMapping("/{userId}/exists")
    public ResponseEntity<String> checkUserIdDupe(@PathVariable String userId) {

        try {
            userService.checkUserIdDupe(userId);
        } catch (NotUniqueIdException e) {
            return RESPONSECONFLICT;
        }
        return RESPONSEOK;
    }
}
