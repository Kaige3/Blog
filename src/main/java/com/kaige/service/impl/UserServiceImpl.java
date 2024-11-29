package com.kaige.service.impl;

import com.kaige.entity.User;
import com.kaige.entity.UserFetcher;
import com.kaige.entity.UserTable;
import com.kaige.service.UserService;
import com.kaige.utils.HashUtils;
import lombok.extern.slf4j.Slf4j;
import org.babyfish.jimmer.sql.JSqlClient;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private JSqlClient jSqlClient;
    public UserServiceImpl(JSqlClient jSqlClient) {
        this.jSqlClient = jSqlClient;
    }
    @Override
    public User findByUsernameAndPassword(String username, String password) {
        UserTable userTable = UserTable.$;
        User user = jSqlClient.createQuery(userTable)
                .where(userTable.username().eq(username))
                .select(userTable.fetch(
                        UserFetcher.$.allTableFields()
                ))
                .fetchOne();
        if (user== null) {
           throw new UsernameNotFoundException("用户名不存在");
        }
        if (!HashUtils.matchBC(password,user.password())){
            throw new UsernameNotFoundException("密码错误");
        }
        return user;
    }
}
