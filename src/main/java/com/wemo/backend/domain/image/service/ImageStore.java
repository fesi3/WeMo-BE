package com.wemo.backend.domain.image.service;

import com.wemo.backend.domain.user.entity.User;

public interface ImageStore {

    void storeMeetingImage(User user, Long meetingId, String fileUrl);

}
