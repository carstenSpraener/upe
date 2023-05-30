package upe.common;

import upe.process.UProcessComponent;
import upe.process.UProcessComponentList;
import upe.process.impl.UProcessComponentImpl;

import java.util.ArrayList;
import java.util.List;

public class MasterProcessComponent extends MasterProcessComponentBase {

    private MasterDetailConfiguration masterDetailConfig;

    public MasterProcessComponent(UProcessComponent parent, String name) {
        super(parent, name);
    }

    public void init(MasterDetailConfiguration config) {
        this.masterDetailConfig = config;
    }

    public MasterDetailConfiguration getMasterDetailConfig() {
        return this.masterDetailConfig;
    }

    public List<?> loadData() {
        return this.masterDetailConfig.getDataSupplier().get();
    }

    public UProcessComponent createInstance() {
        UProcessComponentList<?> list = (UProcessComponentList<?>)getProcess().getProcessElement(masterDetailConfig.getMasterListPath());
        return list.createNewInstance();
    }
}
