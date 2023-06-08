package upe.process.testapp;

import upe.annotations.UpeScaffolds;
import upe.process.UProcessComponent;
import upe.process.impl.UProcessComponentImpl;
import upe.process.testapp.dto.AddressDto;

@UpeScaffolds(AddressDto.class)
public class AddressEditor  extends UProcessComponentImpl {

    public AddressEditor(UProcessComponent parent, String name) {
        super(parent, name);
    }
}
