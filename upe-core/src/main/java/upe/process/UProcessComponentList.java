package upe.process;

import upe.process.impl.AbstractUProcessElementImpl;

public interface UProcessComponentList<T extends UProcessComponent> extends UProcessComponent {
    void add(T element);
    void remove(T element);
    T getAt(int idx);
    int size();
    T createNewInstance();

    int indexOf(UProcessElement element);
}
