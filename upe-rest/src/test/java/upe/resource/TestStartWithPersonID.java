package upe.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import upe.annotations.UpeApplication;
import upe.annotations.UpeBackendFacade;
import upe.backend.UProcessBackend;
import upe.resource.model.ProcessDelta;
import upe.resource.testprocess.PersonProcess;
import upe.resource.testprocess.backend.PersonService;
import upe.resource.testprocess.dto.AddressDTO;
import upe.resource.testprocess.dto.PersonDTO;
import upe.test.jupiter.UpeTestExtension;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(
        UpeTestExtension.class
)
@UpeApplication(
        PersonProcess.class
)
public class TestStartWithPersonID {

    PersonService prsSrv = mock(PersonService.class);
    PersonDTO personFromBackend = new PersonDTO();

    @BeforeEach
    void setup() {
        personFromBackend.setName("John Doe");
        AddressDTO addressFromBackend = new AddressDTO();
        addressFromBackend.setStreet("5th Avenue");
        personFromBackend.getAddressList().add(addressFromBackend);
        when(prsSrv.loadByID(any())).thenReturn(personFromBackend);
        doAnswer( i -> {
            this.personFromBackend = i.getArgument(0);
            return null;
        }).when(prsSrv).store(any());

        UProcessBackend.addSupplier("PersonService", ()->this.prsSrv);
    }

    @Test
    void testStartWithPersonID() throws Exception {
        UpeDialog dialog = new UpeDialog();
        Map<String, Serializable> argsMap = new HashMap<>();
        argsMap.put("ID", "17");
        ProcessDelta delta = dialog.initiateProcess("Person", argsMap);
        assertThat(delta.getElementDeltaList())
                .map(peDelta -> peDelta.getElementPath())
                .contains(
                        "/name",
                        "/addressList[0]/street"
                );
    }

    @Test
    void testRestoreLoadPersonProcessNoActionCalled() throws Exception {
        UpeDialog dialog = new UpeDialog();
        Map<String, Serializable> argsMap = new HashMap<>();
        argsMap.put("ID", "17");
        ProcessDelta delta = dialog.initiateProcess("Person", argsMap);
        String dialogID = delta.getState().getDialogID();
        when(this.prsSrv.loadByID(any())).thenThrow(new RuntimeException("Now initialize when restoring from protocol!"));
        delta = dialog.rebuild(dialogID);
        assertNotNull(delta);
        assertThat(delta.getElementDeltaList())
                .map(peDelta -> peDelta.getElementPath()+"='"+peDelta.getValueForFrontend()+"'")
                .contains("/addressList[0]/street='5th Avenue'");
    }
}

