package upe.test.jupiter;

import upe.exception.UPERuntimeException;

import java.lang.reflect.Field;
import java.util.function.Supplier;

/**
 * Helper class to use as a supplier for backend mocks.
 */
class FieldReflectionBackendSupplier implements Supplier<Object> {
    private Field field;
    private Object testInstance;

    FieldReflectionBackendSupplier(Field field, Object testInstance) {
        this.field = field;
        this.testInstance = testInstance;
    }

    @Override
    public Object get() {
        try {
            field.setAccessible(true);
            return this.field.get(this.testInstance);
        }catch( ReflectiveOperationException roXC ) {
            throw new UPERuntimeException(roXC);
        }
    }
}
