package com.github.fashionbrot.function;


@FunctionalInterface
public interface TokenExpiredFunction {

    /**
     * token 过期执行
     */
    void throwException(Exception exception);

}
