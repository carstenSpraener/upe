package upe.resource.testprocess;

import upe.process.UProcessComponent;
import upe.process.UProcessElementSystem;
import upe.process.UProcessTextField;
import upe.process.impl.UProcessComponentImpl;
import upe.process.validation.impl.MandantoryValidator;

public class AdressEditor extends UProcessComponentImpl {
    private UProcessTextField strasseField;

    public AdressEditor(UProcessComponent parent, String name) {
        super(parent, name);
        this.strasseField = UProcessElementSystem.getProcessElementFactory().newTextField(this, "strasse");
        this.addValidator(new MandantoryValidator("strasse"));
    }

}
