package upe.exception;

/**
 * This exception is used inside UPE to wrap other exceptions whenever it is necessary.
 * <p>
 * It reduces the stack trace of UPE-Exceptions so that only the root cause is visible.
 */
public class UPERuntimeException extends RuntimeException implements UPEExceptionHolder {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final Throwable rootException;
    private final String message;

    public UPERuntimeException( String message ) {
        this(message, null);
    }

    public UPERuntimeException(Throwable t) {
        super(t);
        if (t instanceof UPEExceptionHolder holder) {
            rootException = holder.getRootException() == null ? t : holder.getRootException();
        } else {
            rootException = t;
        }
        this.message = "";
    }

    public UPERuntimeException(String message, Throwable t) {
        super(t);
        if (t instanceof UPEExceptionHolder holder) {
            rootException = holder.getRootException() == null ? t : holder.getRootException();
        } else {
            rootException = t;
        }
        this.message = message;
    }

    public Throwable getRootException() {
        return rootException;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
