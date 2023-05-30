package upe.common;

import upe.process.UProcessComponent;

import java.io.Serializable;
import java.util.Map;


public class ActReturnFromDetail extends ActReturnFromDetailBase {

    public ActReturnFromDetail(UProcessComponent parent, String name) {
        super(parent, name);
    }

    public Serializable internalExecute( Map<String, Serializable> args ) {
        ((MasterProcessComponent)getParent()).getActLoadData().internalExecute(args);
        return Boolean.TRUE;
    }
}
