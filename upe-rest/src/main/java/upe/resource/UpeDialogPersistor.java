package upe.resource;

import com.google.gson.Gson;
import upe.resource.model.ProcessDelta;
import upe.resource.model.UpeDialogState;

import java.util.List;

public interface UpeDialogPersistor {
    /**
     *
     * @param dialogID
     * @param stepCount
     * @param processDeltaList
     * @return
     */
    UpeDialogState storeDelta(String dialogID, int stepCount, List<ProcessDelta> processDeltaList);

    UpeDialogState restore(String dialogID);

    UpeDialogState initiate();
}
