package upe.sample.lanterna.backend;

import upe.annotations.UpeBackendFacade;
import upe.sample.backend.UserMgr;

import java.util.HashSet;
import java.util.Set;

public class UserMgrLanternaImpl implements UserMgr {
    private static Set<String> knownUsers = new HashSet<>();

    public UserMgrLanternaImpl() {
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
