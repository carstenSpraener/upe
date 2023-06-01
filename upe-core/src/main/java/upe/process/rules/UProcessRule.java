package upe.process.rules;

import upe.process.UProcess;

public interface UProcessRule {
    void valuesChanged(UProcess activeProcess);
    boolean interestedIn(String elementPath);
}
