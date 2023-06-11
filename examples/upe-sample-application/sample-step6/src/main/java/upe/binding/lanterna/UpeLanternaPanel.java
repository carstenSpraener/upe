package upe.binding.lanterna;

import com.googlecode.lanterna.gui2.Panel;
import upe.process.UProcess;

public abstract class UpeLanternaPanel extends Panel {
    public abstract void bindToProcess(UProcess process);

    public void unbindFromProcess() {
    }
}
