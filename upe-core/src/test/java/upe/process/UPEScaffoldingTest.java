package upe.process;

import org.junit.Before;
import org.junit.Test;
import upe.process.impl.UProcessComponentImpl;
import upe.process.testapp.Application;
import upe.process.testapp.dto.AddressDto;
import upe.process.testapp.dto.PersonDto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UPEScaffoldingTest {
    UProcess activeUProcess;

    @Before
    public void setup() {
        activeUProcess = Application.createTestProcess();
    }
    @Test
    public void testScaffoldingProcessCreation() throws Exception {
        assertNotNull(activeUProcess.getProcessElement("/person/name"));
        assertNotNull(activeUProcess.getProcessElement("/person/address"));
        assertNotNull(activeUProcess.getProcessElement("/person/address/street"));
    }

    @Test
    public void testMappingFromDto() throws Exception {
        PersonDto prs = new PersonDto();
        prs.setName("Carsten");
        prs.setAddress(new AddressDto());
        prs.getAddress().setStreet("Kirchesch");

        UProcessComponentImpl prsEditor = (UProcessComponentImpl) activeUProcess.getProcessElement("/person");
        prsEditor.mapFromScaffolded(PersonDto.class, prs);

        assertEquals("Carsten", ((UProcessField) activeUProcess.getProcessElement("/person/name")).getValueForFrontend());
        assertEquals("Kirchesch", ((UProcessField) activeUProcess.getProcessElement("/person/address/street")).getValueForFrontend());
    }

    @Test
    public void testMappingToDto() throws Exception {
        UProcessComponentImpl prsEditor = (UProcessComponentImpl) activeUProcess.getProcessElement("/person");

        prsEditor.setFieldValue("name", "Carsten");
        prsEditor.setFieldValue("address/street", "Kirchesch");

        PersonDto prs = new PersonDto();
        prs.setAddress(new AddressDto() );

        prsEditor.mapToScaffolded(PersonDto.class, prs);
        assertEquals("Carsten", prs.getName());
        assertEquals("Kirchesch", prs.getAddress().getStreet());
    }
}
