package upe.process.impl;

import upe.annotations.UProcessValue;
import upe.exception.UPERuntimeException;
import upe.process.UProcess;
import upe.process.UProcessComponent;
import upe.process.UProcessElement;
import upe.process.UProcessField;
import upe.process.rules.UProcessRule;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UMethodURule implements UProcessRule {
    private UProcessComponent containingProcessComponent;
    private Method method;
    private Set<String> elementPaths = null;

    public UMethodURule(UProcessComponent p, Method m) {
        this.containingProcessComponent = p;
        this.method = m;
    }

    @Override
    public void valuesChanged(UProcess activeProcess) {
        List<Object> argList = new ArrayList<>();
        for( Parameter p : this.method.getParameters() ) {
            argList.add(readProcessValue(activeProcess, p));
        }
        try {
            this.method.invoke(this.containingProcessComponent, argList.toArray());
        }catch( ReflectiveOperationException roXC ) {
            throw new UPERuntimeException("Error while triggering ruleMethod "+this.method.getName()+": "+roXC.getMessage(), roXC);
        }
    }

    private Object readProcessValue(UProcess activeProcess, Parameter p) {
        return activeProcess.getProcessElement(getElementPathForParameter(p), UProcessField.class).getValue();
    }
    private String getElementPathForParameter(Parameter p) {
        String elementPath = p.getName();
        if( p.isAnnotationPresent(UProcessValue.class)) {
            elementPath = p.getAnnotation(UProcessValue.class).value();
        }
        UProcessElement element =  this.containingProcessComponent.getProcessElement(elementPath);
        if( element == null ) {
            throw new UPERuntimeException("Unknown element '"+elementPath+"' specified in method "+ method.getName()+" for parameter "+p.getName());
        }
        element.getElementPath();
        return this.containingProcessComponent.getProcessElement(elementPath).getElementPath();
    }

    private Set<String> getElementPaths() {
        // Fill this lazy, because at construction not all the process tree is build.
        if( this.elementPaths == null ) {
            this.elementPaths = new HashSet<>();
            for( Parameter param : this.method.getParameters() ) {
                elementPaths.add(getElementPathForParameter(param));
            }
        }
        return this.elementPaths;
    }

    @Override
    public boolean interestedIn(String elementPath) {
        return getElementPaths().contains(elementPath);
    }
}
