package upe.sample.lanterna;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.TextBox;
import upe.binding.lanterna.Binding;
import upe.binding.lanterna.LanternaTextBoxBinding;
import upe.binding.lanterna.UpeLanternaPanel;
import upe.process.UProcess;
import upe.process.UProcessTextField;

import java.util.ArrayList;
import java.util.List;

public class LanternaHelloWorldPanel  extends UpeLanternaPanel {
    private TextBox contentBox = new TextBox(new TerminalSize(40,1));
    private List<Binding> myBindings = new ArrayList<>();

    public LanternaHelloWorldPanel() {
        withBorder(Borders.singleLine("Welcome to the sample application"));

        setLayoutManager(new GridLayout(2));
        addComponent(new Label("Greetings"));
        addComponent(contentBox);

    }
    @Override
    public void bindToProcess(UProcess process) {
        this.myBindings.add(new LanternaTextBoxBinding(this.contentBox, process.getProcessElement("content", UProcessTextField.class)));
    }


    @Override
    public void unbindFromProcess() {
        this.myBindings.forEach(b -> b.unbind());
    }

}
