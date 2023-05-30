package upe.common;

import upe.process.UProcessComponent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class ActCreateDetail extends ActCreateDetailBase {

    public ActCreateDetail(UProcessComponent parent, String name) {
        super(parent, name);
    }

    public Serializable internalExecute( Map<String, Serializable> args ) {
        MasterProcessComponent master = (MasterProcessComponent) getParent();
        MasterDetailConfiguration config = master.getMasterDetailConfig();

        getProcess().getProcessEngine().callProcess(
                config.getDetailProcessName(),
                new HashMap<>(),
                master.getActReturnFromDetail()
        );
        return Boolean.TRUE;
    }
}
