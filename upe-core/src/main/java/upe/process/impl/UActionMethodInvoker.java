package upe.process.impl;

import upe.exception.UPERuntimeException;
import upe.process.UProcessComponent;


import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.Function;

public class UActionMethodInvoker implements Function<Map<String, Object>, Object> {
    private UProcessComponent p;
    private Method m;

    public UActionMethodInvoker(UProcessComponent p, Method m) {
        this.p = p;
        this.m = m;
    }

    @Override
    public Object apply(Map<String, Object> stringObjectMap) {
        try {
            return (Object)this.m.invoke(p, stringObjectMap);
        } catch( ReflectiveOperationException e ) {
            throw new UPERuntimeException(e);
        }
    }
}
