package upe.binding.lanterna;

import com.googlecode.lanterna.gui2.TextBox;
import upe.process.*;
import upe.process.rules.UpeRuleVetoException;

public class LanternaTextBoxBinding implements UProcessElementListener, Binding {
    TextBox textBox = new TextBox();
    UProcessTextField pField;

    public LanternaTextBoxBinding(TextBox textBox, UProcessTextField textField) {
        this.pField = textField;
        this.textBox = textBox;
        textBox.setTextChangeListener(this::onTextChanged);
        this.pField.addProcessElementListener(this);
        updateFromProcess();
    }

    private void onTextChanged(String newText, boolean changedByUserInteraction) {
        try(UProcessModification mod = new UProcessModification(this.pField.getProcess())) {
            this.pField.setStringValue(newText);
        }
    }

    @Override
    public void elementChanged(UProcessElement abstractUProcessElementImpl) throws UpeRuleVetoException {
        updateFromProcess();
    }

    private void updateFromProcess() {
        this.textBox.setText(this.pField.getValueForFrontend());
        this.textBox.setEnabled(this.pField.isEnabled());
        this.textBox.setVisible(this.pField.isVisible());
    }

    public void unbind() {
        this.textBox.setTextChangeListener(null);
        this.pField.removeProcessElementListener(this);
    }
}
