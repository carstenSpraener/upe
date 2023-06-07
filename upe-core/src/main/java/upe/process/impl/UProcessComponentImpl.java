package upe.process.impl;

import upe.annotations.AnnotatedProcessConfigurator;
import upe.exception.UPERuntimeException;
import upe.incubator.process.impl.GenericUProcessImpl;
import upe.process.UProcessComponent;
import upe.process.UProcessComponentList;
import upe.process.UProcessElement;
import upe.process.UProcessField;
import upe.process.messages.UProcessMessage;
import upe.process.messages.UProcessMessageImpl;
import upe.process.messages.UProcessMessageStorage;
import upe.process.rules.UProcessRule;
import upe.process.validation.UProcessValidator;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class UProcessComponentImpl extends AbstractUProcessElementImpl implements UProcessComponent {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private long lastLoadTS = Long.MAX_VALUE;

    private TreeMap<String, UProcessElement> name2processElementMap = new TreeMap<>();

    private List<UProcessValidator> myValidators = new ArrayList<>();
    private List<UProcessRule> myRules = new ArrayList<>();

    public UProcessComponentImpl(UProcessComponent parent, String name) {
        super(parent, name);
        AnnotatedProcessConfigurator.parseProcessAnnotation(this);
    }

    public UProcessComponentImpl(String name) {
        this(null, name);
    }

    public List<UProcessElement> getProcessElements() {
        List<UProcessElement> result = new ArrayList<>();
        getProcessElements(result);
        return result;
    }

    public List<UProcessElement> getProcessElements(List<UProcessElement> resultList) {
        for (UProcessElement pe : name2processElementMap.values()) {
            resultList.add(pe);
            if (pe instanceof UProcessComponent pc) {
                pc.getProcessElements(resultList);
            }
        }
        return resultList;
    }

    @Override
    public void addProcessElement(String name, UProcessElement pe) {
        name2processElementMap.computeIfAbsent(name, key -> pe);
    }

    @Override
    public UProcessElement getProcessElement(String name) {
        name = compressPath(name);
        int idx;
        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        if ("".equals(name)) {
            return this;
        }
        if ((idx = name.indexOf('/')) != -1) {
            // name is a path. delegate to sub process component
            String roleName = name.substring(0, idx);
            if ("..".equals(roleName)) {
                return parent.getProcessElement(name.substring(idx + 1));
            }
            UProcessComponent subPC = (UProcessComponent) readChildByName(roleName);
            return subPC.getProcessElement(name.substring(idx + 1));
        } else {
            return readChildByName(name);
        }
    }

    @Override
    public <C extends UProcessElement> C getProcessElement(String name, Class<C> clazz) {
        return (C) getProcessElement(name);
    }

    private String compressPath(String aPath) {
        int idx = 0;
        while ((idx = aPath.indexOf("/..")) != -1) {
            String prefix = aPath.substring(0, idx);
            String posfix = aPath.substring(idx + 3);
            if (prefix.indexOf("/") == -1) {
                aPath = posfix;
            } else {
                aPath = prefix.substring(0, prefix.lastIndexOf('/')) + posfix;
            }
        }
        return aPath;
    }

    private UProcessElement readChildByName(final String name) {
        UProcessElement child = null;
        if (isIndexedName(name)) {
            String mapName = removeIndex(name);
            int index = getIndexFromName(name);
            UProcessComponentList<? extends UProcessComponent> list = (UProcessComponentList) name2processElementMap.get(mapName);
            child = list.getAt(index);
        } else {
            child = name2processElementMap.get(name);
        }
        if (child == null) {
            throw new IllegalArgumentException("no such child with name '" + name + "' in process(component) " + getElementPath());
        }
        return child;
    }

    private int getIndexFromName(String name) {
        String number = name.substring(name.indexOf('[') + 1, name.length() - 1);
        return Integer.valueOf(number);
    }

    private boolean isIndexedName(String name) {
        return name.endsWith("]") && name.contains("[");
    }

    private String removeIndex(String name) {
        if (name.indexOf('[') == -1) {
            return name;
        }
        return name.substring(0, name.indexOf('['));
    }

    @Override
    public int getMaximumMessageLevel() {
        int level = super.getMaximumMessageLevel();
        for (UProcessElement p : name2processElementMap.values()) {
            int pLevel = p.getMaximumMessageLevel();
            if (pLevel > level) {
                level = pLevel;
            }
        }
        return level;
    }

    @Override
    public void addValidator(UProcessValidator pv) {
        myValidators.add(pv);
    }

    @Override
    public List<UProcessValidator> getValidators() {
        return myValidators;
    }

    public void scaffold(Class<?> c) {
        try {
            Method[] methods = c.getMethods();
            for (int i = 0; i < methods.length; i++) {
                Method m = methods[i];
                if (isPropertyAccessMehtod(m)) {
                    String fieldName = toFieldName(m);
                    if (!hasProcessElement(fieldName)) {
                        addProcessElement(fieldName, createField(m));
                    }
                }
            }
        } catch (Exception e) {
            throw new UPERuntimeException(e);
        }
    }

    protected boolean isPropertyAccessMehtod(Method m) {
        if (m.getName().startsWith("get")) {
            Class<?> c = m.getDeclaringClass();
            String setterName = "s" + m.getName().substring(1);
            Class<?> type = m.getReturnType();
            try {
                c.getMethod(setterName, type);
                return true;
            } catch (NoSuchMethodException nsmExc) {
                return false;
            }
        }
        if (m.getName().startsWith("is")) {
            Class<?> c = m.getDeclaringClass();
            String setterName = "set" + m.getName().substring(2);
            Class<?> type = m.getReturnType();
            try {
                c.getMethod(setterName, type);
                return true;
            } catch (NoSuchMethodException nsmExc) {
                return false;
            }
        }
        return false;
    }

    protected String toFieldName(Method m) {
        String name = m.getName();
        if (name.startsWith("is")) {
            name = name.substring(2);
        } else {
            name = name.substring(3);
        }
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    protected boolean hasProcessElement(String fieldName) {
        try {
            getProcessElement(fieldName);
            return true;
        } catch (IllegalArgumentException iaExc) {
            return false;
        }
    }

    protected UProcessElement createField(Method m) {
        String typeName = m.getReturnType().getName();
        if (typeName.endsWith("String")) {
            return new UProcessTextFieldImpl(this, toFieldName(m));
        } else if (typeName.endsWith("Integer") || "int".equals(typeName)) {
            return new UProcessDecimalFieldImpl(this, toFieldName(m), UProcessDecimalFieldImpl.INTEGER_FORMAT);
        } else if (typeName.endsWith("Long") || "long".equals(typeName)) {
            return new UProcessDecimalFieldImpl(this, toFieldName(m), UProcessDecimalFieldImpl.INTEGER_FORMAT);
        } else if (typeName.endsWith("BigDecimal")) {
            return new UProcessDecimalFieldImpl(this, toFieldName(m), UProcessDecimalFieldImpl.MONEY_FORMAT);
        } else if (typeName.endsWith("Double") || "double".equals(typeName)) {
            return new UProcessDecimalFieldImpl(this, toFieldName(m), UProcessDecimalFieldImpl.DECIMAL_FORMAT);
        } else if (typeName.endsWith("Float") || "float".equals(typeName)) {
            return new UProcessDecimalFieldImpl(this, toFieldName(m), UProcessDecimalFieldImpl.DECIMAL_FORMAT);
        } else if (typeName.endsWith("Boolean") || "boolean".equals(typeName)) {
            return new UProcessBooleanFieldImpl(this, toFieldName(m));
        } else if (typeName.endsWith("Date")) {
            return new UProcessDateFieldImpl(this, toFieldName(m));
        } else if (isComposition(m)) {
            GenericUProcessImpl subPC = new GenericUProcessImpl(getProcess().getProcessEngine(), toFieldName(m));
            subPC.scaffold(m.getReturnType());
            return subPC;
        } else {
            return new UProcessTextFieldImpl(this, toFieldName(m));
        }
    }

    private boolean isComposition(Method m) {
        return m.getReturnType().getName().startsWith(getClass().getPackageName());
    }

    public void mapToScaffolded(Class<?> scaffoldedInterface, Object obj) throws UProcessMappingException {
        try {
            boolean hasErrors = false;
            Method[] methods = scaffoldedInterface.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                Method m = methods[i];
                hasErrors |= mapToScaffolded(obj, m);
            }
            if (hasErrors) {
                throw new UProcessMappingException("error while mapping from process to scaffolded class", null);
            }
        } catch (Exception e) {
            throw new UProcessMappingException(e.getMessage(), e);
        }
    }

    private boolean mapToScaffolded(Object obj, Method m) throws IllegalAccessException, InvocationTargetException {
        if (isPropertySetMehtod(m)) {
            try {
                return exportProcessValue(obj, m);
            } catch (IllegalArgumentException iaExc) {
                throw new UPERuntimeException(iaExc);
            }
        }
        return false;
    }

    private boolean exportProcessValue(Object obj, Method m) throws IllegalAccessException, InvocationTargetException {
        String fieldName = toFieldName(m);
        UProcessElement pe = getProcessElement(fieldName);
        if (pe != null) {
            if (pe instanceof UProcessField field) {
                Object value = field.getValue();
                Serializable serValue = convertToScaffoldedValue((Serializable) value, m.getParameterTypes()[0]);
                try {
                    m.invoke(obj, serValue);
                } catch (IllegalArgumentException iaExc) {
                    field.addProcessMessage(getIllegalValueMessage(""+value));
                    return true;
                }
            } else if (pe instanceof UProcessComponentImpl component) {
                Object childObj = readChildObject(fieldName, obj);
                component.mapToScaffolded(childObj.getClass(), childObj);
            }
        }
        return false;
    }

    private Object readChildObject(String fieldName, Object obj) {
        try {
            String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method mGetter = obj.getClass().getMethod(getterName);
            return mGetter.invoke(obj);
        } catch (ReflectiveOperationException roXC) {
            throw new UPERuntimeException(roXC);
        }
    }

    protected boolean isPropertySetMehtod(Method m) {
        String name = m.getName();
        if (!name.startsWith("set")) {
            return false;
        }
        Class<?> c = m.getDeclaringClass();
        String getterName = "g" + m.getName().substring(1);
        try {
            c.getMethod(getterName);
            return true;
        } catch (NoSuchMethodException nsmExc) {
            try {
                getterName = "is" + m.getName().substring(3);
                c.getMethod(getterName);
                return true;
            } catch (NoSuchMethodException nsmExc2) {
                return false;
            }
        }
    }

    protected Serializable convertToScaffoldedValue(Serializable value,
                                                    Class<?> paramClass) {
        if (value == null) {
            return null;
        }
        Class<?> valueClass = value.getClass();
        if (valueClass.isAssignableFrom(paramClass)) {
            return value;
        }
        if (Integer.class.equals(paramClass) || int.class.equals(paramClass)
                && valueClass.equals(BigDecimal.class)) {
            return Integer.valueOf(((BigDecimal) value).intValue());
        }
        if (Long.class.equals(paramClass) || long.class.equals(paramClass)) {
            return Long.valueOf(value.toString());
        }
        if (BigDecimal.class.equals(paramClass) && valueClass.equals(String.class)) {
            return new BigDecimal((String) value);
        }
        if (Double.class.equals(paramClass)) {
            return Double.valueOf(value.toString());
        }
        //TODO: Weitere Converter implementieren.
        return value;
    }

    protected UProcessMessage getIllegalValueMessage(String valueAsString) {
        UProcessMessage pm = UProcessMessageStorage.getInstance().getMessage("ILLEGAL_VALUE_MESSAGE");
        if (pm == null) {
            UProcessMessageStorage.getInstance().storeMessage(
                    new UProcessMessageImpl("ILLEGAL_VALUE_MESSAGE",
                            "Value '" + valueAsString + "' could not  be converted.", UProcessMessage.MESSAGE_LEVEL_ERROR));
        }
        return pm;
    }

    /**
     * Takes the data from the other component but checks before if the data inside
     * this component may be changed since last load.
     * <p>
     * The data is copied by the names from this component. The other component needs to have
     * at least the same child names and each child has to be compatible in that way that a
     * <p>
     * {@code
     * myChild.setValue(otherChild.getVaue())
     * }
     * <p>
     * <p>
     * can succeed.
     *
     * @param otherComponent The component with data to take over
     * @throws UProcessComponentOverwriteException the component contains modified data.
     */
    public void copyFrom(UProcessComponent otherComponent) throws UProcessComponentOverwriteException {
        if (modifiedSinceLastDataLoad()) {
            throw new UProcessComponentOverwriteException();
        }
        overwriteFrom(otherComponent);
    }

    /**
     * The same as copyFrom but without checking for modifications.
     *
     * @param otherComponent The component with data to take over
     * @see copyFrom
     */
    public void overwriteFrom(UProcessComponent otherComponent) {
        for (String childPath : name2processElementMap.keySet()) {
            UProcessElement myChild = name2processElementMap.get(childPath);
            if (myChild instanceof UProcessField myField) {
                UProcessElement otherChild = otherComponent.getProcessElement(childPath);
                if (otherChild != null && otherChild instanceof UProcessField otherField) {
                    myField.setValue(otherField.getValue());
                }
            }
        }
        this.lastLoadTS = System.currentTimeMillis();
        for (UProcessElement child : name2processElementMap.values()) {
            ((AbstractUProcessElementImpl) child).setLastModified(this.lastLoadTS);
        }
        this.setLastModified(this.lastLoadTS);
    }

    public void mapFromScaffolded(Object obj) {
        mapFromScaffolded(obj.getClass(), obj);
    }

    public void mapFromScaffolded(Class<?> scaffoldedInterface, Object obj) {
        try {
            Method[] methods = scaffoldedInterface.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                Method m = methods[i];
                if (isPropertyAccessMehtod(m)) {
                    String fieldName = toFieldName(m);
                    UProcessElement e = getProcessElement(fieldName);
                    if (e == null) {
                        continue;
                    }
                    setProcessValue(obj, m, e);
                }
            }
            this.lastLoadTS = System.currentTimeMillis();
        } catch (Exception e) {
            throw new UProcessMappingException(e.getMessage(), e);
        }
    }

    private void setProcessValue(Object obj, Method m, UProcessElement e) throws IllegalAccessException, InvocationTargetException {
        Object value = m.invoke(obj);
        if (e instanceof UProcessField field) {
            field.setValue((Serializable) value);
        } else if (e instanceof UProcessComponentImpl pc) {
            pc.mapFromScaffolded(value.getClass(), value);
        } else if (e instanceof UProcessComponentListImpl pList) {
            if( value instanceof Iterable<?> it ) {
                for( Object listItem : it ) {
                    if( it != null ) {
                        ((UProcessComponentImpl) pList.createNewInstance()).mapFromScaffolded(it);
                    }
                }
            } else {
                throw new UProcessMappingException("Try to mapp not iterable "+value.getClass()+" into process list "+e.getElementPath());
            }
        }
    }

    @Override
    public void doValidation() {
        for (UProcessValidator pv : getValidators()) {
            pv.validate(this);
        }
        for (UProcessElement pe : name2processElementMap.values()) {
            if (pe instanceof UProcessComponentImpl pc) {
                pc.doValidation();
            }
        }
    }

    @Override
    public void addRule(UProcessRule rule) {
        this.myRules.add(rule);
    }

    @Override
    public List<UProcessRule> getRulesRecursive(List<UProcessRule> collectorList) {
        collectorList.addAll(this.myRules);
        for (UProcessElement pe : name2processElementMap.values()) {
            if (pe instanceof UProcessComponent upc) {
                upc.getRulesRecursive(collectorList);
            }
        }
        return collectorList;
    }

    public void setFieldValue(String path, String valueFromFrontend) {
        ((UProcessField) getProcessElement(path)).setValueFromFrontend(valueFromFrontend);
    }

    public String getFieldValue(String path) {
        return ((UProcessField) getProcessElement(path)).getValueForFrontend();
    }

    @Override
    public boolean modifiedSince(long timeStamp) {
        if (super.modifiedSince(timeStamp)) {
            return true;
        }
        for (UProcessElement child : this.name2processElementMap.values()) {
            if (child.modifiedSince(timeStamp)) {
                return true;
            }
        }
        return false;
    }

    public long getLastLoadTS() {
        return lastLoadTS;
    }

    public boolean modifiedSinceLastDataLoad() {
        return modifiedSince(getLastLoadTS());
    }

    @Override
    public void resetModificationTracking() {
        super.resetModificationTracking();
        for (UProcessElement child : this.name2processElementMap.values()) {
            child.resetModificationTracking();
        }
    }

    public void resetAllValues() {
        for( UProcessElement pe : this.name2processElementMap.values() ) {
            if( pe instanceof UProcessField field ) {
                field.setValue(null);
            }
        }
        this.resetModificationTracking();
    }
}
