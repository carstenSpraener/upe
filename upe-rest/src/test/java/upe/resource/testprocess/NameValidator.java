package upe.resource.testprocess;

import upe.process.UProcessComponent;
import upe.process.UProcessField;
import upe.process.messages.UProcessMessage;
import upe.process.messages.UProcessMessageImpl;
import upe.process.validation.impl.UProcessValidatorSupport;

public class NameValidator extends UProcessValidatorSupport {
    private static UProcessMessage nameMissing = new UProcessMessageImpl("0001", "Bitte einen Namen eingeben", UProcessMessage.MESSAGE_LEVEL_ERROR);

    public NameValidator() {
        super();
    }

    @Override
    public void validate(UProcessComponent pc) {
        UProcessField nameField = (UProcessField) pc.getProcessElement("name");
        String value = nameField.getValueForFrontend();
        if( value==null || "".equals(value) ) {
            if( !nameField.getMessages().contains(nameMissing) ) {
                nameField.getMessages().add(nameMissing);
            }
        } else {
            nameField.getMessages().remove(nameMissing);
        }
    }
}
