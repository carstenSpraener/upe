package upe.binding.lanterna;

import upe.exception.UPERuntimeException;
import upe.process.UProcessAction;
import upe.process.UProcessEngine;
import upe.process.engine.BaseUProcessEngine;
import upe.process.messages.UProcessMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class LanternaProcessEngine extends BaseUProcessEngine {
    private Map<String, Supplier<UpeLanternaPanel>> processToPanelRegistration = new HashMap<>();
    private LanternaApplicationWindow window = null;

    public LanternaProcessEngine() {
        super();
        try {
            this.window = new LanternaApplicationWindow();
            this.window.init();
        } catch (Exception e ) {
            throw new UPERuntimeException(e);
        }
    }

    public void registerProcessWindow(String processName, Supplier<UpeLanternaPanel> lanternaLoginPanelSupplier) {
        processToPanelRegistration.put(processName, lanternaLoginPanelSupplier);
    }

    @Override
    public void callProcess(String processName, Map<String, Object> processArgs, UProcessAction returnAction) {
        super.callProcess(processName, processArgs, returnAction);
        activateWindowForProcess(processName);
    }

    @Override
    public void jumpToProcess(String processName, Map<String, Object> processArgs) {
        super.jumpToProcess(processName, processArgs);
        activateWindowForProcess(processName);
    }

    private void activateWindowForProcess(String processName) {
        String activeProcessName = getActiveProcess().getName();
        Supplier<UpeLanternaPanel> supplier = this.processToPanelRegistration.get(activeProcessName);
        if( supplier != null ) {
            UpeLanternaPanel panel = supplier.get();
            UProcessEngine.LOGGER.info("Binding panel "+panel.getClass().getSimpleName()+" to process "+getActiveProcess().getName());
            panel.bindToProcess(getActiveProcess());
            this.window.activatePanel(panel);
        } else {
            UProcessEngine.LOGGER.severe("No view registrated for process "+ processName);
            System.exit(-1);
        }
    }

    @Override
    public  Map<String, Object>finishProcess() {
        Map<String, Object> result = super.finishProcess();
        if( hasActiveProcess() ) {
            activateWindowForProcess(getActiveProcess().getName());
        }
        return result;
    }

    @Override
    public Map<String, Object> cancelProcess() {
        Map<String, Object> result = super.cancelProcess();
        if( hasActiveProcess() ) {
            activateWindowForProcess(getActiveProcess().getName());
        }
        return result;
    }

    @Override
    public void queueProcessMessage(UProcessMessage msg) {
        super.queueProcessMessage(msg);
        this.window.showSystemMessage(msg);
    }
}


