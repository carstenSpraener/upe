package upe.process.impl;

import upe.process.UProcessComponent;
import upe.process.UProcessComponentList;
import upe.process.UProcessElement;

import java.util.ArrayList;
import java.util.List;

public class UProcessComponentListImpl<T extends UProcessComponent> extends UProcessComponentImpl implements UProcessComponentList<T> {
    private List<T> elementList = new ArrayList<>();

    public UProcessComponentListImpl(UProcessComponent parent, String name ) {
        super(parent, name);
    }

    @Override
    public void add(T element) {
        elementList.add(element);
    }

    @Override
    public void remove(T element) {
        elementList.remove(element);
    }

    @Override
    public T getAt(int idx) {
        return elementList.get(idx);
    }

    @Override
    public int size() {
        return 0;
    }
}
