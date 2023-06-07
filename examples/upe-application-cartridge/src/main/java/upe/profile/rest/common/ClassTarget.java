package upe.profile.rest.common;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ClassTarget {
    private Map<Object, ClassSection> mySectionMap = new TreeMap<>();

    public void addSection(Object key, ClassSection aSection) {
        this.mySectionMap.put(key, aSection);
    }

    public ClassTarget withSection(Object key, ClassSection section) {
        addSection(key, section);
        return this;
    }

    public ClassSection getSection(Object key) {
        return mySectionMap.get(key);
    }

    public String evaluate() {
        StringBuilder sb = new StringBuilder();
        for( ClassSection section : this.mySectionMap.values() ) {
            section.evaluate(sb);
        }
        return sb.toString();
    }
}
