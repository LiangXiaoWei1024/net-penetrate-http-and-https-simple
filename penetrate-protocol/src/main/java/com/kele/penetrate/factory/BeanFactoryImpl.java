package com.kele.penetrate.factory;

import com.kele.penetrate.factory.annotation.Autowired;
import com.kele.penetrate.factory.annotation.Recognizer;
import com.kele.penetrate.utils.ScanPackage;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@SuppressWarnings("unused")
public class BeanFactoryImpl
{
    private static BeanFactoryImpl beanFactory;
    private static String scanningPath;
    private static final Map<String, Object> beans = new HashMap<>();

    public BeanFactoryImpl()
    {
        init();
    }

    private void init()
    {
        try
        {
            List<Class<?>> classesByPackageName = ScanPackage.getClassesByPackageName(scanningPath);
            //<editor-fold desc="扫描所有Recognizer注解的类">
            for (Class<?> clazz : classesByPackageName)
            {
                Recognizer recognizer = clazz.getAnnotation(Recognizer.class);
                if (recognizer == null)
                {
                    continue;
                }
                beans.put(clazz.getName(), clazz.newInstance());
            }
            //</editor-fold>

            //<editor-fold desc="注入属性">
            for (Class<?> aClass : classesByPackageName)
            {
                Object o = beans.get(aClass.getName());
                Field[] declaredFields = aClass.getDeclaredFields();
                for (Field field : declaredFields)
                {
                    Autowired annotation = field.getAnnotation(Autowired.class);
                    if (annotation == null)
                    {
                        continue;
                    }
                    field.setAccessible(true);
                    field.set(o, beans.get(field.getType().getName()));
                }
            }
            //</editor-fold>
        }
        catch (Exception ex)
        {
            log.error("注入扫描异常", ex);
        }
    }

    public synchronized static BeanFactoryImpl getInstance(String scanning)
    {
        if (beanFactory == null)
        {
            scanningPath = scanning;
            beanFactory = new BeanFactoryImpl();
        }
        return beanFactory;
    }

    public static <T> T getBean(Class<T> type)
    {
        return type.cast(beans.get(type.getName()));
    }

    public static void setBean(Object o)
    {
        beans.put(o.getClass().getName(), o);
    }
}
