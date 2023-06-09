package upe.process.impl;

import upe.exception.UPERuntimeException;

public class UpeScaffoldingException extends UPERuntimeException {
    public UpeScaffoldingException(String msg) {
        super(msg);
    }
    public UpeScaffoldingException(String msg, Throwable reason) {
        super(msg, reason);
    }
}
