package me.liiot.snsserver.service;

import lombok.RequiredArgsConstructor;
import me.liiot.snsserver.mapper.FollowMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowMapper followMapper;

    @Override
    public void addFollowList(String userId, String targetId) {

        followMapper.insertFollow(userId, targetId);
    }

    @Override
    public void deleteFollowList(String userId, String targetId) {

        followMapper.deleteFollow(userId, targetId);
    }
}
