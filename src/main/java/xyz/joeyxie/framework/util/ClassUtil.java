package xyz.joeyxie.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 对类进行操作的工具，包括加载类和把类放到集合中保存
 * Created by joey on 2016/1/6.
 */
public class ClassUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);
    
    /**
     * 获取类加载器
     */
    public static ClassLoader getClassLoader() {
        //获取当前线程的ClassLoader
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 给定类名，加载一个类
     */
    public static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> cls = null;

        try {
            cls = Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class failure", e);
            throw new RuntimeException(e);
        }

        return cls;
    }

    /**
     * 获取指定包名下面的所有类
     */
    public static Set<Class<?>> getClassSet(String packageName) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        try {
            // 在classpath中查找含有packageName的文件，包括普通的包和jar包都会被查找
            Enumeration<URL> urls = Thread.currentThread().
                    getContextClassLoader().getResources(packageName.replace(".", "/"));
            while(urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if(null != url) {
                    String protocol = url.getProtocol();
                    if(FILE_PROTOCOL.equals(protocol)) {
                        // 如果是文件(包括目录)则直接加载
                        String packagePath = url.getPath().replaceAll("%20", " ");
                        addClass(classSet, packagePath, packageName);
                    } else if (JAR_FILE_PROTOCOL.equals(protocol)) {
                        // 如果是jar包则读取包中的class文件再加载
                        JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
                        if(null != jarURLConnection) {
                            JarFile jarFile = jarURLConnection.getJarFile();
                            /**
                             * 获取jar包中所有的条目，甚至包括不是packageName包中的class
                             * 暂时不做过滤处理，因为同一个jar里面的包可能有相互依赖关系
                             */
                            Enumeration<JarEntry> jarEntries = jarFile.entries();
                            while(jarEntries.hasMoreElements()) {
                                JarEntry jarEntry = jarEntries.nextElement();
                                String jarEntryName = jarEntry.getName();
                                if(jarEntryName.endsWith(CLASS_FILE_SUFFIX)) {
                                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(CLASS_FILE_SUFFIX)).replaceAll("/", ".");
                                    doAddClass(classSet, className);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("get class set failure", e);
            throw new RuntimeException(e);
        }

        return classSet;
    }


    /**
     * 根据包名和包所在的物理路径查找包内的所有class文件，然后加载它们
     * @param classSet      存放所有加载好的类的Class对象的集合
     * @param packagePath   包所在的物理绝对路径，也就是存放了packageName包中所有class文件的文件夹的绝对路径
     * @param packageName   存放了class文件的包名
     */
    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                /**
                 * 因为给定包名后，例如xyz.joeyxie，包的结构可能如下：
                 * xyz.joeyxie
                 *      Person.class
                 * xyz.joeyxie.animal
                 *      Dog.class
                 * xyz.joeyxie.animal.bird
                 *      Eagle.class
                 * 所以在packagePath下面可能还有文件夹，也还有子包，所以过滤器只留下class文件和目录，其它过滤掉
                 */
                return (pathname.isFile() && pathname.getName().endsWith(CLASS_FILE_SUFFIX)) ||
                        pathname.isDirectory();
            }
        });

        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf(CLASS_FILE_SUFFIX));
                if (StringUtil.isNotEmpty(packageName)) {
                    className = packageName + "." + className;
                }
                doAddClass(classSet, className);
            } else {
                // 如果这个文件是目录的话，递归地调用本方法处理，直到遇到class文件为止
                String subPackagePath = fileName;
                if (StringUtil.isNotEmpty(packagePath)) {
                    subPackagePath = packagePath + File.separator + subPackagePath;
                }
                String subPackageName = fileName;
                if (StringUtil.isNotEmpty(packageName)) {
                    // 要在原来包名的基础上增加当前文件夹的名字作为子包名
                    subPackageName = packageName + "." + subPackageName;
                }
                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }

    /**
     * 真正加载类，加载完后添加到集合中
     */
    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> cls = loadClass(className, false);
        classSet.add(cls);
    }


    private static final String FILE_PROTOCOL = "file";
    private static final String JAR_FILE_PROTOCOL = "jar";
    private static final String CLASS_FILE_SUFFIX = ".class";
}
