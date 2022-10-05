/**
 * @(#)ClassUtils.java 2014-2-16
 * <p>
 * Copyright (c) 2004-2014 Lakala, Inc. zhongjiang Road, building 22, Lane 879, shanghai, china All
 * Rights Reserved.
 * <p>
 * This software is the confidential and proprietary information of Lakala, Inc. You shall not
 * disclose such Confidential Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with Lakala.
 */
package org.storevm.framework.remote.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类文件工具类，用于加载指定包名下的所有类文件
 *
 * @author Jack tan
 * @date 2018/03/26
 * @version 1.0.0
 */
@Slf4j
public class ClassUtils {
    /**
     * 加载指定包下的所有类并返回类集合
     *
     * @param pack 包名
     * @return 已加载的类对象集合
     */
    public static Set<Class<?>> getClasses(String pack) {
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        boolean recursive = true;
        String packageName = pack;
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
                } else if ("jar".equals(protocol)) {
                    JarFile jar;
                    try {
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        Enumeration<JarEntry> entries = jar.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            if (name.charAt(0) == '/') {
                                name = name.substring(1);
                            }
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                if (idx != -1) {
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                if ((idx != -1) || recursive) {
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            classes.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException ex) {
                        log.error("获取类文件异常", ex);
                    }
                }
            }
        } catch (IOException ex) {
            log.error("获取类文件异常", ex);
        }

        return classes;
    }

    /**
     * 将指定包路径下的类文件加载到Java的类加载器中并将其放入集合中返回
     *
     * @param packageName 包名
     * @param packagePath 类文件路径
     * @param recursive 是否递归查找
     * @param classes 返回类集合
     */
    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive,
                                                        Set<Class<?>> classes) {
        File dir = new File(packagePath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] dirfiles = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        for (File file : dirfiles) {
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive,
                        classes);
            } else {
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // classes.add(Class.forName(packageName + '.' + className));
                    classes
                            .add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException ex) {
                    log.error("获取类文件异常", ex);
                }
            }
        }
    }
}
