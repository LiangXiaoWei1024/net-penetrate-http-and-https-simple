package com.kele.penetrate.utils;

import com.kele.penetrate.factory.BeanFactoryImpl;
import com.kele.penetrate.factory.annotation.Register;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@SuppressWarnings("unused")
@Slf4j
public class Events<T>
{
    private final ConcurrentLinkedQueue<OrderFunc<T>> events = new ConcurrentLinkedQueue<>();

    public Events(String name, Class<T> t, String registerPath)
    {
        List<Class<?>> classesByPackageName = ScanPackage.getClassesByPackageName(registerPath);
        for (Class<?> clazz : classesByPackageName)
        {
            boolean isSimilar = false;
            boolean isParameterType = false;
            boolean isReturn = false;
            Register register = clazz.getAnnotation(Register.class);
            if (register == null)
            {
                continue;
            }

            for (Class<?> interfaceClazz : clazz.getInterfaces())
            {
                Type[] genericInterfaces = clazz.getGenericInterfaces();
                if (genericInterfaces[0] instanceof ParameterizedType)
                {
                    ParameterizedType parameterizedType = (ParameterizedType) genericInterfaces[0];
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    if (actualTypeArguments[0] == t)
                    {
                        isParameterType = true;
                    }
                    if (actualTypeArguments[1] == Boolean.class)
                    {
                        isReturn = true;
                    }
                }
                if (interfaceClazz == Func.class)
                {
                    isSimilar = true;
                }
            }

            if (isSimilar)
            {
                if (!isParameterType)
                {
                    log.error(name + "  " + clazz.getName() + "自动注册失败:参数类型错误");
                    return;
                }
                if (!isReturn)
                {
                    log.error(name + "  " + clazz.getName() + "自动注册失败:return类型错误");
                    return;
                }

                try
                {
                    Func<T, Boolean> func = (Func<T, Boolean>) BeanFactoryImpl.getBean(clazz);
                    if (func == null)
                    {
                        func = (Func<T, Boolean>) clazz.newInstance();
                        BeanFactoryImpl.setBean(func);
                    }
                    add(new OrderFunc.OrderFuncBuilder().func(func).order(register.value()).build());
                    log.info(name + "  " + clazz.getName() + "自动注册成功");
                }
                catch (Exception ex)
                {
                    log.error(name + "  " + clazz.getName() + "自动注册失败", ex);
                }
            }
        }
    }

    private synchronized void add(OrderFunc orderFunc)
    {
        events.add(orderFunc);

        Object[] objects = events.toArray();
        for (int i = 0; i < objects.length - 1; i++)
        {
            for (int j = 0; j < objects.length - 1 - i; j++)
            {
                if ((((OrderFunc)objects[j]).getOrder()) < ((OrderFunc)objects[j+1]).getOrder())
                {
                    Object temp = objects[j];
                    objects[j] = objects[j + 1];
                    objects[j + 1] = temp;
                }
            }
        }
        events.clear();
        for (Object order : objects)
        {
            events.add((OrderFunc<T>)order);
        }
    }

    public synchronized void add(Func<T, Boolean> func)
    {
        events.add(new OrderFunc.OrderFuncBuilder().func(func).order(0).build());
    }

    public synchronized void add(Func<T, Boolean> func, int order)
    {
        events.add(new OrderFunc.OrderFuncBuilder().func(func).order(order).build());
    }

    public synchronized void remove(Func<T, Boolean> func)
    {
        events.remove(func);
    }

    public synchronized void notice(T t)
    {
        for (OrderFunc orderFunc : events)
        {
            if (orderFunc.getFunc().func(t))
            {
                break;
            }
        }
    }
}
