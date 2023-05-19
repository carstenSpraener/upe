package upe.annotations;

import upe.process.*;
import upe.process.impl.UActionMethodInvoker;
import upe.process.impl.UMethodUProcessAction;
import upe.process.impl.UProcessComponentImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import static upe.process.UProcessEngine.LOGGER;

@SuppressWarnings("java:S3011")
public class AnnotatedProcessConfigurator {

    private AnnotatedProcessConfigurator() {}

    public static void parseProcessAnnotation(UProcessComponent p) {
        UProcessElementFactory peFactory = UProcessElementSystem.getProcessElementFactory();
        for (Field f : collectAnnotatedFields(p)) {
            try {
                UpeProcessField pFieldConfig = f.getAnnotation(UpeProcessField.class);
                f.setAccessible(true);
                f.set(p, createProcessFieldFor(peFactory, p, f, pFieldConfig));
            } catch (ReflectiveOperationException roXC) {
                LOGGER.severe(String.format("Cannot create UProcessElement for field '%s'. Error: %s", f.getName(), roXC.getMessage()));
            }
        }
        for (Method m : collectActionMethods(p)) {
            UpeProcessAction actionConfig = m.getAnnotation(UpeProcessAction.class);
            new UMethodUProcessAction(p, actionConfig.value(), new UActionMethodInvoker(p, m));
        }
        for (Field actField : collectActionFields(p)) {
            UpeProcessAction actionConfig = actField.getAnnotation(UpeProcessAction.class);
            try {
                actField.setAccessible(true);
                actField.set(p, createProcessActionFor(p, actField, actionConfig));
            } catch (ReflectiveOperationException roXC) {
                LOGGER.severe(String.format("Cannot create UProcessElement for field '%s'. Error: %s", actField.getName(), roXC.getMessage()));
            }
        }
        for (Field f : collectAnnotatedComponents(p)) {
            try {
                UpeProcessComponent pcConfig = f.getAnnotation(UpeProcessComponent.class);
                f.setAccessible(true);
                createProcessComponentFor(p, f, pcConfig);
            } catch (ReflectiveOperationException roXC) {
                LOGGER.severe(String.format("Cannot create UProcessComponent for field '%s'. Error: %s", f.getName(), roXC.getMessage()));
            }
        }
        UpeScaffolds scaffolds = findScaffolds(p);
        if( scaffolds!=null ) {
            ((UProcessComponentImpl)p).scaffold(scaffolds.value());
        }
    }

    private static UProcessAction createProcessActionFor(UProcessComponent p, Field actField, UpeProcessAction actionConfig) throws ReflectiveOperationException {
        String name = actionConfig.value();
        if( name==null ||"".equals(name) ) {
            name = actField.getName();
        }
        UProcessAction action = (UProcessAction)actField.getType()
                .getConstructor(UProcessComponent.class, String.class)
                .newInstance(p, name);
        return action;
    }

    private static UpeScaffolds findScaffolds(UProcessComponent p) {
        Class<?> clazz = p.getClass();
        while( !clazz.equals(Object.class) ) {
            if( clazz.isAnnotationPresent(UpeScaffolds.class) ) {
                return clazz.getAnnotation(UpeScaffolds.class);
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    private static UProcessElement createProcessFieldFor(UProcessElementFactory peFactory, UProcessComponent parent, Field f, UpeProcessField pFieldConfig) {
        Class<?> fType = f.getType();
        UProcessElement pe = null;
        String peName = pFieldConfig.value();
        if( peName.equals("") ) {
            peName = f.getName();
        }
        if (fType.equals(UProcessTextField.class)) {
            pe = peFactory.newTextField(parent, peName);
        } else if (fType.equals(UProcessDecimalField.class)) {
            pe = peFactory.newDecimalField(parent, peName);
        } else if (fType.equals(UProcessDateField.class)) {
            pe = peFactory.newDateField(parent, peName);
        } else if (fType.equals(UProcessBooleanField.class)) {
            pe = peFactory.newBooleanField(parent, peName);
        } else if( fType.equals(UProcessComponentList.class) ) {
            pe = peFactory.newProcessComponentList(parent, peName);
        } else {
            try {
                pe = (UProcessElement) fType.getConstructor(UProcessComponent.class, String.class).newInstance(parent, peName);
            } catch (NoSuchMethodException nsmXC) {
                LOGGER.severe("Cannot find a (UProcessComponent parent, String name) constructor on class " + fType.getName());
            } catch (ReflectiveOperationException roXC) {
                LOGGER.severe(String.format("Cannot create instance of %s. Error: %s",fType.getName(),roXC.getMessage()));
            }
        }
        return pe;
    }

    private static List<Field> collectAnnotatedFields(UProcessComponent p) {
        return collectAnnotatedFields(p, UpeProcessField.class);
    }

    private static List<Field> collectActionFields(UProcessComponent p) {
        return collectAnnotatedFields(p, UpeProcessAction.class);
    }

    private static List<Field> collectAnnotatedFields(UProcessComponent p, Class<? extends Annotation> annotation) {
        List<Field> processFields = new ArrayList<>();
        Class<?> clazz = p.getClass();
        while (!clazz.equals(Object.class)) {
            for (Field f : clazz.getDeclaredFields()) {
                if (f.isAnnotationPresent(annotation)) {
                    processFields.add(f);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return processFields;
    }


    private static List<Method> collectActionMethods(UProcessComponent p) {
        List<Method> processMethods = new ArrayList<>();
        Class<?> clazz = p.getClass();
        while (!clazz.equals(Object.class)) {
            for (Method m : clazz.getDeclaredMethods()) {
                if (m.isAnnotationPresent(UpeProcessAction.class)) {
                    processMethods.add(m);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return processMethods;
    }

    private static List<Field> collectAnnotatedComponents(UProcessComponent p) {
        List<Field> processComponentFields = new ArrayList<>();
        Class<?> clazz = p.getClass();
        while (!clazz.equals(Object.class)) {
            for (Field f : clazz.getDeclaredFields()) {
                if (f.isAnnotationPresent(UpeProcessComponent.class)) {
                    processComponentFields.add(f);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return processComponentFields;
    }

    private static void createProcessComponentFor(UProcessComponent parent, Field f, UpeProcessComponent pcConfig) throws ReflectiveOperationException {
        String name = pcConfig.value();
        if( "".equals(name) ) {
            name = f.getName();
        }
        UProcessComponent pc = (UProcessComponent) f.getType().getConstructor(UProcessComponent.class, String.class).newInstance(parent, name);
        f.set(parent, pc);
    }

}
