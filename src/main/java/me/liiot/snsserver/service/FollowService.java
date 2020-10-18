package me.liiot.snsserver.service;

public interface FollowService {

    public void addFollowList(String userId, String targetId);

    public void deleteFollowList(String userId, String targetId);
}
