package upe.exception;

/**
 * The rarely used checked exception in UPE.
 */
public class UPEException extends Exception implements UPEExceptionHolder {
    private final Throwable rootException;

    public UPEException() {
        this.rootException = null;
    }

    public UPEException(String message) {
        super(message);
        this.rootException = null;
    }

    public UPEException(String message, Throwable cause) {
        super(message, cause);
        this.rootException = toRootException(cause);
    }

    public UPEException(Throwable cause) {
        super(cause);
        this.rootException = toRootException(cause);
    }

    public UPEException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.rootException = toRootException(cause);
    }

    private Throwable toRootException(Throwable t) {
        if (t instanceof UPEExceptionHolder holder) {
            return holder.getRootException() == null ? t : holder.getRootException();
        } else {
            return t;
        }
    }

    @Override
    public Throwable getRootException() {
        return rootException;
    }
}
