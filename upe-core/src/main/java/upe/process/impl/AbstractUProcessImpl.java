package upe.process.impl;

import upe.process.UProcess;
import upe.process.UProcessElement;
import upe.process.UProcessEngine;
import upe.process.messages.UProcessMessage;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public boolean hasErrorMessage() {
        return getMaximumMessageLevel() == UProcessMessage.MESSAGE_LEVEL_ERROR;
    }
}
