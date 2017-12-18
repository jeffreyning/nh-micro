package com.nh.micro.nhs.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClassUtil {
    /**
     * @param className
     * @return Object
     * @throws Exception
     */
    public static Object getInstance(String className) throws Exception {
        return getClass(className).newInstance();
    }

    /**
     * @param className
     * @param parameterTypes
     * @param parameters
     * @return Object
     * @throws Exception
     */
    public static Object getInstance(String className, Class<?>[] parameterTypes, Object[] parameters) throws Exception {
        Class<?> clazz = getClass(className);
        Constructor<?> constructor = clazz.getConstructor(parameterTypes);
        return constructor.newInstance(parameters);
    }

    /**
     * @param className
     * @return Class<?>
     * @throws ClassNotFoundException
     */
    public static Class<?> getClass(String className) throws ClassNotFoundException {
        if(className.equals("boolean")) {
            return boolean.class;
        }
        else if(className.equals("byte")) {
            return byte.class;
        }
        else if(className.equals("short")) {
            return short.class;
        }
        else if(className.equals("char")) {
            return char.class;
        }
        else if(className.equals("int")) {
            return int.class;
        }
        else if(className.equals("float")) {
            return float.class;
        }
        else if(className.equals("double")) {
            return double.class;
        }
        else if(className.equals("long")) {
            return long.class;
        }

        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        }
        catch(Exception e) {
        }

        try {
            return ClassUtil.class.getClassLoader().loadClass(className);
        }
        catch(Exception e) {
        }
        return Class.forName(className);
    }

    /**
     * @param values
     * @return Class<?>[]
     */
    public static Class<?>[] getTypes(Object[] values) {
        Class<?>[] types = new Class<?>[values.length];

        for(int i = 0; i < values.length; i++) {
            types[i] = values[i].getClass();
        }
        return types;
    }

    /**
     * @param bean
     * @param properties
     * @throws Exception
     */
    public static void setProperties(Object bean, Map<String, Object> properties) throws Exception {
        if(properties == null || properties.size() < 1) {
            return;
        }

        for(Map.Entry<String, Object> entry : properties.entrySet()) {
            ClassUtil.setProperty(bean, entry.getKey(), entry.getValue());
        }
    }

    /**
     * @param bean
     * @param name
     * @param value
     * @throws Exception
     */
    public static void setProperty(Object bean, String name, Object value) throws Exception {
        if(bean == null) {
            return;
        }

        Class<?> type = bean.getClass();
        Method method = getSetMethod(type, name);

        if(method != null) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?> parameterType = parameterTypes[0];
            Object arg = ClassUtil.cast(value, parameterType);

            if(arg == null && parameterType.isPrimitive()) {
                return;
            }
            method.invoke(bean, new Object[]{arg});
        }
        else {
            String methodName = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
            throw new Exception("NoSuchMethodException: " + type.getName() + "." + methodName);
        }
    }

    /**
     * @param bean
     * @param name
     * @return Object
     * @throws Exception
     */
    public static Object getProperty(Object bean, String name) throws Exception {
        if(bean == null) {
            return null;
        }

        Class<?> type = bean.getClass();
        String methodName = "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
        Method method = type.getMethod(methodName, new Class[0]);
        return method.invoke(bean, new Object[0]);
    }

    /**
     * @param type
     * @param name
     * @return Method
     */
    public static Method getSetMethod(Class<?> type, String name) {
        Method[] methods = type.getMethods();
        String methodName = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);

        for(Method method : methods) {
            if(method.getModifiers() != Modifier.PUBLIC) {
                continue;
            }

            if(method.getName().equals(methodName)) {
                if(method.getParameterTypes().length == 1) {
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * @param type
     * @return List<Method>
     */
    public static List<Method> getSetMethodList(Class<?> type) {
        Method[] methods = type.getMethods();
        List<Method> methodList = new ArrayList<Method>();

        for(Method method : methods) {
            String name = method.getName();

            if(name.length() <= 3 || !name.startsWith("set")) {
                continue;
            }

            if(method.getModifiers() != Modifier.PUBLIC) {
                continue;
            }

            Class<?>[] parameterTypes = method.getParameterTypes();

            if(parameterTypes.length != 1) {
                continue;
            }
            methodList.add(method);
        }
        return methodList;
    }

    /**
     * @param type
     * @return List<Method>
     */
    public static Map<String, Method> getSetMethodMap(Class<?> type) {
        List<Method> methodList = getSetMethodList(type);
        Map<String, Method> map = new HashMap<String, Method>();

        for(Method method : methodList) {
            String name = method.getName();
            String fieldName = Character.toLowerCase(name.charAt(3)) + name.substring(4);
            map.put(fieldName, method);
        }
        return map;
    }

    /**
     * @param <T>
     * @param type
     * @param value
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object value, Class<T> type) {
        if(value == null || type == null) {
            return null;
        }

        Class<?> clazz = value.getClass();

        /**
         *  true: Object.class.isAssignableFrom(String.class)
         * false: String.class.isAssignableFrom(Object.class)
         */
        if(type.isAssignableFrom(clazz)) {
            return (T)value;
        }

        Object result = null;

        if(type == char.class || type == Character.class) {
            return (T)(ClassUtil.getCharacter(value));
        }
        else if(type == boolean.class || type == Boolean.class) {
            return (T)ClassUtil.getBoolean(value);
        }
        else if(type == byte.class || type == Byte.class) {
            return (T)(ClassUtil.getByte(value));
        }
        else if(type == short.class || type == Short.class) {
            return (T)(ClassUtil.getShort(value));
        }
        else if(type == int.class || type == Integer.class) {
            return (T)(ClassUtil.getInteger(value));
        }
        else if(type == float.class || type == Float.class) {
            return (T)(ClassUtil.getFloat(value));
        }
        else if(type == double.class || type == Double.class) {
            return (T)(ClassUtil.getDouble(value));
        }
        else if(type == long.class || type == Long.class) {
            return (T)(ClassUtil.getLong(value));
        }
        else if(type == String.class) {
            result = ClassUtil.getString(value);
        }
        else if(type == java.sql.Date.class) {
            Date date = ClassUtil.getDate(value);

            if(date != null) {
                result = new java.sql.Date(date.getTime());
            }
        }
        else if(type == java.sql.Time.class) {
            Date date = ClassUtil.getDate(value);

            if(date != null) {
                result = new java.sql.Time(date.getTime());
            }
        }
        else if(type == java.sql.Timestamp.class) {
            Date date = ClassUtil.getDate(value);

            if(date != null) {
                result = new java.sql.Timestamp(date.getTime());
            }
        }
        else if(type == java.util.Date.class) {
            result = ClassUtil.getDate(value);
        }
        else if(type == Object.class) {
            result = value;
        }
        return ((T)result);
    }

    /**
     * @param source
     * @return Object[]
     */
    public static Object[] getArray(Object source) {
        if(source instanceof Object[]) {
            return (Object[])source;
        }

        if(source == null) {
            return new Object[0];
        }

        if(!source.getClass().isArray()) {
            throw new IllegalArgumentException("Source is not an array: " + source);
        }

        int length = Array.getLength(source);

        if(length == 0) {
            return new Object[0];
        }

        Class<?> type = Array.get(source, 0).getClass();
        Object[] array = (Object[])(Array.newInstance(type, length));

        for(int i = 0; i < length; i++) {
            array[i] = Array.get(source, i);
        }
        return array;
    }

    /**
     * @param value
     * @return Boolean
     */
    public static Boolean getBoolean(Object value) {
        if(value instanceof Boolean) {
            return (Boolean)value;
        }

        if(value != null) {
            return value.toString().equals("true");
        }
        return Boolean.FALSE;
    }

    /**
     * @param value
     * @return Byte
     */
    public static Byte getByte(Object value) {
        Integer i = getInteger(value);

        if(i != null) {
            return i.byteValue();
        }
        return null;
    }

    /**
     * @param value
     * @return Boolean
     */
    public static Short getShort(Object value) {
        Integer i = getInteger(value);

        if(i != null) {
            return i.shortValue();
        }
        return null;
    }

    /**
     * @param value
     * @return Character
     */
    public static Character getCharacter(Object value) {
        if(value instanceof Character) {
            return (Character)value;
        }

        if(value != null) {
            String content = value.toString();

            if(content.length() > 0) {
                return content.charAt(0);
            }
        }
        return null;
    }

    /**
     * @param value
     * @return Integer
     */
    public static Integer getInteger(Object value) {
        if(value != null) {
            Double d = getDouble(value);

            if(d != null) {
                return d.intValue();
            }
        }
        return null;
    }

    /**
     * @param value
     * @return Float
     */
    public static Float getFloat(Object value) {
        if(value instanceof Number) {
            return ((Number)value).floatValue();
        }

        if(value != null) {
            Double d = getDouble(value);

            if(d != null) {
                return d.floatValue();
            }
        }
        return null;
    }

    /**
     * @param value
     * @return Double
     */
    public static Double getDouble(Object value) {
        if(value instanceof Number) {
            return ((Number)value).doubleValue();
        }

        if(value != null) {
            try {
                return Double.parseDouble(value.toString());
            }
            catch(NumberFormatException e) {
            }
        }
        return null;
    }

    /**
     * @param value
     * @return Long
     */
    public static Long getLong(Object value) {
        if(value instanceof Number) {
            return ((Number)value).longValue();
        }

        if(value != null) {
            Long l = null;
            String s = value.toString().trim();

            try {
                if(s.endsWith("l") || s.endsWith("L")) {
                    l = Long.parseLong(s.substring(0, s.length() - 1));
                }
                else {
                    l = Long.parseLong(s);
                }
            }
            catch(NumberFormatException e) {
            }
            return l;
        }
        return null;
    }

    /**
     * @param value
     * @return String
     */
    public static String getString(Object value) {
        if(value instanceof String) {
            return ((String)value);
        }

        if(value != null) {
            return value.toString();
        }
        return null;
    }

    /**
     * @param value
     * @return Date
     */
    public static Date getDate(Object value) {
        if(value instanceof Date) {
            return (Date)value;
        }

        if(value != null) {
            try {
                String content = value.toString();
                String pattern = getFormat(content);
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                return dateFormat.parse(content);
            }
            catch(java.text.ParseException e) {
            }
        }
        return null;
    }

    /**
     * @param date
     * @return String
     */
    protected static String getFormat(String date) {
        int length = date.length();

        String f1 = "HH:mm:ss";
        String f2 = "yyyy-MM-dd";
        String f3 = "HH:mm:ss SSS";
        String f4 = "yyyy-MM-dd HH:mm:ss";
        String f5 = "yyyy-MM-dd HH:mm:ss SSS";

        if(length <= f1.length()) {
            return f1;
        }
        else if(length <= f2.length()) {
            return f2;
        }
        else if(length <= f3.length()) {
            return f3;
        }
        else if(length <= f4.length()) {
            return f4;
        }
        else if(length <= f5.length()) {
            return f5;
        }
        return f3;
    }

    /**
     * @param source
     * @return Object
     */
    public static Object guess(String source) {
        String temp = source.trim();

        if(temp.length() < 1) {
            return source;
        }

        Object value = null;
        int type = getDataType(source);

        switch(type) {
            case 1: {
                value = temp.equals("true");
                break;
            }
            case 2: {
                try {
                    if(temp.charAt(0) == '+') {
                        value = Integer.parseInt(temp.substring(1));
                    }
                    else {
                        value = Integer.parseInt(temp);
                    }
                }
                catch(NumberFormatException e) {
                }
                break;
            }
            case 3:
            case 4: {
                try {
                    value = Double.parseDouble(temp);
                }
                catch(NumberFormatException e) {
                }
                break;
            }
            case 5: {
                try {
                    if(temp.endsWith("l") || temp.endsWith("L")) {
                        value = Long.parseLong(temp.substring(0, temp.length() - 1));
                    }
                    else {
                        value = Long.parseLong(temp);
                    }
                }
                catch(NumberFormatException e) {
                }
                break;
            }
            default: {
                value = source;
                break;
            }
        }
        return value;
    }

    /**
     * 1: Boolean
     * 2: Integer
     * 3: Float
     * 4: Double
     * 5: Long
     * 9: String
     * @param content
     * @return int
     */
    public static int getDataType(String content) {
        String text = content.trim();

        if(text.length() < 1) {
            return 9;
        }

        int i = 0;
        int d = 0;
        int e = 0;
        char c = text.charAt(0);
        int length = text.length();

        if(c == '+' || c == '-') {
            i++;
        }

        if(c == 't') {
            if(text.equals("treu")) {
                return 1;
            }
            else {
                return 9;
            }
        }

        if(c == 'f') {
            if(text.equals("treu")) {
                return 1;
            }
            else {
                return 9;
            }
        }

        if(c == '.') {
            d = 1;
            i++;
        }

        c = text.charAt(i);

        if(!Character.isDigit(c)) {
            return 9;
        }

        for(; i < length; i++) {
            c = text.charAt(i);

            if(Character.isDigit(c)) {
                continue;
            }

            if(c == '.') {
                if(d == 1 || e == 1) {
                    /**
                     * String
                     */
                    return 9;
                }
                d = 1;
                continue;
            }

            if(c == 'e' || c == 'E') {
                if(e == 1) {
                    /**
                     * String
                     */
                    return 9;
                }
                e = 1;
                continue;
            }

            if(c == 'f' || c == 'F') {
                if(i == length - 1) {
                    return 4;
                }
                else {
                    return 9;
                }
            }

            if(c == 'd' || c == 'D') {
                if(i == length - 1) {
                    return 4;
                }
                else {
                    return 9;
                }
            }

            if(c == 'l' || c == 'L') {
                if(d == 0 && e == 0 && i == length - 1) {
                    return 5;
                }
                else {
                    return 9;
                }
            }
            return 9;
        }
        return ((d == 0 && e == 0) ? 2 : 4);
    }
}
