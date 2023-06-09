package upe.resource;

import com.google.gson.Gson;
import upe.resource.model.UpeDialogState;

public interface UpeDialogPersistor {
    UpeDialogState storeAction(String dialogID, int stepCount, String changedFieldPath, String deltaJSon);

    /**
     * Speichert den Change am Dialog und liefert eine Dialog-Instanz. Dieser ist im normalfall
     * der Dialog, mit der neuen Veränderung. Es kann aber sein, dass auf einem anderem System
     * weitere Änderungen durchgeführt wurden und deshalb mehrere Veränderungen verarbeitet wurden.
     * In diesem Fall wird zunächst der alte Dialog wiederhergestellt und dann der Step verarbeitet.
     * Sollte die übergebene Step-ID nicht mit der StepID des Restaurierten Dialogs übereinstimmen,
     * wird eine entsprechende RuntimeException erzeigt.
     *
     * @param dialogID
     * @param changedFieldPath
     * @param oldValue
     * @param newValue
     * @return
     */
    UpeDialogState storeStep(String dialogID, int stepCount, String changedFieldPath, String oldValue, String newValue, String deltaJson);

    UpeDialogState restore(String dialogID, Gson deltaGson);

    UpeDialogState initiate();
}
