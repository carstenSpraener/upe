package upe.process.impl;

import upe.exception.UPERuntimeException;
import upe.process.UProcessComponent;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;

public class UActionMethodInvoker implements Function<Map<String, Serializable>, Serializable> {
    private UProcessComponent p;
    private Method m;

    public UActionMethodInvoker(UProcessComponent p, Method m) {
        this.p = p;
        this.m = m;
    }

    @Override
    public Serializable apply(Map<String, Serializable> stringSerializableMap) {
        try {
            return (Serializable)this.m.invoke(p, stringSerializableMap);
        } catch( ReflectiveOperationException e ) {
            throw new UPERuntimeException(e);
        }
    }
}
