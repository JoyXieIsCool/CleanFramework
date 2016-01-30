package xyz.joeyxie.framework.proxy;

/**
 * 代理接口
 * Created by joey on 2016/1/29.
 */
public interface Proxy {

    /**
     * 执行链式代理
     */
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}
