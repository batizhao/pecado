package me.batizhao.common.feign.exception;

import lombok.Getter;

/**
 * @author batizhao
 * @since 2020-03-25
 **/
public class PecadoFeignException extends RuntimeException {

    @Getter
    private int status;

    public PecadoFeignException(String message) {
        super(message);
    }

    public PecadoFeignException(String message, int status) {
        super(message);
        this.status = status;
    }

}
