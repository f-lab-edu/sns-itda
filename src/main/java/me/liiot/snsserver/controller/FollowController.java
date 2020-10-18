package me.liiot.snsserver.controller;

import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.annotation.CheckLogin;
import me.liiot.snsserver.annotation.CurrentUser;
import me.liiot.snsserver.enumeration.AlarmType;
import me.liiot.snsserver.model.user.User;
import me.liiot.snsserver.service.AlarmService;
import me.liiot.snsserver.service.FollowService;
import me.liiot.snsserver.util.HttpResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;

    private final AlarmService alarmService;

    @PostMapping("/{targetId}")
    @CheckLogin
    public ResponseEntity<Void> followUser(@PathVariable String targetId,
                                           @CurrentUser User currentUser) {

        followService.addFollowList(currentUser.getUserId(), targetId);
        alarmService.addAlarm(currentUser.getUserId(), targetId, AlarmType.FOLLOWING);

        return HttpResponses.RESPONSE_CREATED;
    }

    @DeleteMapping("/{targetId}")
    @CheckLogin
    public ResponseEntity<Void> unfollowUser(@PathVariable String targetId,
                                             @CurrentUser User currentUser) {

        followService.deleteFollowList(currentUser.getUserId(), targetId);

        return HttpResponses.RESPONSE_OK;
    }
}
