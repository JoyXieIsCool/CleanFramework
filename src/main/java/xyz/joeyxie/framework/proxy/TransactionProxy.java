package xyz.joeyxie.framework.proxy;

import xyz.joeyxie.framework.annotation.Transaction;
import xyz.joeyxie.framework.helper.DatabaseHelper;
import xyz.joeyxie.framework.util.LogUtil;

import java.lang.reflect.Method;

/**
 * 事务代理类，对于添加了@Transaction注解的方法执行
 * Created by joey on 2016/2/17.
 */
public class TransactionProxy implements Proxy{

    private static final ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };


    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        boolean flag = FLAG_HOLDER.get();

        Method method = proxyChain.getTargetMethod();

        if (!flag && method.isAnnotationPresent(Transaction.class)) {
            FLAG_HOLDER.set(true);
            try {

                // 打开事务
                DatabaseHelper.beginTransaction();
                LogUtil.debug("begin transaction");

                // 执行代理链
                result = proxyChain.doProxyChain();

                // 提交事务
                DatabaseHelper.commitTransaction();
                LogUtil.debug("commit transaction");

            } catch (Exception e) {

                //回滚事务
                DatabaseHelper.rollbackTransaction();
                LogUtil.error("rollback transaction", e);
                throw new RuntimeException(e);

            } finally {
                FLAG_HOLDER.remove();
            }
        } else {
            result = proxyChain.doProxyChain();
        }

        return result;
    }
}
