package upe.sample.backend;

import upe.annotations.UpeBackendFacade;

@UpeBackendFacade(UserMgr.NAME)
public interface UserMgr {
    String NAME = "userMgr";

    Boolean isLoginCorrect(String user, String password);
}
