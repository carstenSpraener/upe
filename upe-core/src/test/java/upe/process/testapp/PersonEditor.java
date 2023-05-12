package upe.process.testapp;


import upe.annotations.UpeScaffolds;
import upe.process.testapp.dto.PersonDto;
import upe.process.UProcessComponent;
import upe.process.impl.UProcessComponentImpl;

/**
 * Example on how to use a process component to scaffold a pojo.
 */
@UpeScaffolds(PersonDto.class)
public class PersonEditor extends UProcessComponentImpl {
    public PersonEditor(UProcessComponent parent, String name) {
        super(parent, name);
    }
}
