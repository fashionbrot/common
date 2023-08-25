package com.github.fashionbrot.common.http;

/**
 * @author fashionbrot
 */
/**
 * HTTP 请求回调接口，用于处理 HTTP 请求的结果和异常情况。
 */
public interface HttpCallback {

    /**
     * 当 HTTP 请求成功完成时被调用。
     *
     * @param request  发起的 HTTP 请求对象。
     * @param response HTTP 响应对象，包含响应信息。
     */
    void success(HttpRequest request, HttpResponse response);

    /**
     * 当 HTTP 请求失败时被调用（默认实现为空，可以根据需要覆盖）。
     *
     * @param request  发起的 HTTP 请求对象。
     * @param response HTTP 响应对象，包含响应信息。
     */
    default void failed(HttpRequest request, HttpResponse response) {
        // 默认实现为空，可以在子类中覆盖以自定义失败处理逻辑。
    }

    /**
     * 当 HTTP 请求发生异常时被调用（默认实现为空，可以根据需要覆盖）。
     *
     * @param request   发起的 HTTP 请求对象。
     * @param exception 异常对象，表示发生的异常。
     */
    default void exception(HttpRequest request, Exception exception) {
        // 默认实现为空，可以在子类中覆盖以自定义异常处理逻辑。
    }
}
