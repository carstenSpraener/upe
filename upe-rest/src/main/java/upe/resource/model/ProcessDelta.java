package upe.resource.model;

import upe.process.UProcess;
import upe.process.UProcessElement;
import upe.process.UProcessField;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessDelta {
    private UpeDialogState state;
    private List<ProcessElementState> elementStateList = new ArrayList<>();
    private List<ProcessElementDelta> elementDeltaList = new ArrayList<>();
    private String processName;

    public ProcessDelta(UpeDialogState state) {
        this.state = state;
    }

    public void startRecording(UProcess p) {
        this.processName = p.getName();
        List<UProcessElement> elementList = new ArrayList<>();
        p.getProcessElements(elementList)
                .stream()
                .filter(pe -> pe instanceof UProcessField)
                .map(pe -> (UProcessField) pe)
                .forEach(pf -> elementStateList.add(new ProcessElementState(pf)));
    }

    public void stopRecording(UProcess p) {
        List<UProcessElement> elementList = new ArrayList<>();
        elementList = p.getProcessElements(elementList)
                .stream()
                .filter(pe -> pe instanceof UProcessField)
                .collect(Collectors.toList());
        elementList.stream()
                .map(pe -> (UProcessField) pe)
                .filter( pf -> this.isNewProcessField(pf, elementStateList))
                .forEach(pf -> {
                    ProcessElementState state = ProcessElementState.fromNewField(pf.getElementPath());
                    elementStateList.add(state);
                });
        elementDeltaList = elementStateList.stream()
                .map(
                        pState -> {
                            UProcessElement pe = p.getProcessElement(pState.getFieldPath());
                            return pState.getDelta((UProcessField) pe);
                        }
                )
                .filter(delta -> delta != null)
                .collect(Collectors.toList());
        elementDeltaList.stream()
                .forEach(delta -> {
                    if (delta.newMessages.size() == 0) {
                        delta.newMessages = null;
                    }
                    if (delta.removedMessages.size() == 0) {
                        delta.removedMessages = null;
                    }
                });
        this.elementStateList = null;
    }

    private boolean isNewProcessField(UProcessField pf, List<ProcessElementState> elementStateList) {
        for( ProcessElementState state : elementStateList ) {
            if( state.getFieldPath().equals(pf.getElementPath()) ) {
                return false;
            }
        }
        return true;
    }

    public void buildCompleteState(UProcess p) {
        this.processName = p.getName();
        List<UProcessElement> elementList = new ArrayList<>();
        this.elementDeltaList = p.getProcessElements(elementList)
                .stream()
                .filter(pe -> pe instanceof UProcessField)
                .map( pe -> (UProcessField)pe)
                .map( pf ->  new ProcessElementState(pf))
                .map(s -> new ProcessElementDelta().from(s))
                .collect(Collectors.toList())
        ;
        this.elementStateList = null;
    }

    public List<ProcessElementDelta> getElementDeltaList() {
        return elementDeltaList;
    }

    public void setUpeDialogState(UpeDialogState state) {
        this.state = state;
    }

    public UpeDialogState getState() {
        return state;
    }
}
