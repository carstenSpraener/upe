package upe.test;

import org.opentest4j.AssertionFailedError;

public class UPEAssertionException extends AssertionFailedError {
    public UPEAssertionException(String msg) {
        super(msg);
    }
}
