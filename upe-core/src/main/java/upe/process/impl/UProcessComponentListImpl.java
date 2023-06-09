package upe.process.impl;

import upe.annotations.UpeScaffolds;
import upe.exception.UPERuntimeException;
import upe.process.UProcessComponent;
import upe.process.UProcessComponentList;
import upe.process.UProcessElement;

import java.util.ArrayList;
import java.util.List;

public class UProcessComponentListImpl<T extends UProcessComponent> extends UProcessComponentImpl implements UProcessComponentList<T> {
    private List<T> elementList = new ArrayList<>();
    private Class<? extends T> myClazz = null;
    private Class<?> scaffolededClazz = null;

    public UProcessComponentListImpl(UProcessComponent parent, String name, Class<? extends T> clazz ) {
        super(parent, name);
        this.myClazz = clazz;
        this.scaffolededClazz = readScaffoldedClass(clazz);
        try {
            myClazz.getConstructor(UProcessComponent.class, String.class);
        } catch( ReflectiveOperationException roXc ) {
            throw new UpeScaffoldingException("The inner process element "+myClazz.getName()+" of the list "+name+" does  not provide the required constructor (UProcessComponent parent, String name).");
        }
        try {
            this.scaffolededClazz.getConstructor();
        } catch( ReflectiveOperationException roXC ){
            throw new UpeScaffoldingException("The scaffolded class "+this.scaffolededClazz.getName()+" does not provide a default constructor. Scaffolding not possible.");
        }
    }

    private Class<?> readScaffoldedClass(Class<? extends T> clazz) {
        Class c = clazz;
        while( c!=null ) {
            if (c.isAnnotationPresent(UpeScaffolds.class)) {
                return ((UpeScaffolds)c.getAnnotation(UpeScaffolds.class)).value();
            }
            c = c.getSuperclass();
        }
        return null;
    }

    @Override
    public List<UProcessElement> getProcessElements(List<UProcessElement> resultList) {
        for( UProcessComponent listElement : this.elementList ) {
            resultList.add(listElement);
            listElement.getProcessElements(resultList);
        }
        return resultList;
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
        return elementList.size();
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

    @Override
    public List<T> getComponentList() {
        return this.elementList;
    }

    public Class<?> getScaffoldedClass() {
        return this.scaffolededClazz;
    }

    public Object createScaffolded() {
        try {
            return this.scaffolededClazz.getConstructor().newInstance();
        } catch( ReflectiveOperationException rxOP ) {
            throw new UpeScaffoldingException("Scaffolded class "+this.scaffolededClazz.getName()+" of process element "+getElementPath()+" does not provide a default constructor. Mapping of lists not possible.");
        }
    }
}
