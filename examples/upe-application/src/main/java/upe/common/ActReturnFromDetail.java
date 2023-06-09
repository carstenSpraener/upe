package upe.common;

import upe.process.UProcessComponent;


import java.util.Map;


public class ActReturnFromDetail extends ActReturnFromDetailBase {

    public ActReturnFromDetail(UProcessComponent parent, String name) {
        super(parent, name);
    }

    public Object internalExecute( Map<String, Object> args ) {
        ((MasterProcessComponent)getParent()).getActLoadData().internalExecute(args);
        return Boolean.TRUE;
    }
}
