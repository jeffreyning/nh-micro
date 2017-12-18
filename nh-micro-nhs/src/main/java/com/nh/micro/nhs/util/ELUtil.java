package com.nh.micro.nhs.util;

import java.util.ArrayList;
import java.util.List;




public class ELUtil {
    private ELUtil() {
    }

    /**
     * @param expressionContext
     * @param attribute
     * @param expectType
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T getValue(ExpressionContext expressionContext, Attribute attribute, Class<T> expectType) {
        int type = attribute.getType();

        /**
         * 属�?�?种类�?         * 1. VARIABLE;
         * 2. EXPRESSION;
         * 3. JSP_EXPRESSION;
         * 4. TEXT
         * 解释模式不支持JSP_EXPRESSION
         */
        switch(type) {
            case Attribute.VARIABLE: {
                Object value = expressionContext.getAttribute(attribute.getText());
                return ClassUtil.cast(value, expectType);
            }
            case Attribute.EXPRESSION: {
                return (T)(expressionContext.getValue(attribute.getText(), expectType));
            }
            case Attribute.STRING: {
                return ClassUtil.cast(attribute.getValue(), expectType);
            }
            case Attribute.NUMBER: {
                return ClassUtil.cast(attribute.getValue(), expectType);
            }
            case Attribute.BOOLEAN: {
                return ClassUtil.cast(attribute.getValue(), expectType);
            }
            case Attribute.MIX_EXPRESSION: {
                String value = ELUtil.replace(expressionContext, attribute.getText());
                return ClassUtil.cast(value, expectType);
            }
            case Attribute.JSP_EXPRESSION: {
                return null;
            }
            default: {
                if(expectType.isAssignableFrom(String.class)) {
                    return (T)(attribute.getText());
                }
                return null;
            }
        }
    }

    /**
     * @param expressionContext
     * @param source
     * @return Object
     */
    public static String getString(ExpressionContext expressionContext, String source) {
        Object value = ELUtil.eval(expressionContext, source);
        return (value != null ? value.toString() : null);
    }

    /**
     * @param expressionContext
     * @param source
     * @return Object
     */
    public static boolean getBoolean(ExpressionContext expressionContext, String source) {
        Object value = ELUtil.eval(expressionContext, source);

        if(value == null) {
            return false;
        }

        if(value instanceof Boolean) {
            return Boolean.TRUE.equals(value);
        }
        return (value.toString().equals("true"));
    }

    /**
     * @param expressionContext
     * @param source
     * @return Byte
     */
    public static Byte getByte(ExpressionContext expressionContext, String source) {
        Object value = ELUtil.eval(expressionContext, source);

        if(value != null && value instanceof Number) {
            return ((Number)value).byteValue();
        }
        return ClassUtil.cast(value, Byte.class);
    }

    /**
     * @param expressionContext
     * @param source
     * @return Short
     */
    public static Short getShort(ExpressionContext expressionContext, String source) {
        Object value = ELUtil.eval(expressionContext, source);

        if(value != null && value instanceof Number) {
            return ((Number)value).shortValue();
        }
        return ClassUtil.cast(value, Short.class);
    }

    /**
     * @param expressionContext
     * @param source
     * @return Integer
     */
    public static Integer getInteger(ExpressionContext expressionContext, String source) {
        Object value = ELUtil.eval(expressionContext, source);

        if(value != null && value instanceof Number) {
            return ((Number)value).intValue();
        }
        return ClassUtil.cast(value, Integer.class);
    }

    /**
     * @param expressionContext
     * @param source
     * @return Float
     */
    public static Float getFloat(ExpressionContext expressionContext, String source) {
        Object value = ELUtil.eval(expressionContext, source);

        if(value != null && value instanceof Number) {
            return ((Number)value).floatValue();
        }
        return ClassUtil.cast(value, Float.class);
    }

    /**
     * @param expressionContext
     * @param source
     * @return Double
     */
    public static Double getDouble(ExpressionContext expressionContext, String source) {
        Object value = ELUtil.eval(expressionContext, source);

        if(value != null && value instanceof Number) {
            return ((Number)value).doubleValue();
        }
        return ClassUtil.cast(value, Double.class);
    }

    /**
     * @param expressionContext
     * @param source
     * @return Long
     */
    public static Long getLong(ExpressionContext expressionContext, String source) {
        Object value = ELUtil.eval(expressionContext, source);

        if(value != null && value instanceof Number) {
            return ((Number)value).longValue();
        }
        return ClassUtil.cast(value, Long.class);
    }

    /**
     * @param expressionContext
     * @param expresion
     * @return Object
     */
    private static Object eval(ExpressionContext expressionContext, String expresion) {
        if(expresion == null) {
            return null;
        }

        List<Node> list = parse(expresion);

        if(list.size() < 1) {
            return null;
        }

        if(list.size() == 1) {
            Node node = list.get(0);

            if(node.getNodeType() == NodeType.TEXT) {
                return node.getTextContent();
            }
            else if(node.getNodeType() == NodeType.EXPRESSION) {
                return expressionContext.getValue(node.getTextContent());
            }
            else {
                return null;
            }
        }

        if(list.size() > 0) {
            if(list.size() == 1) {
                Node node = list.get(0);

                if(node instanceof Expression) {
                    return expressionContext.getValue(node.getTextContent());
                }
                return node.getTextContent();
            }
            Object value = null;
            StringBuilder buffer = new StringBuilder();

            for(Node node : list) {
                if(node instanceof Expression) {
                    value = expressionContext.getValue(node.getTextContent());

                    if(value != null) {
                        buffer.append(value.toString());
                    }
                }
                else {
                    buffer.append(node.getTextContent());
                }
            }
            return buffer.toString();
        }
        return null;
    }

    /**
     * @param expressionContext
     * @param source
     * @return String
     */
    public static String replace(ExpressionContext expressionContext, String source) {
        char c;
        int length = source.length();
        StringBuilder buffer = new StringBuilder();
        StringBuilder result = new StringBuilder(4096);

        for(int i = 0; i < length; i++) {
            c = source.charAt(i);

            if(c == '$' && i < length - 1 && source.charAt(i + 1) == '{') {
                for(i = i + 2; i < length; i++) {
                    c = source.charAt(i);

                    if(c == '}') {
                        Object value = expressionContext.getValue(buffer.toString());

                        if(value != null) {
                            result.append(value);
                        }
                        break;
                    }
                    else {
                        buffer.append(c);
                    }
                }
                buffer.setLength(0);
            }
            else if(c == '<' && (i + 1) < length && source.charAt(i + 1) == '%') {
                for(i = i + 2; i < length; i++) {
                    c = source.charAt(i);

                    if(c == '%' && (i + 1) < length && source.charAt(i + 1) == '>') {
                        i++;
                        break;
                    }
                }
            }
            else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * @param source
     * @return List<Node>
     */
    public static List<Node> parse(String source) {
        char c;
        List<Node> list = new ArrayList<Node>();
        StringBuilder buffer = new StringBuilder();
        AppendableNode textNode = null;

        for(int i = 0, length = source.length(); i < length; i++) {
            c = source.charAt(i);

            if(c == '$' && (i + 1) < length && source.charAt(i + 1) == '{') {
                for(i = i + 2; i < length; i++) {
                    c = source.charAt(i);

                    if(c == '}') {
                        break;
                    }
                    buffer.append(c);
                }

                String content = buffer.toString().trim();

                if(content.length() > 0) {
                    if(content.startsWith("?")) {
                        if(textNode == null) {
                            textNode = new AppendableNode();
                            list.add(textNode);
                        }
                        textNode.append("${");
                        textNode.append(content.substring(1));
                        textNode.append("}");
                    }
                    else {
                        Expression expression = new Expression();
                        expression.setTextContent(content);
                        list.add(expression);
                        textNode = null;
                    }
                }
                buffer.setLength(0);
            }
            else if(c == '<' && (i + 2) < length && source.charAt(i + 1) == '%' && source.charAt(i + 2) == '=') {
                for(i = i + 3; i < length; i++) {
                    c = source.charAt(i);

                    if(c == '%' && (i + 1) < length && source.charAt(i + 1) == '>') {
                        i++;
                        break;
                    }
                    buffer.append(source.charAt(i));
                }

                String content = buffer.toString().trim();

                if(content.length() > 0) {
                    JspExpression expression = new JspExpression();
                    expression.setTextContent(content);
                    list.add(expression);
                    textNode = null;
                }
                buffer.setLength(0);
            }
            else {
                if(textNode == null) {
                    textNode = new AppendableNode();
                    list.add(textNode);
                }
                textNode.append(c);
            }
        }
        return list;
    }
}
