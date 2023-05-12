package upe.exception;

/**
 * Interface to reduce the stack trace from all the "Caused by..."
 */
public interface UPEExceptionHolder {
    Throwable getRootException();
}
