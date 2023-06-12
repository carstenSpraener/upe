package upe.resource.model;

import upe.process.UProcess;
import upe.process.UProcessElement;
import upe.process.UProcessEngine;
import upe.process.UProcessField;
import upe.process.messages.UProcessMessage;
import upe.resource.UpeDialogProcessEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProcessDelta {
    private static final Logger LOGGER = Logger.getLogger(ProcessDelta.class.getName());

    private static long processDeltaCount = 0;

    private Long deltaID = processDeltaCount++;
    private UpeDialogState state;
    private UpeDeltaType type;
    private List<ProcessElementState> elementStateList = new ArrayList<>();
    private List<ProcessElementDelta> elementDeltaList = new ArrayList<>();
    private String processName;
    private List<UProcessMessage> queuedMessages;
    private Map<String, Object> callArgs;
    private String returnActionPath;
    private String targetProcess;

    public ProcessDelta(UpeDialogProcessEngine engine, UpeDialogState state, UpeDeltaType type) {
        this.state = state;
        this.type = type;
        if( engine.hasActiveProcess() ) {
            this.processName = engine.getActiveProcess().getName();
        } else {
            this.processName = null;
        }
    }

    public Long getDeltaID() {
        return deltaID;
    }

    public static ProcessDelta createProcessDeltaForCall(UpeDialogProcessEngine engine, UpeDialogState dialogState, String toProcess, Map<String, Object> callArgs, String returnActionPath) {
        ProcessDelta pd = new ProcessDelta(engine, dialogState, UpeDeltaType.CL);
        pd.recordProcessCall(toProcess, callArgs, returnActionPath);
        return pd;
    }

    public void recordProcessCall(String targetProcess, Map<String,Object> callArgs, String returnActionPath) {
        this.callArgs = callArgs;
        this.targetProcess = targetProcess;
        this.returnActionPath = returnActionPath;
    }

    public UpeDeltaType getType() {
        return type;
    }

    public void startRecording(UProcess p) {
        LOGGER.fine("ProcessDelta "+this.getDeltaID()+": start recrding on process "+p.getName());
        this.processName = p.getName();
        List<UProcessElement> elementList = new ArrayList<>();
        p.getProcessElements(elementList)
                .stream()
                .filter(pe -> pe instanceof UProcessField)
                .map(pe -> (UProcessField) pe)
                .forEach(pf -> elementStateList.add(new ProcessElementState(pf)));
    }

    public void stopRecording(UProcess p) {
        LOGGER.fine("ProcessDelta "+this.getDeltaID()+": stop recrding on process "+p.getName());
        if( !p.getName().equals(this.processName) ) {
            buildCompleteState(p);
            return;
        }
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
                            final ProcessElementDelta delta = pState.getDelta((UProcessField) pe);
                            if( delta != null ) {
                                LOGGER.fine(()->"ProcessDelta "+getDeltaID()+" recorded new ProcessElementDelta "+delta);
                            }
                            return delta;
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
        LOGGER.fine(()->"Building complete state for process "+p.getName());
        this.processName = p.getName();
        List<UProcessElement> elementList = new ArrayList<>();
        this.elementDeltaList = p.getProcessElements(elementList)
                .stream()
                .filter(pe -> pe instanceof UProcessField)
                .map( pe -> (UProcessField)pe)
                .map( pf ->  new ProcessElementState(pf))
                .map(s -> new ProcessElementDelta().from(s))
                .map( delta -> {
                    LOGGER.fine("ProcessDelta "+getDeltaID()+" in buildCompleteState: create new processElementDelta "+delta);
                    return delta;
                })
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

    public void setGlobalMessages(List<UProcessMessage> queuedMessages) {
        this.queuedMessages = queuedMessages;
    }

    public List<UProcessMessage> getQueuedMessages() {
        return queuedMessages;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Map<String, Object> getCallArgs() {
        return callArgs;
    }

    public String getReturnActionPath() {
        return returnActionPath;
    }

    public String getTargetProcess() {
        return targetProcess;
    }

    @Override
    public String toString() {
        return "ProcessDelta{" +
                "deltaID=" + deltaID +
                ", type=" + type +
                ", processName='" + processName + '\'' +
                ", callArgs=" + callArgs +
                ", returnActionPath='" + returnActionPath + '\'' +
                ", targetProcess='" + targetProcess + '\'' +
                '}';
    }
}
