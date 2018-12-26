package com.mitrais.rms.service;

import com.mitrais.rms.dao.impl.UserDaoImpl;
import com.mitrais.rms.model.User;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService extends UserDaoImpl implements Service<Map<String, String>, Long> {

    @Override
    public boolean doCheck(Map<String, String> strings) {
        Optional<User> user = find(Long.decode(strings.get("userid")));
        if(user.isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean doInsert(Map<String, String> strings) throws SQLException {
        List<User> listUser = findAll().stream().sorted(Comparator.comparing(User::getId)).collect(Collectors.toList());
        Long newId = listUser.get(0).getId() + 1;

        User user = new User(newId, strings.get("username"), strings.get("userpass"));
        return save(user);
    }

    @Override
    public boolean doUpdate(Map<String, String> strings) throws SQLException {
        Optional<User> user = find(Long.decode(strings.get("userid")));
        if(user.isPresent()) {
            return update(new User(Long.decode(strings.get("userid")), strings.get("username"), strings.get("userpass")));
        }
        return false;
    }

    @Override
    public boolean doDelete(Long id) throws SQLException {
        Optional<User> user = find(id);
        if(user.isPresent()) {
            return delete(user.get());
        }
        return false;
    }

    private static class SingletonHelper
    {
        private static final UserService INSTANCE = new UserService();
    }

    public static UserService getInstance()
    {
        return UserService.SingletonHelper.INSTANCE;
    }
}
