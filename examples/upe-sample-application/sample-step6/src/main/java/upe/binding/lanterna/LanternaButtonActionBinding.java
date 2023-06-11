package upe.binding.lanterna;

import com.googlecode.lanterna.gui2.Button;
import upe.process.UProcessAction;
import upe.process.UProcessElement;
import upe.process.UProcessElementListener;
import upe.process.UProcessEngine;

public class LanternaButtonActionBinding implements UProcessElementListener, Button.Listener, Binding {
    private Button lanternaButton;
    private UProcessAction action;

    public LanternaButtonActionBinding(Button lanternaButton, UProcessAction action) {
        this.lanternaButton = lanternaButton;
        this.action = action;
        this.lanternaButton.addListener(this);
        this.action.addProcessElementListener(this);
        updateFromProcess();
    }

    public void elementChanged(UProcessElement uProcessElement) {
        updateFromProcess();
    }

    private void updateFromProcess() {
        UProcessEngine.LOGGER.info("ACTION-BINDING: Setting state Action"+action.getElementPath()+"[enabled="+action.isEnabled()+", visible="+action.isVisible()+"] to Button");
        this.lanternaButton.setEnabled( this.action.isEnabled() );
        this.lanternaButton.setVisible( this.action.isVisible() );
    }

    @Override
    public void onTriggered(Button button) {
        UProcessEngine.LOGGER.info("ACTION-BINDING: Executing action " + action.getElementPath() + " on process " +
                this.action.getProcess().getProcessEngine().getActiveProcess().getName());
        this.action.execute(null);
    }

    public void unbind() {
        this.lanternaButton.removeListener(this);
        this.action.removeProcessElementListener(this);
    }

}
