package com.nh.micro.nhs.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Reflect {
    private static final ConcurrentHashMap<String, Method> methodCache = new ConcurrentHashMap<String, Method>();
    private static final ConcurrentHashMap<String, Class<?>> classCache = new ConcurrentHashMap<String, Class<?>>();
    private static final ConcurrentHashMap<String, Map<String, Method>> setMethodCache = new ConcurrentHashMap<String, Map<String, Method>>();

    /**
     * @param className
     * @return Class<?>
     * @throws Exception
     */
    public static Class<?> getClass(String className) throws Exception {
        Class<?> type = classCache.get(className);

        if(type == null) {
            type = ClassUtil.getClass(className);
            Class<?> old = classCache.putIfAbsent(className, type);

            if(old != null) {
                type = old;
            }
        }
        return type;
    }

    /**
     * @param type
     * @param methodName
     * @param parameterTypes
     * @return Method
     * @throws Exception
     */
    public static Method getMethod(Class<?> type, String methodName, Class<?>[] parameterTypes) throws Exception {
        String key = getSignature(type, methodName, parameterTypes);
        Method method = methodCache.get(key);

        if(method == null) {
            method = type.getMethod(methodName, parameterTypes);
            Method old = methodCache.putIfAbsent(key, method);

            if(old != null) {
                method = old;
            }
        }
        return method;
    }

    /**
     * @param type
     * @param name
     * @return Method
     */
    public static Method getSetMethod(Class<?> type, String name) {
        Map<String, Method> methodMap = getSetMethodMap(type);
        return methodMap.get(name);
    }

    /**
     * @param object
     * @return List<Method>
     */
    public static List<Method> getSetMethodList(Object object) {
        return getSetMethodList(object.getClass());
    }

    /**
     * @param type
     * @return List<Method>
     */
    public static List<Method> getSetMethodList(Class<?> type) {
        Map<String, Method> methodMap = getSetMethodMap(type);
        List<Method> methodList = new ArrayList<Method>();
        methodList.addAll(methodMap.values());
        return methodList;
    }

    /**
     * @param type
     * @return List<Method>
     */
    public static Map<String, Method> getSetMethodMap(Class<?> type) {
        String className = type.getName();
        Map<String, Method> methodMap = setMethodCache.get(className);

        if(methodMap == null) {
            methodMap = ClassUtil.getSetMethodMap(type);
            setMethodCache.putIfAbsent(className, methodMap);
        }
        return methodMap;
    }

    /**
     * @param type
     * @param methodName
     * @param parameterTypes
     * @return String
     */
    public static String getSignature(Class<?> type, String methodName, Class<?>[] parameterTypes) {
        return type.getName() + "." + methodName + toString(parameterTypes);
    }

    /**
     * @param types
     * @return String
     */
    public static String toString(Class<?>[] types) {
        StringBuilder buffer = new StringBuilder("(");

        if(types != null) {
            for(int i = 0; i < types.length; i++) {
                if(i > 0) {
                    buffer.append(", ");
                }
                Class<?> c = types[i];
                buffer.append((c == null) ? "null" : c.getName());
            }
        }
        buffer.append(")");
        return buffer.toString();
    }
}
