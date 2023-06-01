package upe.resource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import upe.annotations.UpeApplication;
import upe.resource.model.ProcessDelta;
import upe.resource.testprocess.PersonProcess;
import upe.test.annotations.UpeProcessToTest;
import upe.test.jupiter.UpeTestExtension;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


@ExtendWith(
        UpeTestExtension.class
)
@UpeApplication(
        PersonProcess.class
)
public class TestProcessListHandling {

    @Test
    public void testAddressAdding() throws Exception {
        UpeDialog dialog = new UpeDialog();
        Map<String, Serializable> argsMap = new HashMap<>();
        ProcessDelta delta = dialog.initiateProcess("Person", argsMap);
        PersonProcess pp = (PersonProcess)dialog.getActiveProcess();
        pp.setFieldValue("adress/strasse", "Kirchesch 6");


    }
}
