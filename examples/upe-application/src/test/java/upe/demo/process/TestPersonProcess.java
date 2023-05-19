package upe.demo.process;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import upe.annotations.UpeApplication;
import upe.backend.UProcessBackend;
import upe.demo.process.backend.PersonMgr;
import upe.demo.process.dto.Person;
import upe.process.UProcessAction;
import upe.process.UProcessElement;
import upe.process.UProcessEngine;
import upe.process.UProcessModification;
import upe.process.messages.UProcessMessage;
import upe.test.UpeAssertions;
import upe.test.annotations.UInject;
import upe.test.annotations.UpeProcessToTest;
import upe.test.jupiter.UpeTestExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({
        UpeTestExtension.class
})
@UpeApplication({
    PersonProcess.class,
    ClientBrowsingProcess.class
})
public class TestPersonProcess {
    @UInject
    private UProcessEngine processEngine;

    @UpeProcessToTest("personProcess")
    private PersonProcess prsProc;

    @UpeProcessToTest()
    private ClientBrowsingProcess clientBrowsingProcess;

    @Test
    public void testValNameAndFirstName() throws Exception {
        UpeAssertions ua = new UpeAssertions(this.prsProc);

        ua.assertMaxMsgLevel("person/name", UProcessMessage.MESSAGE_LEVEL_ERROR);
        ua.assertMaxMsgLevel("person/firstName", UProcessMessage.MESSAGE_LEVEL_ERROR);
        ua.assertHasError("person/name", "PE-001");
        ua.assertHasError("person/firstName", "PE-001");

        try(var mod = new UProcessModification(this.prsProc)) {
            prsProc.setFieldValue("person/name", "Doe");
        }
        ua.assertMaxMsgLevel("person/name", UProcessMessage.MESSAGE_LEVEL_NONE);
        ua.assertMaxMsgLevel("person/firstName", UProcessMessage.MESSAGE_LEVEL_NONE);
        ua.assertNotHasError("person/name", "PE-001");
        ua.assertNotHasError("person/firstName", "PE-001");

        try(var mod = new UProcessModification(this.prsProc)) {
            prsProc.setFieldValue("person/name", "");
        }
        ua.assertMaxMsgLevel("person/name", UProcessMessage.MESSAGE_LEVEL_ERROR);
        ua.assertMaxMsgLevel("person/firstName", UProcessMessage.MESSAGE_LEVEL_ERROR);
        ua.assertHasError("person/name", "PE-001");
        ua.assertHasError("person/firstName", "PE-001");

        try(var mod = new UProcessModification(this.prsProc)) {
            prsProc.setFieldValue("person/firstName", "John");
        }
        ua.assertMaxMsgLevel("person/name", UProcessMessage.MESSAGE_LEVEL_NONE);
        ua.assertMaxMsgLevel("person/firstName", UProcessMessage.MESSAGE_LEVEL_NONE);
        ua.assertNotHasError("person/name", "PE-001");
        ua.assertNotHasError("person/firstName", "PE-001");
    }

    @Test
    public void testAddressValidations() throws Exception {
        UpeAssertions ua = new UpeAssertions(this.prsProc);
        ua.assertMaxMsgLevel("person/address/street", UProcessMessage.MESSAGE_LEVEL_ERROR);
        ua.assertMaxMsgLevel("person/address/number", UProcessMessage.MESSAGE_LEVEL_NONE);
        ua.assertMaxMsgLevel("person/address/zipCode", UProcessMessage.MESSAGE_LEVEL_ERROR);
        ua.assertMaxMsgLevel("person/address/city", UProcessMessage.MESSAGE_LEVEL_ERROR);
        ua.assertMaxMsgLevel("person/address/country", UProcessMessage.MESSAGE_LEVEL_NONE);

        try( var m = new UProcessModification(this.prsProc)) {
            this.prsProc.setFieldValue("person/address/street", "5th Avenue");
            this.prsProc.setFieldValue("person/address/number", "7b");
            this.prsProc.setFieldValue("person/address/zipCode", "49594");
            this.prsProc.setFieldValue("person/address/city", "Alfhausen");
            this.prsProc.setFieldValue("person/address/country", "Germany");
        }

        ua.assertMaxMsgLevel("person/address/street", UProcessMessage.MESSAGE_LEVEL_NONE);
        ua.assertMaxMsgLevel("person/address/number", UProcessMessage.MESSAGE_LEVEL_NONE);
        ua.assertMaxMsgLevel("person/address/zipCode", UProcessMessage.MESSAGE_LEVEL_NONE);
        ua.assertMaxMsgLevel("person/address/city", UProcessMessage.MESSAGE_LEVEL_NONE);
        ua.assertMaxMsgLevel("person/address/country", UProcessMessage.MESSAGE_LEVEL_NONE);
    }

    @Test
    public void testActSaveNiceWeatherFlight() throws Exception {
        PersonMgr mgrMock = mock(PersonMgr.class);
        UProcessBackend.addSupplier("personMgr", () -> mgrMock);
        final List<Person> storedPrs = new ArrayList<>();
        when(mgrMock.savePerson(any())).thenAnswer(i ->{
            Person prs = i.getArgument(0);
            storedPrs.add(prs);
            return prs;
        } );

        UProcessElement actSave = this.prsProc.getProcessElement("actSave");
        assertNotNull(actSave);
        try(var m = new UProcessModification(this.prsProc) ) {
            this.prsProc.setFieldValue("person/name", "Doe");
            this.prsProc.setFieldValue("person/firstName", "John");
            this.prsProc.setFieldValue("person/age", "27");
            this.prsProc.setFieldValue("person/gender", "m");
            this.prsProc.setFieldValue("person/address/street", "5th Avenue");
            this.prsProc.setFieldValue("person/address/number", "7b");
            this.prsProc.setFieldValue("person/address/zipCode", "49594");
            this.prsProc.setFieldValue("person/address/city", "Alfhausen");
            this.prsProc.setFieldValue("person/address/country", "Germany");
        }

        ((UProcessAction)actSave).execute(null);
        verify(mgrMock, times(1)).savePerson(any());
        Person prs = storedPrs.get(0);
        assertNotNull(prs);
        assertEquals("Doe", prs.getName());
        assertEquals("John", prs.getFirstName());
        assertEquals(27, prs.getAge());
        assertEquals("m", prs.getGender());

        assertEquals("5th Avenue", prs.getAddress().getStreet());
        assertEquals("7b", prs.getAddress().getNumber());
        assertEquals("49594", prs.getAddress().getZipCode());
        assertEquals("Alfhausen", prs.getAddress().getCity());
        assertEquals("Germany", prs.getAddress().getCountry());
    }

    @Test
    public void testActSaveWithErrors() throws Exception {
        UProcessElement actSave = this.prsProc.getProcessElement("actSave");
        assertNotNull(actSave);
        ((UProcessAction)actSave).execute(null);
        UpeAssertions ua = new UpeAssertions(this.prsProc);

        ua.assertProcessMessageQueued("PE-002");
    }
    @Test
    public void testCallFromClientBrowsing() throws Exception {
        assertNotNull(this.clientBrowsingProcess);
    }
}
