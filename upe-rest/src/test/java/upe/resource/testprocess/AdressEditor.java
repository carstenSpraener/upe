package upe.resource.testprocess;

import upe.annotations.UpeProcessField;
import upe.annotations.UpeScaffolds;
import upe.process.UProcessComponent;
import upe.process.UProcessDecimalField;
import upe.process.UProcessElementSystem;
import upe.process.UProcessTextField;
import upe.process.impl.UProcessComponentImpl;
import upe.process.validation.impl.MandantoryValidator;
import upe.resource.testprocess.dto.AddressDTO;

import java.math.BigDecimal;

@UpeScaffolds(AddressDTO.class)
public class AdressEditor extends UProcessComponentImpl {
    @UpeProcessField("rowID")
    private UProcessDecimalField rowID;

    public AdressEditor(UProcessComponent parent, String name) {
        super(parent, name);
        this.addValidator(new MandantoryValidator("street"));
    }

    public BigDecimal getRowIDValue() {
        return getProcessElement("rowID", UProcessDecimalField.class).getDecimalValue();
    }

    public void setRowIDValue(Number n) {
        getProcessElement("rowID", UProcessDecimalField.class).setDecimalValue(BigDecimal.valueOf(n.longValue()));
    }
}
