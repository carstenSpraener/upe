package upe.incubator.process.impl;

import upe.process.UProcess;
import upe.process.UProcessEngine;
import upe.process.impl.AbstractUProcessImpl;

import java.io.Serializable;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class GenericUProcessImpl extends AbstractUProcessImpl {

    private transient BiConsumer<UProcess, Map<String, Serializable>> onInitialize;
    private transient Function<UProcess, Map<String, Serializable>> onFinish;
    private transient Function<UProcess, Map<String, Serializable>> onCancel;

    public GenericUProcessImpl(UProcessEngine pe, String name) {
        super(pe, name);
    }

    @Override
    public void initialize(Map<String, Serializable> args) {
        if( this.onInitialize != null ) {
            this.onInitialize.accept(this, args);
        }
    }

    @Override
    public Map<String, Serializable> finish() {
        if( this.onFinish != null ) {
            return this.onFinish.apply(this);
        }
        return Map.of();
    }

    @Override
    public Map<String, Serializable> cancel() {
        if( this.onCancel != null ) {
            return this.onCancel.apply(this);
        }
        return Map.of();
    }

    public void setOnInitialize(BiConsumer<UProcess, Map<String, Serializable>> onInitialize) {
        this.onInitialize = onInitialize;
    }

    public void setOnFinish(Function<UProcess, Map<String, Serializable>> onFinish) {
        this.onFinish = onFinish;
    }

    public void setOnCancel(Function<UProcess, Map<String, Serializable>> onCancel) {
        this.onCancel = onCancel;
    }

}
