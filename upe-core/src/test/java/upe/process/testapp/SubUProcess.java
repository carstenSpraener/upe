package upe.process.testapp;

import upe.annotations.UpeProcess;
import upe.annotations.UpeProcessAction;
import upe.process.UProcessEngine;
import upe.process.impl.AbstractUProcessImpl;


import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This process is for testing sub processes. It provides two actions. actFinish and actCancel.
 * <p>
 * If the test call "actFinish" on this process, the process terminates and gives the value
 * "FINISHED" under the key "result" in the resulting map.
 * <p>
 * If the test call "actCancle" on this process, the process terminates and gives the value
 * "CANCELED" under the key "result" in the resulting map.
 * <p>
 * The tests using this process demonstrates how to call processes and react on the result.
 */
@UpeProcess("SubProcess")
public class SubUProcess extends AbstractUProcessImpl {
    private Logger LOGGER = Logger.getLogger(SubUProcess.class.getSimpleName());

    public SubUProcess(UProcessEngine pe, String name) {
        super(pe, name);
    }

    @Override
    public void initialize(Map<String, Object> args) {
        LOGGER.info("initialize() called");
    }

    @Override
    public Map<String, Object> finish() {
        LOGGER.info("finish() called");
        Map<String, Object> resultMap = new HashMap();
        resultMap.put("result", "FINISHED");
        return resultMap;
    }

    @Override
    public Map<String, Object> cancel() {
        LOGGER.info("cancel() called");
        Map<String, Object> resultMap = new HashMap();
        resultMap.put("result", "CANCELED");
        return resultMap;
    }

    @UpeProcessAction("actClose")
    public Object actClose(Map<String, Object> args) {
        getProcessEngine().finishProcess();
        return null;
    }


    @UpeProcessAction("actCancel")
    public Object actCancel(Map<String, Object> args) {
        getProcessEngine().cancelProcess();
        return null;
    }
}
