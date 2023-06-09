package upe.resource;

import upe.process.*;
import upe.process.messages.UProcessMessage;
import upe.resource.model.ProcessDelta;
import upe.resource.model.ProcessElementDelta;

public class UProcessDeltaApplier {

    public void applyDelta(UProcess activeProcess, ProcessDelta delta) {
        for(ProcessElementDelta peDelta : delta.getElementDeltaList() ) {
            applyElementDelta(activeProcess, peDelta);
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
