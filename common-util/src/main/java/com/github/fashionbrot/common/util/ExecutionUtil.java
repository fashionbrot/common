package com.github.fashionbrot.common.util;

import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ExecutionUtil {

    /**
     * 执行一个带有超时限制的任务，并返回任务的结果。如果任务在指定的超时时间内未完成，
     * 则返回默认值。
     *
     * @param <T>         任务结果的类型
     * @param callable    要执行的任务
     * @param timeout     超时时间
     * @param timeUnit    超时时间的时间单位
     * @param defaultValue 当任务超时时返回的默认值
     * @return            任务的结果，如果任务超时则返回默认值
     */
    public static <T> T executeWithTimeout(Callable<T> callable,
                                           long timeout,
                                           TimeUnit timeUnit,
                                           T defaultValue) {
        try {
            Future<T> future = Executors.newSingleThreadExecutor().submit(callable);
            return future.get(timeout, timeUnit);
        } catch (Exception e) {
            return defaultValue;
        }
    }


    /**
     * 异步执行操作并处理回调
     * @param asyncOperation 异步操作
     * @param successCallback 成功回调函数
     * @param errorCallback 失败回调函数
     * @param <T> 操作结果类型
     */
    public static <T> void executeAsync(Supplier<T> asyncOperation,
                                        Consumer<T> successCallback,
                                        Consumer<Throwable> errorCallback) {
        CompletableFuture<T> future = CompletableFuture.supplyAsync(asyncOperation);
        future.thenAccept(successCallback)
                .exceptionally(error -> {
                    errorCallback.accept(error);
                    return null;
                });
    }

}
