package com.kele.penetrate.factory;

import com.kele.penetrate.utils.ScanPackage;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class BeanFactoryImpl
{
    /**
     * 获取所有的java文件
     */
    private static final List<Class<?>> classesByPackageName = ScanPackage.getClassesByPackageName("com.kele.penetrate");
    /**
     * 管理IOC bean
     */
    private static final Map<String, Object> beans = new HashMap<>();

    public BeanFactoryImpl()
    {
        try
        {
            //<editor-fold desc="第一次 主要是为了把所有的类全部实例出来，后续可能会有依赖注入的情况 比如DemoA 里面注入 DemoB">
            for (Class<?> clazz : classesByPackageName)
            {
                Recognizer annotation = clazz.getAnnotation(Recognizer.class);
                if (annotation == null)
                {
                    continue;
                }
                beans.put(clazz.getName(), clazz.newInstance());
            }
            //</editor-fold>

            //<editor-fold desc="第二次 主要类里面的属性给注入进去">
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

    public <T> T getBean(Class<T> tClass)
    {
        return (T) beans.get(tClass.getName());
    }
}
