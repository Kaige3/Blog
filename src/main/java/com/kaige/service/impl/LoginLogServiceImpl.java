package com.kaige.service.impl;

import com.kaige.entity.LoginLog;
import com.kaige.service.LoginLogService;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.mutation.SimpleSaveResult;
import org.springframework.stereotype.Service;

@Service
public class LoginLogServiceImpl implements LoginLogService {

    private JSqlClient jSqlClient;
    public LoginLogServiceImpl(JSqlClient jSqlClient) {
        this.jSqlClient = jSqlClient;
    }
    /**
     * 保存登录日志
     * @param log
     */
    @Override
    public void saveLoginLog(LoginLog log) {
        jSqlClient.insert(log);
    }
}
