package upe.process.impl;

import upe.process.UProcess;
import upe.process.UProcessElement;
import upe.process.UProcessEngine;
import upe.process.messages.UProcessMessage;
import upe.process.rules.UProcessRule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractUProcessImpl extends UProcessComponentImpl implements UProcess {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private UProcessEngine myUProcessEngine = null;

    protected AbstractUProcessImpl(UProcessEngine pe, String name) {
        super(name);
        myUProcessEngine = pe;
    }

    @Override
    public UProcess getProcess() {
        return this;
    }

    @Override
    public UProcessEngine getProcessEngine() {
        return myUProcessEngine;
    }

    @Override
    public void inputStarts() {
        List<UProcessElement> resultList = new ArrayList<>();
        resultList = getProcessElements(resultList);
        for (UProcessElement pe : resultList) {
            ((AbstractUProcessElementImpl) pe).inputStarts();
        }
    }

    @Override
    public void inputStops() {
        doValidation();
    }

    private Set<String> collectChangedValuePaths() {
        Set<String> changedValues = new HashSet<>();
        for( UProcessElement pe : getProcessElements() ) {
            if( pe.needsRendering() ) {
                changedValues.add(pe.getElementPath());
            }
        }
        return changedValues;
    }

    @Override
    public boolean hasErrorMessage() {
        return getMaximumMessageLevel() == UProcessMessage.MESSAGE_LEVEL_ERROR;
    }
}
