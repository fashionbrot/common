package com.github.fashionbrot.common.function;

@FunctionalInterface
public interface ExceptionSupplier<T> {
    T get() throws Exception;
}

