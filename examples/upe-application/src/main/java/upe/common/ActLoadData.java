package upe.common;

import upe.process.UProcessComponent;
import upe.process.UProcessComponentList;
import upe.process.impl.UProcessComponentImpl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public class ActLoadData extends ActLoadDataBase {

    public ActLoadData(UProcessComponent parent, String name) {
        super(parent, name);
    }

    public Serializable internalExecute( Map<String, Serializable> args ) {
        MasterProcessComponent master = (MasterProcessComponent)getParent();
        MasterDetailConfiguration config = master.getMasterDetailConfig();
        UProcessComponentList<?> list = (UProcessComponentList<?>)master
                .getProcess()
                .getProcessElement(config.getMasterListPath());
        List<?> dataList = master.loadData();

        for( int idx = 0; idx<dataList.size(); idx++ ) {
            UProcessComponent uc = null;
            if( idx<list.size() ) {
                uc = list.getAt(idx);
            } else {
                uc = master.createInstance();
            }
            Object data = dataList.get(idx);
            ((UProcessComponentImpl)uc).mapFromScaffolded(data.getClass(), data);
        }
        return Boolean.TRUE;
    }
}
