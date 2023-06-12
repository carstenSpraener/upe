package upe.resource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import upe.annotations.UpeApplication;
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
import java.util.stream.Collectors;

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

    @Test
    public void testInitializationAndRebuild() throws Exception {
        UpeDialog dialog = new UpeDialog();
        Map<String, Object> argsMap = new HashMap<>();
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
        Map<String, Object> argsMap = new HashMap<>();
        ProcessDelta delta = dialog.initiateProcess("Person", argsMap);
        assertThat(delta.getElementDeltaList())
                .flatMap(peDelta ->
                        peDelta.getNewMessages().stream()
                                .map(msg -> peDelta.getElementPath()+":"+msg.getMessageID())
                                .collect(Collectors.toList())
                )
                .contains("/selectedAddress/street:UPE0001");
        PersonProcess pp = (PersonProcess)dialog.getActiveProcess();
        assertNotNull(pp);

        dialog = new UpeDialog();
        delta = dialog.putValueChange(delta.getState().getDialogID(),delta.getState().getStepCount(),  "/name", "Carsten");
        assertEquals(1, delta.getElementDeltaList().size());
        assertEquals(1, delta.getState().getStepCount());
        assertEquals("/name", delta.getElementDeltaList().get(0).getElementPath());
        assertEquals("Carsten", delta.getElementDeltaList().get(0).getValueForFrontend());
    }

    @Test
    public void testActionTrigger() throws Exception {
        Map<String, Object> argsMap = new HashMap<>();
        ProcessDelta delta = new UpeDialog().initiateProcess("Person", argsMap);
        delta = new UpeDialog().putValueChange(delta.getState().getDialogID(),delta.getState().getStepCount(),
                "/name", "Carsten");
        delta = new UpeDialog().putValueChange(delta.getState().getDialogID(),delta.getState().getStepCount(),
                "/selectedAddress/street", "Kirchesch 6");
        delta = new UpeDialog().triggerAction(delta.getState().getDialogID(), delta.getState().getStepCount(),
                "actSelectedAddressOK");
        UpeDialog restoreDialog = new UpeDialog();
        restoreDialog.rebuild(delta.getState().getDialogID());
        PersonProcess pp = (PersonProcess) restoreDialog.getActiveProcess();
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
        delta = new UpeDialog().putValueChange(delta.getState().getDialogID(),delta.getState().getStepCount(),
                "/selectedAddress/street", "Kirchesch 7");
        delta = new UpeDialog().triggerAction(delta.getState().getDialogID(), delta.getState().getStepCount(),
                "actSelectedAddressOK");
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
