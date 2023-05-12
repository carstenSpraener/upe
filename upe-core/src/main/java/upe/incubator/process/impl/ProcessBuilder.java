package upe.incubator.process.impl;

import upe.annotations.UpeOnCancel;
import upe.annotations.UpeOnFinish;
import upe.annotations.UpeOnInitialize;
import upe.annotations.UpeProcess;
import upe.exception.UPERuntimeException;
import upe.process.UProcess;
import upe.process.UProcessEngine;
import upe.process.impl.UProcessTextFieldImpl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ProcessBuilder {
    private Class<?> clazz;
    private UProcessEngine engine;
    private String name;
    private BiConsumer<UProcess, Map<String, Serializable>> onInitialize;
    private Function<UProcess, Map<String, Serializable>> onFinish;
    private Function<UProcess, Map<String, Serializable>> onCancel;

    public ProcessBuilder(Class<?> clazz) {
        this.clazz = clazz;
    }

    public ProcessBuilder withProcessEngine(UProcessEngine engine) {
        this.engine = engine;
        return this;
    }

    public ProcessBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProcessBuilder withOnInitialize(BiConsumer<UProcess, Map<String, Serializable>> onInitialize) {
        this.onInitialize = onInitialize;
        return this;
    }

    public ProcessBuilder withOnFinish(Function<UProcess, Map<String, Serializable>> onFinish) {
        this.onFinish = onFinish;
        return this;
    }

    public ProcessBuilder withOnCancel(Function<UProcess, Map<String, Serializable>> onCancel) {
        this.onCancel = onCancel;
        return this;
    }

    public UProcess build() {
        UpeProcess upeProcessAnnotation = clazz.getAnnotation(UpeProcess.class);
        if (this.name == null && upeProcessAnnotation != null) {
            this.name = upeProcessAnnotation.value();
        }
        if (this.name == null) {
            this.name = this.clazz.getName();
        }
        buildMethods();
        GenericUProcessImpl proc = new GenericUProcessImpl(this.engine, this.name);
        proc.setOnInitialize(this.onInitialize);
        proc.setOnFinish(this.onFinish);
        proc.setOnCancel(this.onCancel);
        createFields(proc, this.clazz);
        return proc;
    }

    private void buildMethods() {
        for (Method m : this.clazz.getMethods()) {
            if( m.isAnnotationPresent(UpeOnInitialize.class)) {
                buildInitialzeMethd(m);
            }
            if( m.isAnnotationPresent(UpeOnFinish.class)) {
                buildFinishMethod(m);
            }
            if( m.isAnnotationPresent(UpeOnCancel.class)) {
                buildCancelMethod(m);
            }
        }
    }

    private void buildCancelMethod(Method m) {
        Object annotation = m.getAnnotation(UpeOnCancel.class);
        if (annotation != null) {
            if (m.getReturnType().isAssignableFrom(Map.class) && m.getParameterTypes().length == 0) {
                this.onCancel = process -> {
                    try {
                        return (Map<String, Serializable>) m.invoke(process);
                    } catch (Exception e) {
                        throw new UPERuntimeException(e);
                    }
                };
            } else {
                throw new IllegalArgumentException("Method " + m.getName() + " marked as UpeOnCancel does not have appliable signature.");
            }
        }
    }

    private void buildFinishMethod(Method m) {
        Object annotation = m.getAnnotation(UpeOnFinish.class);
        if (annotation != null) {
            if (m.getReturnType().isAssignableFrom(Map.class) && m.getParameterTypes().length == 0) {
                this.onFinish = process -> {
                    try {
                        return (Map<String, Serializable>) m.invoke(process);
                    } catch (Exception e) {
                        throw new UPERuntimeException(e);
                    }
                };
            } else {
                throw new IllegalArgumentException(
                        String.format("Method '%s' marked as UpeOnInitialized does not have applicable signature.", m.getName()));
            }
        }
    }

    private void buildInitialzeMethd(Method m) {
        Object annotation = m.getAnnotation(UpeOnInitialize.class);
        Class<?>[] params = m.getParameterTypes();
        if (annotation != null) {
            if (params.length == 1 && params[0].isAssignableFrom(Map.class)) {
                this.onInitialize = (p, args) -> {
                    try {
                        m.invoke(p, args);
                    } catch (Exception e) {
                        throw new UPERuntimeException(e);
                    }
                };
            } else {
                throw new IllegalArgumentException(
                        String.format("Method '%s' marked as UpeOnInitialized does not have applicable signature.", m.getName()));
            }
        }
    }

    private void createFields(GenericUProcessImpl proc, Class<?> clazz) {
        for (Field f : clazz.getDeclaredFields()) {
            String fieldName = f.getName();
            proc.addProcessElement(fieldName, new UProcessTextFieldImpl(proc, fieldName));
        }
    }
}
