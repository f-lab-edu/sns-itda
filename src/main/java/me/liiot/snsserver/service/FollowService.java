package me.liiot.snsserver.service;

public interface FollowService {

    public void addFollowList(String userId, String followUserId);

    public void deleteFollowList(String userId, String followUserId);
}
