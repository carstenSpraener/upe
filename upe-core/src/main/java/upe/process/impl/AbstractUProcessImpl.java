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
        trigerRules();
        doValidation();
    }

    private void trigerRules() {
        Set<String> changedValues = collectChangedValuePaths();
        int nofChanged = changedValues.size();
        do {
            nofChanged = changedValues.size();
            Set<UProcessRule> rulesToTrigger = new HashSet<>();
            for (UProcessRule r : this.getRulesRecursive(new ArrayList<>())) {
                for (String path : changedValues) {
                    if (r.interestedIn(path)) {
                        rulesToTrigger.add(r);
                        break;
                    }
                }
            }
            for (UProcessRule r : rulesToTrigger) {
                r.valuesChanged(this);
            }
            changedValues = collectChangedValuePaths();
        }while( changedValues.size() > nofChanged);
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
