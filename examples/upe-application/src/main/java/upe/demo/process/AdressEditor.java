package upe.demo.process;

import upe.annotations.UpeProcessField;
import upe.process.UProcessComponent;
import upe.process.UProcessTextField;
import upe.process.impl.UProcessComponentImpl;

public class AdressEditor extends UProcessComponentImpl {
    @UpeProcessField("strasse")
    private UProcessTextField strasseField;

    public AdressEditor(UProcessComponent parent, String name) {
        super(parent, name);
    }

}
