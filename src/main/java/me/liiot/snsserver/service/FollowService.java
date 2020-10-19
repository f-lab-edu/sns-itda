package me.liiot.snsserver.service;

import me.liiot.snsserver.model.follow.Follow;

import java.util.List;

public interface FollowService {

    public void addFollowList(String userId, String targetId);

    public void deleteFollowList(String userId, String targetId);

    public List<Follow> getFollowList(String userId);

    public List<Follow> getFollowingList(String followUserId);
}
