package upe.process.testapp;


import upe.annotations.UpeProcessComponent;
import upe.annotations.UpeScaffolds;
import upe.process.UProcessComponentList;
import upe.process.testapp.dto.PersonDto;
import upe.process.UProcessComponent;
import upe.process.impl.UProcessComponentImpl;

/**
 * Example on how to use a process component to scaffold a pojo.
 */
@UpeScaffolds(PersonDto.class)
public class PersonEditor extends UProcessComponentImpl {
    @UpeProcessComponent
    AddressEditor addressEditor;

    @UpeProcessComponent(listType = AddressEditor.class)
    UProcessComponentList<AddressEditor> otherAddresses;

    public PersonEditor(UProcessComponent parent, String name) {
        super(parent, name);
    }
}
