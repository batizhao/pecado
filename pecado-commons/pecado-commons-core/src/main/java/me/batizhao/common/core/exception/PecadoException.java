package me.batizhao.common.core.exception;

/**
 * @author batizhao
 * @since 2020-03-20
 **/
public class PecadoException extends RuntimeException {

    public PecadoException(String message) {
        super(message);
    }

    public PecadoException(String message, Throwable cause) {
        super(message, cause);
    }
}
