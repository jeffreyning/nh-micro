package com.nh.micro.nhs.util;


public class HtmlUtil {
    /**
     * @param source
     * @return String
     */
    public static String encode(String source) {
        if(source == null || source.length() < 1) {
            return "";
        }

        int length = source.length();
        StringBuilder buffer = null;

        for(int i = 0; i < length; i++) {
            char c = source.charAt(i);

            switch(c) {
                case '"':
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("&quot;");
                    break;
                case '<':
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("&lt;");
                    break;
                case '>':
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("&gt;");
                    break;
                case '&':
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("&amp;");
                    break;
                case '\'':
                    if(buffer == null) {
                        buffer = StringUtil.getBuffer(source, 0, i);
                    }
                    buffer.append("&#39;");
                    break;
                default:
                    if(buffer != null) {
                        buffer.append(c);
                    }
                    break;
            }
        }
        return (buffer != null ? buffer.toString() : source);
    }

    /**
     * @param source
     * @return String
     */
    public static String decode(String source) {
        if(source == null) {
            return "";
        }

        int length = source.length();
        char[] c = source.toCharArray();
        StringBuilder buffer = new StringBuilder(length);

        for(int i = 0; i < length; i++) {
            if(c[i] == '&') {
                if(((i + 3) < length) && (c[i + 1] == 'l') && (c[i + 2] == 't') && (c[i + 3] == ';')) {
                    // &lt;
                    buffer.append('<');
                    i += 3;
                }
                else if(((i + 3) < length) && (c[i + 1] == 'g') && (c[i + 2] == 't') && (c[i + 3] == ';')) {
                    // &gt;
                    buffer.append('>');
                    i += 3;
                }
                else if (((i + 4) < length) && (c[i + 1] == 'a') && (c[i + 2] == 'm') && (c[i + 3] == 'p') && (c[i + 4] == ';')) {
                    // &amp;
                    buffer.append('&');
                    i += 4;
                }
                else if(((i + 5) < length) && (c[i + 1] == 'q') && (c[i + 2] == 'u') && (c[i + 3] == 'o') && (c[i + 4] == 't') && (c[i + 5] == ';') ) {
                    // &quot;
                    buffer.append('"');
                    i += 5;
                }
                else if(((i + 3) < length && (c[i + 1] == '#') && Character.isDigit(c[i + 2]))) {
                    // &#10;
                    for(int j = i + 2; j < length; j++) {
                        if(Character.isDigit(c[j])) {
                            continue;
                        }
                        if(c[j] != ';') {
                            buffer.append('&');
                        }
                        else {
                            try {
                                int charCode = Integer.parseInt(new String(c, i + 2, j - i - 2));
                                buffer.append((char)charCode);
                            }
                            catch(NumberFormatException e) {
                                buffer.append(new String(c, i + 2, j - i - 2));
                            }
                            i = j;
                        }

                        break;
                    }
                }
                else {
                    buffer.append('&');
                }
            }
            else {
                buffer.append(c[i]);
            }
        }
        return buffer.toString();
    }

    /**
     * @param source
     * @return String
     */
    public static String remove(String source) {
        if(source == null) {
            return "";
        }

        char c;
        int i = 0;
        int length = source.length();
        String nodeName = null;
        StringBuilder temp = new StringBuilder();
        StringBuilder buffer = new StringBuilder();

        while(i < length) {
            c = source.charAt(i);

            if(c == '<') {
                if((i + 3) < length && source.charAt(i + 1) == '!' && source.charAt(i + 2) == '-' && source.charAt(i + 3) == '-') {
                    i = skipComment(source, i + 4);
                }
                else {
                    i = readNodeName(source, temp, i + 1);
                    i = source.indexOf('>', i);

                    if(i < 0) {
                        break;
                    }

                    nodeName = temp.toString();
                    temp.setLength(0);
                    i++;

                    if(nodeName.equals("script") || nodeName.equals("style")) {
                        i = skipContent(source, i);
                    }
                }
            }
            else {
                buffer.append(c);
                i++;
            }
        }
        return buffer.toString();
    }

    /**
     * @param source
     * @param offset
     * @return int
     */
    private static int skipComment(String source, int offset) {
        char c;
        int i = offset;
        int length = source.length();

        while(i < length) {
            c = source.charAt(i);

            if(c == '-') {
                if((i + 2) < length && source.charAt(i + 1) == '-' && source.charAt(i + 2) == '>') {
                    i = i + 3;
                    break;
                }
            }
            i++;
        }
        return i;
    }

    /**
     * @param source
     * @param offset
     * @return int
     */
    private static int skipContent(String source, int offset) {
        char c;
        int i = offset;
        int length = source.length();
        String nodeName = null;
        StringBuilder buffer = new StringBuilder();

        while(i < length) {
            c = source.charAt(i);

            if(c == '<' && i + 1 < length && source.charAt(i + 1) == '/') {
                i = readNodeName(source, buffer, i + 2);
                nodeName = buffer.toString().trim();
                buffer.setLength(0);

                if(nodeName.equals("script") || nodeName.equals("style")) {
                    break;
                }
            }
            else {
                i++;
            }
        }

        while(i < length) {
            c = source.charAt(i++);

            if(c == '>') {
                break;
            }
        }
        return i;
    }

    /**
     * @param source
     * @param buffer
     * @param offset
     * @return int
     */
    private static int readNodeName(String source, StringBuilder buffer, int offset) {
        char c;
        int i = offset;
        
        while(i < source.length()) {
            c = source.charAt(i);

            if(Character.isWhitespace(c) || Character.isISOControl(c) || c == '/' || c == '>') {
                break;
            }
            else {
                buffer.append(Character.toLowerCase(c));
                i++;
            }
        }
        return i;
    }

    /**
     * @param text
     * @param length
     * @return String
     */
    protected static String right(String text, int offset, int length) {
        if(text == null) {
            return "";
        }

        if((text.length() - offset) > length) {
            return text.substring(offset, text.length() - length);
        }
        else {
            return text.substring(offset);
        }
    }
}