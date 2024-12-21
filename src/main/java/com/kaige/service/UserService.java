package com.kaige.service;

import com.kaige.entity.User;

public interface UserService {
    User findByUsernameAndPassword(String username, String password);

    User findByUsernameAndpassword(String username);
}
