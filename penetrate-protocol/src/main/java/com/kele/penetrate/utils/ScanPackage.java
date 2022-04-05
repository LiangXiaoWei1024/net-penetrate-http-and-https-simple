package com.kele.penetrate.utils;


import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 扫描当前包下所有的class
 */
public class ScanPackage
{
    public static List<Class<?>> getClassesByPackageName(String packageName)
    {
        ArrayList<Class<?>> classes = new ArrayList<>();

        try
        {
            ClassLoader e = Thread.currentThread().getContextClassLoader();
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = e.getResources(path);
            ArrayList<File> dirs = new ArrayList<>();

            if (!getJarStart())
            {
                while (resources.hasMoreElements())
                {
                    URL directory = resources.nextElement();
                    dirs.add(new File(directory.getFile()));
                }

                for (File directory1 : dirs)
                {
                    classes.addAll(findClasses(directory1, packageName));
                }
            }
            else
            {
                while (resources.hasMoreElements())
                {
                    URL directory = resources.nextElement();
                    JarURLConnection jarURLConnection = (JarURLConnection) directory.openConnection();
                    JarFile jarFile = jarURLConnection.getJarFile();
                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements())
                    {
                        JarEntry jarEntry = entries.nextElement();
                        String jarEntryName = jarEntry.getName();
                        if (jarEntryName.contains(path) && jarEntryName.contains(".class"))
                        {
                            classes.add(Class.forName(jarEntryName.replace("/", ".").replace(".class", "")));
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return classes;
    }

    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException
    {
        ArrayList<Class<?>> classes = new ArrayList<>();
        if (directory.exists())
        {
            File[] files = directory.listFiles();
            assert files != null;

            for (File file : files)
            {
                if (file.isDirectory())
                {
                    assert !file.getName().contains(".");

                    classes.addAll(findClasses(file, packageName + '.' + file.getName()));
                }
                else if (file.getName().endsWith(".class"))
                {
                    classes.add(Class
                            .forName(packageName + "." + file.getName().substring(0, file.getName().length() - 6)));
                }
            }

        }
        return classes;
    }

    //<editor-fold desc="获取是否是jar启动">
    private static boolean getJarStart()
    {
        URL url = ScanPackage.class.getResource("");
        String protocol = url.getProtocol();
        return "jar".equals(protocol);
    }
    //</editor-fold>
}
