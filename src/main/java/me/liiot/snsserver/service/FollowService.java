package me.liiot.snsserver.service;

import me.liiot.snsserver.model.follow.Follow;

import java.util.List;

public interface FollowService {

    public void addFollowList(String userId, String followUserId);

    public void deleteFollowList(String userId, String followUserId);

    public List<Follow> getFollowList(String userId);

    public List<Follow> getFollowingList(String followUserId);
}
