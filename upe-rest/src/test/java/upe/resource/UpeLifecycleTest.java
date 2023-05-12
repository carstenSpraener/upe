package upe.resource;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import upe.process.ApplicationConfiguration;
import upe.resource.model.ProcessDelta;
import upe.resource.testprocess.PersonProcess;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UpeLifecycleTest {

    @Before
    public void setup() {
        ApplicationConfiguration.getInstance().addProcessClass("Person", PersonProcess.class.getName());
    }

    @Test
    public void testInitializationAndRebuild() throws Exception {
        UpeDialog dialog = new UpeDialog();
        Map<String, Serializable> argsMap = new HashMap<>();
        ProcessDelta delta = dialog.initiateProcess("Person", argsMap);
        PersonProcess pp = (PersonProcess)dialog.getActiveProcess();
        Assert.assertNotNull(pp);

        UpeDialog dialog2 = new UpeDialog();
        dialog2.rebuild(delta.getState().getDialogID());
        PersonProcess pp2 = (PersonProcess)dialog2.getActiveProcess();
        Assert.assertNotNull(pp2);
        Assert.assertFalse(pp2==pp);
    }

    @Test
    public void testValueChange() throws Exception {
        UpeDialog dialog = new UpeDialog();
        Map<String, Serializable> argsMap = new HashMap<>();
        ProcessDelta delta = dialog.initiateProcess("Person", argsMap);
        System.out.println( new Gson().toJson(delta));
        PersonProcess pp = (PersonProcess)dialog.getActiveProcess();
        Assert.assertNotNull(pp);

        delta = dialog.putValueChange(delta.getState().getDialogID(),delta.getState().getStepCount(),  "/name", "Carsten");
        System.out.println( new Gson().toJson(delta));
    }
}
