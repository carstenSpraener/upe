package upe.process.impl;

import upe.process.UProcessComponent;


import java.util.Map;
import java.util.function.Function;

public class UMethodUProcessAction extends AbstractUActionImpl {
    private transient Function<Map<String, Object>, Object> actionMethod;

    public UMethodUProcessAction(UProcessComponent parent, String name, Function<Map<String,Object>, Object> actionMethod) {
        super(parent, name);
        this.actionMethod = actionMethod;
    }

    @Override
    public Object internalExecute(Map<String, Object> args) {
        return actionMethod.apply(args);
    }
}
