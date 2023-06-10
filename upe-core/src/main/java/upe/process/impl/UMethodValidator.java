package upe.process.impl;

import upe.annotations.UpeProcessValue;
import upe.annotations.UpeValidator;
import upe.process.UProcessComponent;
import upe.process.UProcessEngine;
import upe.process.UProcessField;
import upe.process.validation.impl.UProcessValidatorSupport;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class UMethodValidator extends UProcessValidatorSupport {
    private UProcessComponent processComponent;
    private Method methodToInvoke;
    private String[] parameterPaths = null;
    private String msgID;

    public UMethodValidator(UProcessComponent processComponent, Method methodToInvoke) {
        this.processComponent = processComponent;
        this.methodToInvoke = methodToInvoke;
        fillParameterPathList(methodToInvoke);
        this.processComponent.addValidator(this);
        this.msgID = ((UpeValidator)methodToInvoke.getAnnotation(UpeValidator.class)).value();
    }

    private void fillParameterPathList(Method methodToInvoke) {
        List<String> paramList = new ArrayList<>();
        for( Parameter p : methodToInvoke.getParameters() ) {
            if( p.isAnnotationPresent(UpeProcessValue.class)) {
                String path = p.getName();
                UpeProcessValue paraConfig = p.getAnnotation(UpeProcessValue.class);
                if ( paraConfig.value()!=null && !paraConfig.value().equals("") ) {
                    path = paraConfig.value();
                }
                paramList.add(path);
            }
        }
        this.parameterPaths = new String[paramList.size()];
        paramList.toArray(this.parameterPaths);
    }

    @Override
    public void validate(UProcessComponent proc) {
        String[] args = new String[parameterPaths.length];
        for(int idx=0; idx<this.parameterPaths.length; idx++ ) {
            args[idx] = this.processComponent.getProcessElement(parameterPaths[idx], UProcessField.class).getValueForFrontend();
        }
        try {
            Boolean hasError = (Boolean) this.methodToInvoke.invoke(this.processComponent, (Object[])args);
            if (hasError) {
                addMessage(msgID, this.processComponent, parameterPaths);
            } else {
                removeMessage(msgID, this.processComponent, parameterPaths);
            }
        } catch( ReflectiveOperationException roXC ) {
            UProcessEngine.LOGGER.warning("Error while calling method validator "+ methodToInvoke.getName()+" on process component "+this.processComponent.getElementPath()+": "+roXC.getMessage());
        }
    }
}
