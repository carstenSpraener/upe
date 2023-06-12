package upe.resource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import upe.annotations.UpeApplication;
import upe.resource.model.ProcessDelta;
import upe.resource.model.UpeDialogState;
import upe.resource.model.UpeStep;
import upe.resource.persistorimpl.UpeDialogPersistorJdbcImpl;
import upe.resource.testprocess.PersonProcess;
import upe.test.jupiter.UpeTestExtension;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(
        UpeTestExtension.class
)
@UpeApplication(
        PersonProcess.class
)
public class TestProcessListHandling {

    @Test
    public void testAddressAdding() throws Exception {
        // Given: A person process with addressEditor and adressList
        Map<String, Object> argsMap = new HashMap<>();
        ProcessDelta delta = new UpeDialog().initiateProcess("Person", argsMap);

        UpeDialog _dialog = new UpeDialog();
        _dialog.rebuild(delta.getState().getDialogID());
        PersonProcess pp = (PersonProcess) _dialog.getActiveProcess();

        // setting the address/strasse field to some value
        delta = new UpeDialog().putValueChange(delta.getState().getDialogID(), delta.getState().getStepCount(), "selectedAddress/street", "Kirchesch 6");

        // when: triggering action actSelectedAdressOK
        delta = new UpeDialog().triggerAction(delta.getState().getDialogID(), delta.getState().getStepCount(), "actSelectedAddressOK");

        // then: /address/strasse should empty and addressList[0]/strasse should take the value
        // this changes shall apear in the delta.
        assertThat(delta.getElementDeltaList())
                .map(peDelta -> peDelta.getElementPath()+"='"+peDelta.getValueForFrontend()+"'")
                .contains("/selectedAddress/street=''", "/addressList[0]/street='Kirchesch 6'");
        UpeDialogState upeState = UpeDialogPersistorJdbcImpl.intance(UpeDialog.getGson()).restore(delta.getState().getDialogID());
        assertEquals(upeState.getStepCount() + 1, upeState.getSteps().size());
        assertNotNull(upeState.getSteps().get(upeState.getStepCount()));
        UpeStep lastState = upeState.getSteps().get(upeState.getStepCount());
        assertNotNull(lastState.getDeltaList().get(0));
    }
}
