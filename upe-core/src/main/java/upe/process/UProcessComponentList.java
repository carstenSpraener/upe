package upe.process;

public interface UProcessComponentList<T extends UProcessComponent> extends UProcessComponent {
    void add(T element);
    void remove(T element);
    T getAt(int idx);
    int size();
}
