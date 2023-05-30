package upe.process.impl;

import upe.exception.UPERuntimeException;
import upe.process.UProcessComponent;
import upe.process.UProcessComponentList;
import upe.process.UProcessElement;

import java.util.ArrayList;
import java.util.List;

public class UProcessComponentListImpl<T extends UProcessComponent> extends UProcessComponentImpl implements UProcessComponentList<T> {
    private List<T> elementList = new ArrayList<>();
    private Class<? extends T> myClazz = null;

    public UProcessComponentListImpl(UProcessComponent parent, String name, Class<? extends T> clazz ) {
        super(parent, name);
        this.myClazz = clazz;
        try {
            myClazz.getConstructor(UProcessComponent.class, String.class);
        } catch( ReflectiveOperationException roXc ) {
            throw new UPERuntimeException(roXc);
        }
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

    public T createNewInstance() {
        try {
            T uc = myClazz.getConstructor(UProcessComponent.class, String.class).newInstance(this, this.getName());
            this.elementList.add(uc);
            return uc;
        } catch( ReflectiveOperationException e ) {
            throw new UPERuntimeException(e);
        }
    }

    @Override
    public int indexOf(UProcessElement member) {
        return elementList.indexOf(member);
    }
}
