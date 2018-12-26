package com.mitrais.rms.service;

import com.mitrais.rms.dao.UserDao;
import com.mitrais.rms.dao.impl.UserDaoImpl;
import com.mitrais.rms.model.User;

import java.util.Map;
import java.util.Optional;

public class LoginService implements Service<Map<String, String>,Integer> {

    private UserDao userDao = UserDaoImpl.getInstance();

    @Override
    public boolean doCheck(Map<String, String> strings) {
        if(strings.get("buttonLogin") != null) {
            String userName = strings.get("username");
            String userPass = strings.get("userpass");
            if((userName != null) && (userPass != null)) {
                Optional<User> user = userDao.findByUserName(userName);
                if((user.isPresent()) && (user.get().getPassword().equals(userPass))) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean doInsert(Map<String, String> stringStringMap) {
        return false;
    }

    @Override
    public boolean doUpdate(Map<String, String> stringStringMap) {
        return false;
    }

    @Override
    public boolean doDelete(Integer integer) {
        return false;
    }

    private static class SingletonHelper
    {
        private static final LoginService INSTANCE = new LoginService();
    }

    public static LoginService getInstance()
    {
        return LoginService.SingletonHelper.INSTANCE;
    }
}
