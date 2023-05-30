package upe.test.jupiter;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public class FieldReflectionBackendSupplier implements Supplier<Object> {
    private Field field;
    private Object testInstance;

    public FieldReflectionBackendSupplier(Field field, Object testInstance) {
        this.field = field;
        this.testInstance = testInstance;
    }

    @Override
    public Object get() {
        try {
            field.setAccessible(true);
            return this.field.get(this.testInstance);
        }catch( ReflectiveOperationException roXC ) {
            throw new RuntimeException(roXC);
        }
    }
}
