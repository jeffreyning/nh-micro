package com.nh.micro.nhs.util;

import java.util.ArrayList;
import java.util.List;


public class StringUtil {
    private static final String EMPTY = "";

    private StringUtil() {
    }

    /**
     * @param text
     * @param c
     * @return String
     */
    public static String trim(String text, char c) {
        if(text == null) {
            return EMPTY;
        }

        int i = 0;
        int j = text.length();

        while((i < j) && (text.charAt(i) == c)) {
            i++;
        }

        while((i < j) && (text.charAt(j - 1) == c)) {
            j--;
        }
        return ((i > 0) || (j < text.length())) ? text.substring(i, j) : text;
    }

    /**
     * @param text
     * @param c
     * @return String
     */
    public static String ltrim(String text, char c) {
        if(text == null) {
            return EMPTY;
        }

        int i = 0;
        int length = text.length();

        while((i < length) && (text.charAt(i) == c)) {
            i++;
        }
        return (i > 0 ? text.substring(i) : text);
    }

    /**
     * @param text
     * @param c
     * @return String
     */
    public static String rtrim(String text, char c) {
        if(text == null) {
            return EMPTY;
        }

        int i = 0;
        int j = text.length();

        while((i < j) && (text.charAt(j - 1) == c)) {
            j--;
        }
        return (j < text.length()) ? text.substring(i, j) : text;
    }

    /**
     * @param source
     * @param length
     * @param padding
     * @return String
     */
    public static String substring(String source, int length, String padding) {
        if(source == null) {
            return "";
        }

        String s = source.trim();

        char c;
        int size = 0;
        int count = s.length();
        StringBuilder buffer = new StringBuilder();

        for(int i = 0; i < s.length(); i++) {
            c = s.charAt(i);

            if(c >= 0x0080) {
                size += 2;
                count++;
            }
            else {
                size++;
            }

            if(size > length) {
                if(c >= 0x4e00) {
                    size -= 2;
                }
                else {
                    size--;
                }
                break;
            }
            buffer.append(c);
        }

        if(size < count && padding != null) {
            buffer.append(padding);
        }
        return buffer.toString();
    }

    /**
     * @param source
     * @param length
     * @param pad
     * @return String
     */
    public static String padding(String source, int length, String pad) {
        StringBuilder buffer = new StringBuilder(source);

        while(buffer.length() < length) {
            buffer.append(pad);
        }

        if(buffer.length() > length) {
            return buffer.substring(0, length);
        }
        return buffer.toString();
    }

    /**
     * @param source
     * @param search
     * @param replacement
     * @return String
     */
    public static String replace(String source, String search, String replacement) {
        if(source == null) {
            return EMPTY;
        }

        if(search == null) {
            return source;
        }

        int s = 0;
        int e = 0;
        int d = search.length();
        StringBuilder buffer = new StringBuilder();

        while(true) {
            e = source.indexOf(search, s);

            if(e == -1) {
                buffer.append(source.substring(s));
                break;
            }
            buffer.append(source.substring(s, e)).append(replacement);
            s = e + d;
        }
        return buffer.toString();
    }

    /**
     * @param source
     * @param limit
     * @param trim
     * @param ignoreWhitespace
     * @return String[]
     */
    public static String[] split(String source, String limit, boolean trim, boolean ignoreWhitespace) {
        int i = 0;
        int j = 0;
        String s = null;
        List<String> list = new ArrayList<String>();

        while((j = source.indexOf(limit, i)) > -1) {
            if(j > i) {
                s = source.substring(i, j);

                if(trim) {
                    s = s.trim();
                }

                if(!ignoreWhitespace || s.length() > 0) {
                    list.add(s);
                }
            }
            i = j + limit.length();
        }

        if(i < source.length()) {
            s = source.substring(i);

            if(trim) {
                s = s.trim();
            }

            if(!ignoreWhitespace || s.length() > 0) {
                list.add(s);
            }
        }
        String[] result = new String[list.size()];
        return list.toArray(result);
    }

    /**
     * @param c
     * @return String
     */
    public static String escape(char c) {
        switch (c) {
            case '"': {
                return "\\\"";
            }
            case '\r': {
                return "\\r";
            }
            case '\n': {
                return "\\n";
            }
            case '\t': {
                return "\\t";
            }
            case '\b': {
                return "\\b";
            }
            case '\f': {
                return "\\f";
            }
            case '\\': {
                return "\\\\";
            }
            default : {
                return String.valueOf(c);
            }
        }
    }

    /**
     * @param source
     * @return String
     */
    public static String escape(String source) {
        if(source == null) {
            return "";
        }

        char c;
        StringBuilder buffer = null;

        for(int i = 0, length = source.length(); i < length; i++) {
            c = source.charAt(i);

            switch (c) {
                case '"': {
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("\\\"");
                    break;
                }
                case '\r': {
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("\\r");
                    break;
                }
                case '\n': {
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("\\n");
                    break;
                }
                case '\t': {
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("\\t");
                    break;
                }
                case '\b': {
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("\\b");
                    break;
                }
                case '\f': {
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("\\f");
                    break;
                }
                case '\\': {
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("\\\\");
                    break;
                }
                default : {
                    if(buffer != null) {
                        buffer.append(c);
                    }
                    break;
                }
            }
        }
        return (buffer != null ? buffer.toString() : source);
    }

    /**
     * @param source
     * @return String
     */
    public static String unescape(String source) {
        if(source == null) {
            return "";
        }

        char c;
        StringBuilder buffer = new StringBuilder();

        for(int i = 0, length = source.length(); i < length; i++) {
            c = source.charAt(i);

            if(c == '\\' && (i + 1 < length)) {
                switch (source.charAt(i + 1)) {
                    case '\'': {
                        buffer.append("\'");break;
                    }
                    case '"': {
                        buffer.append("\"");break;
                    }
                    case 'r': {
                        buffer.append("\r");break;
                    }
                    case 'n': {
                        buffer.append("\n");break;
                    }
                    case 't': {
                        buffer.append("\t");break;
                    }
                    case 'b': {
                        buffer.append("\b");break;
                    }
                    case 'f': {
                        buffer.append("\f");break;
                    }
                    case '\\': {
                        buffer.append("\\");break;
                    }
                    default : {
                        buffer.append('\\');
                        buffer.append(source.charAt(i + 1));
                        break;
                    }
                }
                i++;
            }
            else {
                buffer.append(c);
            }
        }
        return buffer.toString();
    }

    /**
     * @param source
     * @param offset
     * @param length
     * @return StringBuilder
     */
    public static StringBuilder getBuffer(String source, int offset, int length) {
        StringBuilder buffer = new StringBuilder();

        if(length > 0) {
            char[] cbuf = new char[length];
            buffer = new StringBuilder((int)(source.length() * 1.2));
            source.getChars(offset, length, cbuf, 0);
            buffer.append(cbuf, 0, length);
        }
        return buffer;
    }

    /**
     * @param source
     * @return String
     */
    public static String compact(String source) {
        return compact(source, "\r\n");
    }

    /**
     * @param source
     * @param crlf
     * @return String
     */
    public static String compact(String source, String crlf) {
        char c;
        boolean b = true;
        int length = source.length();
        StringBuilder buffer = new StringBuilder();

        for(int i = 0; i < length; i++) {
            c = source.charAt(i);

            if(c == '\n') {
                if(b) {
                    buffer.append(crlf);
                    b = false;
                }
            }
            else if(c == '\r') {
                continue;
            }
            else {
                buffer.append(c);
                b = true;
            }
        }
        return buffer.toString();
    }

    /**
     * @param content
     * @param value
     * @return boolean
     */
    public static boolean contains(String content, String value) {
        if(content != null) {
            if(content.trim().equals("*")) {
                return true;
            }

            String[] array = content.split(",");

            for(int i = 0; i < array.length; i++) {
                array[i] = array[i].trim();

                if(array[i].equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param source
     * @return boolean
     */
    public static boolean isJavaIdentifier(String source) {
        if(Character.isJavaIdentifierStart(source.charAt(0)) == false) {
            return false;
        }

        for(int i = 0; i < source.length(); i++) {
            if(Character.isJavaIdentifierPart(source.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
}
