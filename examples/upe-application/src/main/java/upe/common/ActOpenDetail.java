package upe.common;

import upe.process.UProcessComponent;

import java.io.Serializable;
import java.util.Map;


public class ActOpenDetail extends ActOpenDetailBase {

    public ActOpenDetail(UProcessComponent parent, String name) {
        super(parent, name);
    }

    public Serializable internalExecute( Map<String, Serializable> args ) {
        MasterProcessComponent master = getParent();
        MasterDetailConfiguration config = master.getMasterDetailConfig();

        getProcess().getProcessEngine()
                .callProcess(config.getDetailProcessName(), args, master.getActReturnFromDetail());
        return Boolean.TRUE;
    }

    public MasterProcessComponent getParent() {
        return (MasterProcessComponent)getProcess().getProcessElement(getElementPath()+"/..");
    }
}
