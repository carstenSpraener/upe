package upe.resource;

import java.util.Map;

public interface UProcessChangeListener {

    void callProcess(String fromProcess, String toProcess, Map<String, Object> callArgs, String returnActionPath);
    void jumpToProcess(String fromProcess, String toProcess, Map<String, Object> callArgs);
    void finishProcess(String finishedProcessName, Map<String, Object> resultArgs);
    void cancelProcess(String canceledProcessName, Map<String, Object> resultArgs);
}
