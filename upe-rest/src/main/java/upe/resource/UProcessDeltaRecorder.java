package upe.resource;

import upe.process.messages.UProcessMessage;
import upe.resource.model.ProcessDelta;
import upe.resource.model.UpeDeltaType;
import upe.resource.model.UpeDialogState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class UProcessDeltaRecorder  implements UProcessChangeListener {
    private static final Logger LOGGER  = Logger.getLogger(UProcessDeltaRecorder.class.getSimpleName());

    private UpeDialogProcessEngine processEngine;
    private List<ProcessDelta> recordedDeltas = new ArrayList<>();
    private UpeDialogState dialogState;
    private List<UProcessMessage> queuedMessages = new ArrayList<>();

    public UProcessDeltaRecorder(UpeDialogState dialogState, UpeDialogProcessEngine processEngine) {
        this.dialogState = dialogState;
        this.processEngine = processEngine;
        this.processEngine.setChangeListener(this);
        this.processEngine.withQueuedMessageConsumer(this::recordMessage);
        LOGGER.fine("\n>>>>>>>>>>> New instance of UProcessDeltaRecorder for dialog "+dialogState.getDialogID()+" and step "+dialogState.getStepCount()+"<<<<<<<<<<<\n");
    }

    private void recordMessage(UProcessMessage uProcessMessage) {
        this.queuedMessages.add(uProcessMessage);
    }

    public ProcessDelta getActiveDelta() {
        if( this.recordedDeltas.isEmpty() ) {
            return null;
        }
        return this.recordedDeltas.get(this.recordedDeltas.size()-1);
    }

    public void startRecording(UpeDeltaType deltaType) {
        LOGGER.fine("Start Recording with type "+deltaType.name());
        ProcessDelta activeDelta = getActiveDelta();
        if( activeDelta==null ) {
            LOGGER.fine("   no ProcessDelta active. Creating one.");
            activeDelta = new ProcessDelta(this.processEngine, this.dialogState,deltaType);
            this.recordedDeltas.add(activeDelta);
        }
        if( deltaType==UpeDeltaType.AC ||deltaType==UpeDeltaType.VC) {
            // Other navigation type are recorded in the ProcessChangeListener methods.
            LOGGER.fine("Starting recording ProcessDelta "+activeDelta);
            activeDelta.startRecording(this.processEngine.getActiveProcess());
        }
    }

    public List<ProcessDelta> stopRecording() {
        LOGGER.fine("Stop Recording on ProcessDelta "+getActiveDelta());
        getActiveDelta().stopRecording(this.processEngine.getActiveProcess());
        return this.recordedDeltas;
    }

    @Override
    public void callProcess(String fromProcess, String toProcess, Map<String, Object> callArgs, String returnActionPath) {
        LOGGER.fine(()->"Recording callProcess("+fromProcess+", "+toProcess+", "+callArgs+", "+returnActionPath+")");
        ProcessDelta callDelta = getActiveDelta();
        if( this.processEngine.hasActiveProcess() ) {
            LOGGER.fine(()->"    recording callProcess: Active process found. Stop recording on ProcessDelta for that process");
            getActiveDelta().stopRecording(this.processEngine.getActiveProcess());
            callDelta = ProcessDelta.createProcessDeltaForCall(this.processEngine, this.dialogState, toProcess, callArgs, returnActionPath);
            this.recordedDeltas.add(callDelta);
            LOGGER.fine(()->"    added new create process delta to the recorded list.");
        } else {
            LOGGER.fine(()->"    calling Process from empty call stack. Adding info to existing ProcessDelta");
            callDelta.recordProcessCall(toProcess, callArgs, returnActionPath);
        }
    }

    @Override
    public void jumpToProcess(String fromProcess, String toProcess, Map<String, Object> callArgs) {
        LOGGER.fine(()->"Recording jumpToProcess("+fromProcess+", "+toProcess+", "+callArgs+")");
        LOGGER.warning(()->"jumpToProcess recoding not implemented yet!");
    }

    @Override
    public void finishProcess(String finishedProcessName, Map<String, Object> resultArgs) {
        LOGGER.fine(()->"Recording finishProcess("+finishedProcessName+", "+resultArgs+")");
        ProcessDelta activeDelta = getActiveDelta();
        if( activeDelta != null ) {
            LOGGER.fine(()->"    active delta to record finisProcess found.");
            if(processEngine.hasActiveProcess()) {
                LOGGER.fine(()->"    there is a active process on the stack. Stopping recording for that ProcessDelta.");
                activeDelta.stopRecording(processEngine.getActiveProcess());
            } else {
                LOGGER.fine(()->"    there is no more active process on the stack. Forgetting last record. (Is that a good idea?)");
                // Last process removed from stack... forget the recording.
                this.recordedDeltas.remove(activeDelta);
                activeDelta = null;
            }
        }
        if(processEngine.hasActiveProcess()) {
            LOGGER.fine(()->"    there is a active process on the stack. Starting new ProcessDelta recording for that process.");
            ProcessDelta finishDelta = new ProcessDelta(this.processEngine, this.dialogState, UpeDeltaType.FP);
            this.recordedDeltas.add(finishDelta);
            finishDelta.startRecording(this.processEngine.getActiveProcess());
        }
    }

    @Override
    public void cancelProcess(String canceledProcessName, Map<String, Object> resultArgs) {
        LOGGER.fine(()->"Recording cancelProcess("+canceledProcessName+", "+resultArgs+")");
        LOGGER.warning(()->"cancelProcess recoding not implemented yet!");
    }

    public List<ProcessDelta> getRecordedDeltas() {
        return this.recordedDeltas;
    }
}
