package me.liiot.snsserver.service;

import me.liiot.snsserver.model.user.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    public void uploadPost(User user, String content, List<MultipartFile> images);
}
