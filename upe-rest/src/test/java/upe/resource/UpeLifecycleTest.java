package upe.resource;

import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import upe.annotations.UpeApplication;
import upe.process.ApplicationConfiguration;
import upe.process.UProcessElement;
import upe.resource.model.ProcessDelta;
import upe.resource.testprocess.PersonProcess;
import upe.test.annotations.UpeProcessToTest;
import upe.test.jupiter.UpeTestExtension;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(
        UpeTestExtension.class
)
@UpeApplication(
        PersonProcess.class
)
public class UpeLifecycleTest {

    @UpeProcessToTest("Person")
    private PersonProcess personProcess;

    public void setup() {
        ApplicationConfiguration.getInstance().addProcessClass("Person", PersonProcess.class.getName());
    }

    @Test
    public void testInitializationAndRebuild() throws Exception {
        UpeDialog dialog = new UpeDialog();
        Map<String, Serializable> argsMap = new HashMap<>();
        ProcessDelta delta = dialog.initiateProcess("Person", argsMap);
        PersonProcess pp = (PersonProcess)dialog.getActiveProcess();
        assertNotNull(pp);

        UpeDialog dialog2 = new UpeDialog();
        dialog2.rebuild(delta.getState().getDialogID());
        PersonProcess pp2 = (PersonProcess)dialog2.getActiveProcess();
        assertNotNull(pp2);
        assertFalse(pp2==pp);
    }

    @Test
    public void testValueChange() throws Exception {
        UpeDialog dialog = new UpeDialog();
        Map<String, Serializable> argsMap = new HashMap<>();
        ProcessDelta delta = dialog.initiateProcess("Person", argsMap);
        PersonProcess pp = (PersonProcess)dialog.getActiveProcess();
        assertNotNull(pp);
        delta = dialog.putValueChange(delta.getState().getDialogID(),delta.getState().getStepCount(),  "/name", "Carsten");
        assertEquals(1, delta.getElementDeltaList().size());
        assertEquals(1, delta.getState().getStepCount());
        assertEquals("/name", delta.getElementDeltaList().get(0).getElementPath());
        assertEquals("Carsten", delta.getElementDeltaList().get(0).getValueForFrontend());
    }

    @Test
    public void testActionTrigger() throws Exception {
        UpeDialog dialog = new UpeDialog();
        Map<String, Serializable> argsMap = new HashMap<>();
        ProcessDelta delta = dialog.initiateProcess("Person", argsMap);
        delta = dialog.putValueChange(delta.getState().getDialogID(),delta.getState().getStepCount(),
                "/name", "Carsten");
        delta = dialog.putValueChange(delta.getState().getDialogID(),delta.getState().getStepCount(),
                "/selectedAddress/street", "Kirchesch 6");
        delta = dialog.triggerAction(delta.getState().getDialogID(), delta.getState().getStepCount(),
                "actSelectedAdressOK");
        PersonProcess pp = (PersonProcess)dialog.getActiveProcess();
        assertNotNull(pp);
        assertEquals(3, delta.getElementDeltaList().size());
        assertThat(delta.getElementDeltaList())
                .map( ped -> {
                    System.out.println(ped.getElementPath());
                    return ped.getElementPath();
                })
                .contains(
                        "/selectedAddress/street",
                        "/addressList[0]/rowID",
                        "/addressList[0]/street"
                );
        delta = dialog.putValueChange(delta.getState().getDialogID(),delta.getState().getStepCount(),
                "/selectedAddress/street", "Kirchesch 7");
        delta = dialog.triggerAction(delta.getState().getDialogID(), delta.getState().getStepCount(),
                "actSelectedAdressOK");
        List<UProcessElement> elementList = new ArrayList<>();
        assertEquals(3, delta.getElementDeltaList().size());
        assertThat(delta.getElementDeltaList())
                .map( ped -> {
                    System.out.println(ped.getElementPath());
                    return ped.getElementPath();
                })
                .contains(
                        "/selectedAddress/street",
                        "/addressList[1]/rowID",
                        "/addressList[1]/street"
                );
    }
}
