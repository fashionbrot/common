package com.github.fashionbrot.common.util;

public class ThreadUtil {

    public static void sleep(final long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException iex) {
            Thread.currentThread().interrupt();
        }
    }

    public static void wait(final Object obj) {
        synchronized (obj) {
            try {
                obj.wait();
            } catch (InterruptedException inex) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
