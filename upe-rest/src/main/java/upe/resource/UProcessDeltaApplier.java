package upe.resource;

import upe.process.*;
import upe.process.messages.UProcessMessage;
import upe.resource.model.ProcessDelta;
import upe.resource.model.ProcessElementDelta;
import upe.resource.model.UpeDeltaType;

import java.util.List;
import java.util.logging.Logger;

public class UProcessDeltaApplier {
    private static final Logger LOGGER = Logger.getLogger(UProcessDeltaRecorder.class.getSimpleName());

    public UProcessDeltaApplier (){
        LOGGER.fine("\n>>>>>> New UProcessDeltaApplier to rebuild a process");
    }

    public void applyDelta(UProcess activeProcess, ProcessDelta delta) {
        for(ProcessElementDelta peDelta : delta.getElementDeltaList() ) {
            LOGGER.fine(()->"     applying processElementDelta on element "+peDelta.getElementPath());
            applyElementDelta(activeProcess, peDelta);
        }
    }

    public void applyDelta(UpeDialogProcessEngine engine, List<ProcessDelta> deltaList) {
        for( ProcessDelta pDelta : deltaList ) {
            LOGGER.fine(()->"Applying process delta "+pDelta.getType()+ ".");
            applyProcessNavigation(engine, pDelta);
            applyDelta(engine.getActiveProcess(), pDelta);
        }
    }

    private static void applyProcessNavigation(UpeDialogProcessEngine engine, ProcessDelta pDelta) {
        if( pDelta.getType() == UpeDeltaType.CL ) {
            UProcessAction returnAction = null;
            if( engine.hasActiveProcess() && pDelta.getReturnActionPath()!=null ) {
                returnAction = engine.getActiveProcess().getProcessElement(pDelta.getReturnActionPath(), UProcessAction.class);
            }
            LOGGER.fine(()->"     calling callProcessForRestore "+pDelta.getTargetProcess()+ " with args "+pDelta.getCallArgs()+".");
            engine.callProcessForRestore(pDelta.getTargetProcess(), pDelta.getCallArgs(), returnAction);
        }
        if( pDelta.getType() == UpeDeltaType.FP ) {
            engine.finishProcessForRestore();
        }
    }

    private void applyElementDelta(UProcess activeProcess, ProcessElementDelta delta) {
        UProcessElement pElement = findOrCreate(activeProcess,delta.getElementPath());
        pElement.setEnabled(delta.getEnabled());
        pElement.setVisible(delta.getVisible());
        if( pElement  instanceof UProcessField pField) {
            pField.setValueFromFrontend(delta.getValueForFrontend());
        }
        pElement.resetModificationTracking();
        applyMessages(pElement, delta);
        pElement.setNeedsRendering(false);
        LOGGER.fine(()->"The following ProcessElementDelta was applyied on process "+activeProcess.getName()+" : "+delta);
    }

    private void applyMessages(UProcessElement pElement, ProcessElementDelta delta) {
        for(UProcessMessage msg : delta.getRemovedMessages() ) {
            pElement.removeProcessMessage(msg);
        }
        for( UProcessMessage msg : delta.getNewMessages() ) {
            pElement.addProcessMessage(msg);
        }
    }

    /**
     * Ensure, that a list contains the requested element, if not yet present.
     *
     * @param processComponent
     * @param elementPath
     * @return
     */
    private UProcessElement findOrCreate(UProcessComponent processComponent, String elementPath) {
       if( !elementPath.contains("[")) {
           return processComponent.getProcessElement(elementPath);
       }
       UProcessComponentList<?> list = getProcessList(processComponent, elementPath);
       int requestedIdx = getElementPathRequestedIndex(elementPath);
       while( list.size() <= requestedIdx ) {
           list.createNewInstance();
       }
       return findOrCreate(list.getAt(requestedIdx), getSubListPath(elementPath));
    }

    private String getSubListPath(String elementPath) {
        return elementPath.substring(elementPath.indexOf("]/")+2);
    }

    private int getElementPathRequestedIndex(String elementPath) {
        String idxStr = elementPath.substring(elementPath.indexOf('[')+1, elementPath.indexOf(']'));
        return Integer.valueOf(idxStr);
    }

    private UProcessComponentList<?> getProcessList(UProcessComponent processComponent, String elementPath) {
        String listPath = elementPath.substring(0, elementPath.indexOf('['));
        return (UProcessComponentList<?>)processComponent.getProcessElement(listPath);
    }


}
