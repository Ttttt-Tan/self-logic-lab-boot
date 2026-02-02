package org.self.lab.exception;

/**
 * 统一业务层抛出的异常
 */

public class SelfBusinessException extends RuntimeException {

    public SelfBusinessException(String message) {
        super(message);
    }
}
