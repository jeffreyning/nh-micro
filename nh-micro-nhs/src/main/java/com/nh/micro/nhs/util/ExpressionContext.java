package com.nh.micro.nhs.util;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;


public interface ExpressionContext {
    /**
     * @param expression
     * @return Object
     */
    public Object getValue(String expression);

    /**
     * @param expression
     * @param resultType
     * @return Object
     */
    public Object getValue(String expression, Class<?> resultType);

    /**
     * @param expression
     * @return Object
     * @throws Exception
     */
    public Object evaluate(String expression) throws Exception;

    /**
     * @param name
     * @return Object
     */
    public Object getAttribute(String name);

    /**
     * @param name
     * @param value
     */
    public void setAttribute(String name, Object value);

    /**
     * @return the Map<String, Object>
     */
    public Map<String, Object> getContext();

    /**
     * @param context the context to set
     */
    public void setContext(Map<String, Object> context);

    /**
     * @param expression
     * @return Object
     */
    public boolean getBoolean(String expression);

    /**
     * @param expression
     * @return Byte
     */
    public Byte getByte(String expression);

    /**
     * @param expression
     * @return Short
     */
    public Short getShort(String expression);

    /**
     * @param expression
     * @return Object
     */
    public Integer getInteger(String expression);

    /**
     * @param expression
     * @return Float
     */
    public Float getFloat(String expression);

    /**
     * @param expression
     * @return Double
     */
    public Double getDouble(String expression);

    /**
     * @param expression
     * @return Long
     */
    public Long getLong(String expression);

    /**
     * @param expression
     * @return Character
     */
    public Character getCharacter(String expression);

    /**
     * @param expression
     * @return Object
     */
    public String getString(String expression);

    /**
     * @param expression
     * @return Object
     */
    public String getEncodeString(String expression);

    /**
     * @param out
     * @param b
     * @throws IOException
     */
    public void print(Writer out, boolean b) throws IOException;

    /**
     * @param out
     * @param c
     * @throws IOException
     */
    public void print(Writer out, char c) throws IOException;

    /**
     * @param out
     * @param b
     * @throws IOException
     */
    public void print(Writer out, byte b) throws IOException;

    /**
     * @param out
     * @param s
     * @throws IOException
     */
    public void print(Writer out, short s) throws IOException;

    /**
     * @param out
     * @param i
     * @throws IOException
     */
    public void print(Writer out, int i) throws IOException;

    /**
     * @param out
     * @param f
     * @throws IOException
     */
    public void print(Writer out, float f) throws IOException;

    /**
     * @param out
     * @param d
     * @throws IOException
     */
    public void print(Writer out, double d) throws IOException;

    /**
     * @param out
     * @param l
     * @throws IOException
     */
    public void print(Writer out, long l) throws IOException;

    /**
     * @param out
     * @param content
     * @throws IOException
     */
    public void print(Writer out, String content) throws IOException;

    /**
     * @param out
     * @param object
     * @throws IOException
     */
    public void print(Writer out, Object object) throws IOException;

    /**
     * @return Encoder
     */
    public Encoder getEncoder();

    /**
     * @param encoder
     */
    public void setEncoder(Encoder encoder);

    /**
     * release the current ExpressionContext
     */
    public void release();
}
