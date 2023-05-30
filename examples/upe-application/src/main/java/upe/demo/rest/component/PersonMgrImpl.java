package upe.demo.rest.component;

import org.springframework.stereotype.Component;
import upe.annotations.UpeBackendFacade;
import upe.demo.process.backend.PersonMgr;
import upe.demo.process.dto.Address;
import upe.demo.process.dto.Person;

import java.util.ArrayList;
import java.util.List;

@Component("PersonMgrImpl")
public class PersonMgrImpl implements PersonMgr {
    private Person tstPerson = new Person();

    public PersonMgrImpl() {
        tstPerson.setFirstName("Carsten");
        tstPerson.setName("Spraener");
        tstPerson.setAge(55);
        tstPerson.setId(1L);
        tstPerson.setGender("male");
        tstPerson.setAddress(new Address());
        tstPerson.getAddress().setStreet("Kirchesch");
        tstPerson.getAddress().setNumber("6");
        tstPerson.getAddress().setCity("Alfhausen");
        tstPerson.getAddress().setZipCode("49594");
        tstPerson.getAddress().setCountry("Germany");
    }

    @Override
    public Person createPerson() {
        return new Person();
    }

    @Override
    public Person findByID(String id) {
        return tstPerson;
    }

    @Override
    public List<Person> findAll() {
        List<Person> prsList = new ArrayList<>();
        prsList.add(tstPerson);
        return prsList;
    }

    @Override
    public Person savePerson(Person value) {
        return value;
    }

    @Override
    public Person deletePerson(Person value) {
        return value;
    }

    @Override
    public Person deleteById(String id) {
        return tstPerson;
    }


}
