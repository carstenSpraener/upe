package upe.incubator.process.impl;

import upe.process.UProcess;
import upe.process.UProcessEngine;
import upe.process.impl.AbstractUProcessImpl;


import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class GenericUProcessImpl extends AbstractUProcessImpl {

    private transient BiConsumer<UProcess, Map<String, Object>> onInitialize;
    private transient Function<UProcess, Map<String, Object>> onFinish;
    private transient Function<UProcess, Map<String, Object>> onCancel;

    public GenericUProcessImpl(UProcessEngine pe, String name) {
        super(pe, name);
    }

    @Override
    public void initialize(Map<String, Object> args) {
        if( this.onInitialize != null ) {
            this.onInitialize.accept(this, args);
        }
    }

    @Override
    public Map<String, Object> finish() {
        if( this.onFinish != null ) {
            return this.onFinish.apply(this);
        }
        return Map.of();
    }

    @Override
    public Map<String, Object> cancel() {
        if( this.onCancel != null ) {
            return this.onCancel.apply(this);
        }
        return Map.of();
    }

    public void setOnInitialize(BiConsumer<UProcess, Map<String, Object>> onInitialize) {
        this.onInitialize = onInitialize;
    }

    public void setOnFinish(Function<UProcess, Map<String, Object>> onFinish) {
        this.onFinish = onFinish;
    }

    public void setOnCancel(Function<UProcess, Map<String, Object>> onCancel) {
        this.onCancel = onCancel;
    }

}
