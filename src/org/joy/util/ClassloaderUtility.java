package org.joy.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.joy.exception.AppRuntimeException;
import org.joy.util.messgae.Messages;

public class ClassloaderUtility {

    private static final Logger LOGGER = Logger.getLogger(ClassloaderUtility.class);

    private ClassloaderUtility(){
    }

    public static ClassLoader getCustomClassloader(String basePath, List<String> entries) {
        List<URL> urls = new ArrayList<URL>();
        File file;

        if (entries != null) {
            for (String classPathEntry : entries) {
                String jarPath = (basePath + classPathEntry).replaceAll("%20", " ");
                file = new File(jarPath);
                LOGGER.debug("Loading jar : " + file.getPath());
                if (!file.exists()) {
                    throw new AppRuntimeException(Messages.getString(Messages.RUNTIME_ERROR_4, classPathEntry));
                }

                try {
                    urls.add(file.toURI().toURL());
                } catch (MalformedURLException e) {
                    throw new AppRuntimeException(Messages.getString(Messages.RUNTIME_ERROR_4, classPathEntry));
                }
            }
        }

        ClassLoader parent = Thread.currentThread().getContextClassLoader();

        return new URLClassLoader(urls.toArray(new URL[urls.size()]), parent);
    }
}
