package org.storevm.framework.remote.exception;

import lombok.Getter;

/**
 * @author Jack
 */
public class RemoteException extends RuntimeException {
    @Getter
    private int statusCode;

    public RemoteException(Throwable cause) {
        super(cause);
    }

    public RemoteException(int statusCode, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
    }

    public RemoteException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "RemoteException{" +
                "statusCode=" + statusCode + ", message=" + getMessage() +
                '}';
    }
}
