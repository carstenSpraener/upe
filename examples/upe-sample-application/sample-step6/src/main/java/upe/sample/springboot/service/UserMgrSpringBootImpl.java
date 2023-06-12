package upe.sample.springboot.service;

import org.springframework.stereotype.Component;
import upe.sample.backend.UserMgr;

import java.util.HashSet;
import java.util.Set;

@Component("UserMgrSpringBootImpl")
public class UserMgrSpringBootImpl implements UserMgr {
    private static Set<String> knownUsers = new HashSet<>();

    public UserMgrSpringBootImpl() {
        knownUsers.add("johndoe::password");
    }
    @Override
    public Boolean isLoginCorrect(String user, String password) {
        return knownUsers.contains(user+"::"+password);
    }

    @Override
    public String registerUser(String user, String password) {
        knownUsers.add(user+"::"+password);
        return user;
    }
}
