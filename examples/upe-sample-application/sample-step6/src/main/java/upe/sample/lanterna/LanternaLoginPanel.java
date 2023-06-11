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

public class LanternaLoginPanel extends UpeLanternaPanel {
    private TextBox userTextBox = new TextBox();
    private TextBox passwordTextBox = new TextBox();
    private Button loginButton = new Button("OK");
    private Button registerButton = new Button("Register");
    private List<Binding> myBindings = new ArrayList<>();

    public LanternaLoginPanel() {
        withBorder(Borders.singleLine("Login"));
        setLayoutManager(new GridLayout(2));

        addComponent(new Label("Username"));
        addComponent(userTextBox);

        addComponent(new Label("Password"));
        addComponent(passwordTextBox);

        addComponent(loginButton);
        addComponent(registerButton);
    }

    @Override
    public void bindToProcess(UProcess process) {
        this.myBindings.add(new LanternaTextBoxBinding(this.userTextBox, process.getProcessElement("user", UProcessTextField.class)));
        this.myBindings.add(new LanternaTextBoxBinding(this.passwordTextBox, process.getProcessElement("password", UProcessTextField.class)));
        this.myBindings.add(new LanternaButtonActionBinding(this.loginButton, process.getProcessElement("actLogin", UProcessAction.class)));
        this.myBindings.add(new LanternaButtonActionBinding(this.registerButton, process.getProcessElement("actRegister", UProcessAction.class)));
    }

    @Override
    public void unbindFromProcess() {
        super.unbindFromProcess();
        this.myBindings.forEach(b->b.unbind());
    }
}
