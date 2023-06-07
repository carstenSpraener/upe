package upe.process.impl;

import upe.annotations.UProcessValue;
import upe.exception.UPERuntimeException;
import upe.process.UProcess;
import upe.process.UProcessComponent;
import upe.process.UProcessElement;
import upe.process.UProcessField;
import upe.process.rules.UProcessRule;
import upe.process.rules.UpeRuleVetoException;

import java.lang.reflect.InvocationTargetException;
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
    public void elementChanged(UProcessElement processElement) throws UpeRuleVetoException {
        List<Object> argList = new ArrayList<>();
        for( Parameter p : this.method.getParameters() ) {
            argList.add(readProcessValue(processElement.getProcess(), p));
        }
        try {
            this.method.invoke(this.containingProcessComponent, argList.toArray());
        } catch( InvocationTargetException itXC ) {
          if( itXC.getTargetException() instanceof UpeRuleVetoException rvXC ) {
              throw rvXC;
          }
          throw new UPERuntimeException(itXC);
        } catch( ReflectiveOperationException roXC ) {
            throw new UPERuntimeException("Error while triggering ruleMethod "+this.method.getName()+": "+roXC.getMessage(), roXC);
        }
    }

    private Object readProcessValue(UProcess activeProcess, Parameter p) {
        UProcessElement pElement = this.containingProcessComponent.getProcessElement(getElementPathForParameter(p));
        String fqPath = pElement.getElementPath();
        return activeProcess.getProcessElement(
                fqPath,
                UProcessField.class
        ).getValue();
    }

    private String getElementPathForParameter(Parameter p) {
        String elementPath = p.getName();
        if( p.isAnnotationPresent(UProcessValue.class)) {
            elementPath = p.getAnnotation(UProcessValue.class).value();
        }
        return elementPath;
    }

    public void bindToProcess(UProcessComponent p) {
        for( Parameter param : this.method.getParameters() ) {
            p.getProcessElement(getElementPathForParameter(param)).addProcessElementListener(this);
        }
    }
}
