package me.liiot.snsserver.service;

import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.mapper.FollowMapper;
import me.liiot.snsserver.model.follow.Follow;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowMapper followMapper;

    @Override
    public void addFollowList(String userId, String followUserId) {

        followMapper.insertFollow(userId, followUserId);
    }

    @Override
    public void deleteFollowList(String userId, String followUserId) {

        followMapper.deleteFollow(userId, followUserId);
    }

    @Override
    public List<Follow> getFollowList(String userId) {

        List<Follow> followList = followMapper.getFollowList(userId);

        return followList;
    }

    @Override
    public List<Follow> getFollowingList(String followUserId) {

        List<Follow> followingList = followMapper.getFollowingList(followUserId);

        return followingList;
    }
}
