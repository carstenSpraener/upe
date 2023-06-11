package upe.sample.lanterna;

import com.googlecode.lanterna.gui2.*;
import upe.binding.lanterna.Binding;
import upe.binding.lanterna.LanternaButtonActionBinding;
import upe.binding.lanterna.LanternaTextBoxBinding;
import upe.binding.lanterna.UpeLanternaPanel;
import upe.process.UProcess;
import upe.process.UProcessAction;
import upe.process.UProcessTextField;

import java.util.ArrayList;
import java.util.List;

public class LanternaRegistrationPanel extends UpeLanternaPanel {
    private TextBox userTextBox = new TextBox();
    private TextBox passwordTextBox = new TextBox();
    private TextBox passsword2TextBox = new TextBox();
    private Button okButton = new Button("OK");
    private Button cancelButton = new Button("Cancel");
    private List<Binding> myBindings = new ArrayList<>();

    public LanternaRegistrationPanel() {
        withBorder(Borders.singleLine("Register to sample application"));

        setLayoutManager(new GridLayout(2));

        addComponent(new Label("Username"));
        addComponent(userTextBox);

        addComponent(new Label("Password"));
        addComponent(passwordTextBox);

        addComponent(new Label("retype password"));
        addComponent(passsword2TextBox);

        addComponent(okButton);
        addComponent(cancelButton);
    }

    @Override
    public void bindToProcess(UProcess process) {
        this.myBindings.add(new LanternaTextBoxBinding(this.userTextBox, process.getProcessElement("user", UProcessTextField.class)));
        this.myBindings.add(new LanternaTextBoxBinding(this.passwordTextBox, process.getProcessElement("password", UProcessTextField.class)));
        this.myBindings.add(new LanternaTextBoxBinding(this.passsword2TextBox, process.getProcessElement("password2", UProcessTextField.class)));
        this.myBindings.add(new LanternaButtonActionBinding(this.okButton, process.getProcessElement("actRegisterOK", UProcessAction.class)));
        this.myBindings.add(new LanternaButtonActionBinding(this.cancelButton, process.getProcessElement("actRegisterCancel", UProcessAction.class)));
    }

    @Override
    public void unbindFromProcess() {
        this.myBindings.forEach(b -> b.unbind());
    }
}
