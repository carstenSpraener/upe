package upe.process;

import org.junit.Before;
import org.junit.Test;
import upe.process.impl.UProcessComponentImpl;
import upe.process.testapp.AddressEditor;
import upe.process.testapp.Application;
import upe.process.testapp.DerivedUProcess;
import upe.process.testapp.PersonEditor;
import upe.process.testapp.dto.AddressDto;
import upe.process.testapp.dto.PersonDto;

import java.util.Date;

import static org.junit.Assert.*;

public class UPEScaffoldingTest {
    UProcess activeUProcess;

    @Before
    public void setup() {
        activeUProcess = Application.createTestProcess();
    }

    @Test
    public void testScaffoldingProcessCreation() throws Exception {
        assertNotNull(activeUProcess.getProcessElement("/personList[0]/name"));
        assertNotNull(activeUProcess.getProcessElement("/personList[0]/address"));
        assertNotNull(activeUProcess.getProcessElement("/personList[0]/address/street"));
    }

    @Test
    public void testMappingFromDto() throws Exception {
        PersonDto prs = new PersonDto();
        prs.setName("Carsten");
        prs.setAddress(new AddressDto());
        prs.getAddress().setStreet("Kirchesch");

        UProcessComponentImpl prsEditor = (UProcessComponentImpl) activeUProcess.getProcessElement("/personList[0]");
        prsEditor.mapFromScaffolded(PersonDto.class, prs);

        assertEquals("Carsten", ((UProcessField) activeUProcess.getProcessElement("/personList[0]/name")).getValueForFrontend());
        assertEquals("Kirchesch", ((UProcessField) activeUProcess.getProcessElement("/personList[0]/address/street")).getValueForFrontend());
    }

    @Test
    public void testMappingToDto() throws Exception {
        UProcessComponentImpl prsEditor = (UProcessComponentImpl) activeUProcess.getProcessElement("/personList[0]");

        prsEditor.setFieldValue("name", "Carsten");
        prsEditor.setFieldValue("address/street", "Kirchesch");

        PersonDto prs = new PersonDto();
        prs.setAddress(new AddressDto());

        prsEditor.mapToScaffolded(PersonDto.class, prs);
        assertEquals("Carsten", prs.getName());
        assertEquals("Kirchesch", prs.getAddress().getStreet());
    }

    @Test
    public void testCreationOfProcessList() throws Exception {
        // Given: A PersonEditor scaffolds a PersonDTO. PersonDTO references 0..n AddressDTO on
        // otherAddresses

        // when retrieving a person editor
        DerivedUProcess proc = new DerivedUProcess(null, "root");
        PersonEditor uut = new PersonEditor(proc, "personEditor");

        // then:
        // the personEditor should have a ProcessElement "otherAddresses"
        UProcessElement otherAddresses = uut.getProcessElement("otherAddresses");
        assertNotNull(otherAddresses);
        // the processElement should be a UProcessComponentList
        assertTrue(otherAddresses instanceof UProcessComponentList<?>);
    }


    @Test
    public void testPersonEditorReadFromScaffolded() throws Exception {
        // when retrieving a person editor
        DerivedUProcess proc = new DerivedUProcess(null, "root");
        PersonEditor uut = new PersonEditor(proc, "personEditor");
        PersonDto prs = new PersonDto();
        prs.setName("Doe");
        prs.setDateOfBirth(new Date());
        prs.setSurname("John");
        prs.setHeightCM(183);

        AddressDto adr = new AddressDto();
        adr.setCity("New York");
        adr.setStreet("5th Avenue");
        adr.setCountry("USA");
        adr.setNumber("55 a");
        prs.setAddress(adr);

        adr = new AddressDto();
        adr.setCity("New York");
        adr.setStreet("5th Avenue");
        adr.setCountry("USA");
        adr.setNumber("55 b");

        prs.getOtherAddresses().add(adr);
        adr = new AddressDto();
        adr.setCity("New York");
        adr.setStreet("5th Avenue");
        adr.setCountry("USA");
        adr.setNumber("55 c");
        prs.getOtherAddresses().add(adr);

        uut.mapFromScaffolded(prs);

        assertEquals("John", uut.getProcessElement("surname", UProcessTextField.class).getStringValue());
        assertEquals("Doe", uut.getProcessElement("name", UProcessTextField.class).getStringValue());
        assertEquals(183, uut.getProcessElement("heightCM", UProcessDecimalField.class).getDecimalValue().intValue());

        assertEquals("New York", uut.getProcessElement("address/city", UProcessTextField.class).getStringValue());
        assertEquals("5th Avenue", uut.getProcessElement("address/street", UProcessTextField.class).getStringValue());
        assertEquals("55 a", uut.getProcessElement("address/number", UProcessTextField.class).getStringValue());
        assertEquals("USA", uut.getProcessElement("address/country", UProcessTextField.class).getStringValue());

        assertEquals("55 b", uut.getProcessElement("otherAddresses[0]/number", UProcessTextField.class).getStringValue());
        assertEquals("55 c", uut.getProcessElement("otherAddresses[1]/number", UProcessTextField.class).getStringValue());
    }
}
