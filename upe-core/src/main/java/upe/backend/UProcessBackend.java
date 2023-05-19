package upe.backend;

import upe.annotations.UpeBackendFacade;
import upe.exception.UPEConfigurationException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class UProcessBackend implements AutoCloseable {
    private static ThreadLocal<UProcessBackend> myInstance = new ThreadLocal<>();

    private static Map<String, Supplier<?>> supplierMap = new HashMap<>();
    private UProcessBackend(){}

    public static UProcessBackend getInstance() {
        UProcessBackend result = myInstance.get();
        if( result == null ) {
            result = new UProcessBackend();
            myInstance.set(result);
        }
        return result;
    }

    public <F> F provide(Class<? extends F> clazz) {
        UpeBackendFacade facade = clazz.getAnnotation(UpeBackendFacade.class);
        String facadeName = facade.value();
        Supplier<F> supplier = (Supplier<F>) findProviderForFacade(facadeName);
        if( supplier == null ) {
            throw new UPEConfigurationException("No supplier for backend facade "+clazz.getName());
        } else {
            return supplier.get();
        }
    }

    private static Supplier<?> findProviderForFacade(String facadeName) {
        return supplierMap.get(facadeName);
    }

    public static void addSupplier(String name, Supplier<?> supplier) {
        synchronized(supplierMap) {
            supplierMap.put(name, supplier);
        }
    }

    @Override
    public void close()  {
        myInstance.remove();
    }
}
