package upe.test.jupiter;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import upe.backend.UProcessBackend;
import upe.process.ApplicationConfiguration;
import upe.process.UProcessEngine;
import upe.process.engine.BaseUProcessEngine;
import upe.test.TestUProcessEngine;
import upe.test.annotations.UInject;
import upe.test.annotations.UpeBackendComponent;
import upe.test.annotations.UpeProcessToTest;

import java.lang.reflect.Field;
import java.util.HashMap;

public class UpeTestExtension  implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        try {
            ApplicationConfiguration.getInstance().readApplication(testInstance.getClass());
            TestUProcessEngine engine = new TestUProcessEngine();

            for (Field field : testInstance.getClass().getDeclaredFields()) {
                if( field.isAnnotationPresent(UpeBackendComponent.class)) {
                    String name = field.getAnnotation(UpeBackendComponent.class).value();
                    if( "".equals(name) ) {
                        name = field.getName();
                    }
                    UProcessBackend.addSupplier(name, new FieldReflectionBackendSupplier(field, testInstance));
                }
                if (UProcessEngine.class.isAssignableFrom(field.getType()) && field.isAnnotationPresent(UInject.class)) {
                    field.setAccessible(true);
                    field.set(testInstance, engine);
                }
            }
            for(Field field : testInstance.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(UpeProcessToTest.class)) {
                    injectTestProcess(engine, testInstance, field);
                }
            }
        } catch( ReflectiveOperationException roXc ) {
            throw new IllegalArgumentException(roXc);
        }
    }

    private void injectTestProcess(BaseUProcessEngine engine, Object testInstance, Field field) throws IllegalAccessException {
        UpeProcessToTest annotation = field.getAnnotation(UpeProcessToTest.class);
        String name = annotation.value();
        if( name==null || "".equals(name)) {
            name = field.getName();
        }
        engine.callProcess(name, new HashMap<>(), null);
        field.setAccessible(true);
        field.set(testInstance, engine.getActiveProcessInfo().getProcess());
    }
}
