package xyz.joeyxie.framework.helper;

import xyz.joeyxie.framework.util.LogUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库操作助手类
 * Created by joey on 2016/2/17.
 */
public final class DatabaseHelper {

    private static ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<Connection>();

    /**
     * 获取当前线程的数据库连接，如果当前线程中不存在则创建数据库连接并保存到线程中
     */
    public static Connection getConnection() {
        Connection conn = CONNECTION_HOLDER.get();

        try {
            // 如果连接不存在则创建
            if (null == conn) {
                Class.forName(ConfigHelper.getJdbcDriver());

                // 这里以后要改用连接池，考虑c3p0
                conn = DriverManager.getConnection(ConfigHelper.getJdbcUrl(),
                        ConfigHelper.getJdbcUsername(),
                        ConfigHelper.getJdbcPassword());
            }
        } catch (Exception e) {
            LogUtil.error("fail to get connection", e);
            e.printStackTrace();
        } finally {
            // 无论创建成功或失败都将 conn 放回容器中
            CONNECTION_HOLDER.set(conn);
        }

        return conn;
    }


    /**
     * 开启事务
     */
    public static void beginTransaction() {

        Connection conn = getConnection();

        if (null != conn) {
            try {
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                LogUtil.error("begin transaction fail.", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
    }

    /**
     * 提交事务
     */
    public static void commitTransaction() {
        Connection conn = getConnection();

        if (null != conn) {
            try {
                conn.commit();
                conn.close();
            } catch (SQLException e) {
                LogUtil.error("commit transaction fail.", e);
                throw new RuntimeException(e);
            } finally {
                // 无论如何都要将 Connection 从当前线程中移除
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction() {
        Connection conn = getConnection();

        if (null != conn) {
            try {
                conn.rollback();
                conn.close();
            } catch (SQLException e) {
                LogUtil.error("rollback transaction fail", e);
                throw new RuntimeException(e);
            } finally {
                // 无论是否成功回滚都需要将该连接从当前线程中移除
                CONNECTION_HOLDER.remove();
            }

        }
    }
}
