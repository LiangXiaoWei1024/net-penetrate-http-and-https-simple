package com.kele.penetrate.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

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
        catch (Exception arg7)
        {
            arg7.printStackTrace();
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
}
