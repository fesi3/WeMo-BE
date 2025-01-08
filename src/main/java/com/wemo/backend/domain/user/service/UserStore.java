package com.wemo.backend.domain.user.service;

import com.wemo.backend.domain.user.entity.User;

public interface UserStore {

    void store(User initUser);

}
