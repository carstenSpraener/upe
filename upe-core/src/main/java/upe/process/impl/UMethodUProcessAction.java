package upe.process.impl;

import upe.process.UProcessComponent;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Function;

public class UMethodUProcessAction extends AbstractUActionImpl {
    private transient Function<Map<String, Serializable>, Serializable> actionMethod;

    public UMethodUProcessAction(UProcessComponent parent, String name, Function<Map<String,Serializable>, Serializable> actionMethod) {
        super(parent, name);
        this.actionMethod = actionMethod;
    }

    @Override
    public Serializable internalExecute(Map<String, Serializable> args) {
        return actionMethod.apply(args);
    }
}
