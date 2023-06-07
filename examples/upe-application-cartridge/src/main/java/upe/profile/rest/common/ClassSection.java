package upe.profile.rest.common;

import de.spraener.nxtgen.model.ModelElement;
import upe.profile.rest.common.java.JavaAspects;

public interface ClassSection {
    CodeSnippet getSnippet(Object key);
    void evaluate(StringBuilder sb);
    CodeSnippet getSnippet(Object key, ModelElement me);
}
