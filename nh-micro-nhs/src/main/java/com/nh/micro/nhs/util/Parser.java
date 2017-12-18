package com.nh.micro.nhs.util;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class Parser {
    protected Stream stream;

    /**
     */
    public Parser() {
    }

    /**
     * @return String
     * @throws IOException
     */
    protected String getNodeName() throws IOException {
        int i;
        StringBuilder buffer = new StringBuilder();

        while((i = this.stream.peek()) != -1) {
            if(Character.isLetter(i) || Character.isDigit(i) || i == ':' || i == '-' || i == '_' || i == '.') {
                buffer.append((char)i);
                this.stream.read();
            }
            else {
                break;
            }
        }
        return buffer.toString();
    }

    /**
     * read node name, after read nodeName
     * @return String
     * @throws IOException 
     */
    protected AttributeList getAttributeList() throws IOException {
        int i;
        int quote;
        String name = null;
        String value = null;
        StringBuilder buffer = new StringBuilder();
        Map<String, String> attributes = new LinkedHashMap<String, String>();
        Stream stream = this.stream;

        while(true) {
            // skip invalid character
            while((i = stream.peek()) != Stream.EOF) {
                if(Character.isLetter(i) || Character.isDigit(i) || i == ':' || i == '-' || i == '_' || i == '%' || i == '/' || i == '>') {
                    break;
                }
                else {
                    stream.read();
                }
            }

            // check end
            if(i == Stream.EOF) {
                break;
            }

            if(i == '>') {
                break;
            }
            else if(i == '%' || i == '/') {
                if(stream.peek(1) == '>') {
                    break;
                }
                continue;
            }
            else {
            }

            // read name
            while((i = stream.peek()) != -1) {
                if(Character.isLetter(i) || Character.isDigit(i) || i == ':' || i == '-' || i == '_') {
                    buffer.append((char)i);
                    stream.read();
                }
                else {
                    break;
                }
            }

            name = buffer.toString();
            buffer.setLength(0);

            if(name.length() < 1) {
                continue;
            }

            this.stream.skipWhitespace();
            i = this.stream.peek();

            // next character must be '='
            if(i != '=') {
                attributes.put(name, "");
                continue;
            }
            else {
                this.stream.read();
            }

            this.stream.skipWhitespace();
            i = stream.peek();

            if(i == '"') {
                quote = '"';
                stream.read();
            }
            else if(i == '\'') {
                quote = '\'';
                stream.read();
            }
            else {
                quote = ' ';
            }

            if(quote == ' ') {
                value = this.getAttributeValue(buffer);
            }
            else {
                value = this.getAttributeValue(buffer, quote);
            }
            attributes.put(name, value);
            buffer.setLength(0);
        }
        this.stream.skipWhitespace();
        return this.getAttributeList(attributes);
    }

    /**
     * @param buffer
     * @return String
     * @throws IOException
     */
    private String getAttributeValue(StringBuilder buffer) throws IOException {
        int i = 0;

        while((i = this.stream.read()) != -1) {
            if(i == '#' && this.stream.peek() == '{') {
                this.stream.read();
                buffer.append("#{");

                while((i = this.stream.read()) != -1) {
                    buffer.append((char)i);

                    if(i == '}') {
                        break;
                    }
                }
                continue;
            }        	
            if(i == '$' && this.stream.peek() == '{') {
                this.stream.read();
                buffer.append("${");

                while((i = this.stream.read()) != -1) {
                    buffer.append((char)i);

                    if(i == '}') {
                        break;
                    }
                }
                continue;
            }

            if(i == '<' && this.stream.peek() == '%' && this.stream.peek(1) == '=') {
                this.stream.read();
                buffer.append("<%=");

                while((i = this.stream.read()) != -1) {
                    buffer.append((char)i);

                    if(i == '%' && this.stream.peek() == '>') {
                        this.stream.read();
                        buffer.append('>');
                        break;
                    }
                }
                continue;
            }

            if(i <= ' ' || i == '>') {
                break;
            }
            else if(i == '/' && this.stream.peek() == '>') {
                break;
            }
            else {
                buffer.append((char)i);
            }
        }
        return HtmlUtil.decode(buffer.toString());
    }

    /**
     * @param buffer
     * @param quote
     * @return String
     * @throws IOException
     */
    private String getAttributeValue(StringBuilder buffer, int quote) throws IOException {
        int i = 0;

        while((i = this.stream.read()) != -1) {
            if(i == '#' && this.stream.peek() == '{') {
                this.stream.read();
                buffer.append("#{");

                while((i = this.stream.read()) != -1) {
                    buffer.append((char)i);

                    if(i == '}') {
                        break;
                    }
                }
                continue;
            }        	
            if(i == '$' && this.stream.peek() == '{') {
                this.stream.read();
                buffer.append("${");

                while((i = this.stream.read()) != -1) {
                    buffer.append((char)i);

                    if(i == '}') {
                        break;
                    }
                }
                continue;
            }

            if(i == '<' && this.stream.peek() == '%' && this.stream.peek(1) == '=') {
                this.stream.read();
                this.stream.read();
                buffer.append("<%=");

                while((i = this.stream.read()) != -1) {
                    buffer.append((char)i);

                    if(i == '%' && this.stream.peek() == '>') {
                        this.stream.read();
                        buffer.append('>');
                        break;
                    }
                }
                continue;
            }

            if(i == quote) {
                break;
            }
            else {
                buffer.append((char)i);
            }
        }
        return HtmlUtil.decode(buffer.toString());
    }

    /**
     * @param attributes
     * @return Map<String, Attribute>
     */
    private AttributeList getAttributeList(Map<String, String> attributes) {
        Map<String, Attribute> map = new LinkedHashMap<String, Attribute>();

        /**
         * 属�?表达式的支持情况.
         * 1. 允许文本和el表达式同时存�?
         * 2. 只允许独立的jsp表达�?
         * 
         * ---------------------------------------------------------
         *    type    | example                          | support |
         * ---------------------------------------------------------
         *    text    | a="admin"                        | Y       |
         * ---------------------------------------------------------
         *     el     | a="${user.name}"                 | Y       |
         * ---------------------------------------------------------
         *     jsp    | a="<%=user.getName()%>"          | Y       |
         * ---------------------------------------------------------
         *     mix    | a="Hello, ${user.name}!"         | Y       |
         * ---------------------------------------------------------
         *     mix    | a="Hello, <%=user.getName()%>!"  | N       |
         * ---------------------------------------------------------
         *     mix    | a="Hello, <%=123%>${true}"       | N       |
         * ---------------------------------------------------------
         */
        for(Map.Entry<String, String> entry : attributes.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue().trim();
            Attribute attribute = this.getAttribute(name, value);
            map.put(name, attribute);
        }
        return new AttributeList(map);
    }

    /**
     * @param name
     * @param expression
     * @return Attribute
     */
    protected Attribute getAttribute(String name, String expression) {
        /**
         * 返回节点三种类型: TEXT, EXPRESSION, JSP_EXPRESSION
         */
        List<Node> nodes = ELUtil.parse(expression);

        if(nodes.size() < 1) {
            return new Attribute(Attribute.STRING, name, expression);
        }

        if(nodes.size() == 1) {
            Node node = nodes.get(0);

            if(node.getNodeType() == NodeType.TEXT) {
                Object value = ClassUtil.guess(node.getTextContent());

                if(value instanceof Boolean) {
                    return new Attribute(Attribute.BOOLEAN, name, value);
                }
                else if(value instanceof Number) {
                    return new Attribute(Attribute.NUMBER, name, value);
                }
                else {
                    return new Attribute(Attribute.STRING, name, value);
                }
            }
            else if(node.getNodeType() == NodeType.EXPRESSION) {
                if(StringUtil.isJavaIdentifier(node.getTextContent())) {
                    return new Attribute(Attribute.VARIABLE, name, node.getTextContent());
                }
                else {
                    return new Attribute(Attribute.EXPRESSION, name, node.getTextContent());
                }
            }
            else {
                return new Attribute(Attribute.JSP_EXPRESSION, name, node.getTextContent());
            }
        }

        /**
         * 处理规则
         * 1. 如果只有�?��节点, 直接返回该节�?
         * 节点的类型决定了�?��计算出来的�?类型.
         * 
         * 2. 超过�?��节点认为是混合型表达�?
         * 混合型表达式的返回�?�?��是String类型.
         * 例如: test="Hello, ${user.name}"
         * 属�?test的�?就是�?��混合型表达式
         * 混合型表达式不允许出现jsp表达�?         * 例如: test="Hello, <%=user.name%>" 是非法的, 但是不报�? 处理为字符串.
         * tomcat也是这样处理�?
         * 
         * value=" ${user} " 这样的写法会认为是混合型表达�? 因为容器认为你确实需要两边有空格.
         * �?��value的�?�? (" " + user.toString() + " ")
         * 如果希望 value 的�?是user对象, 就必须写�? value="${user}" 或�? value="<%=user%>"
         * 
         * value=" <%=user%> ", 这样的写法会被认为value的�?是字符串 " <%=user%> ".
         * 如果使用jsp表达�? jsp表达式必须单独出�? 只要不是单独出现, jsp表达式就会无�?
         * 
         * jsp表达式可能存在特殊的情况: abc<%="${user.name}"%>xyz${user.name}
         */
        for(Node node : nodes) {
            if(node instanceof Expression) {
                return new Attribute(Attribute.MIX_EXPRESSION, name, expression);
            }
        }

        /**
         * 如果没有EXPRESSION节点, 那么�?��存在JSP_EXPRESSION
         * 混合节点不允许存在JSP_EXPRESSION, 按照文本节点处理.
         */
        return new Attribute(Attribute.STRING, name, expression);
    }

    /**
     * @throws IOException
     */
    protected String readText() throws IOException {
        int i = 0;
        StringBuilder buffer = new StringBuilder();

        while((i = this.stream.peek()) != -1) {
            if(i == '<') {
                break;
            }
            else if(i == '$' && this.stream.peek(1) == '{') {
                break;
            }
            else if(i == '#' && this.stream.peek(1) == '{') {
                break;
            }            
            else {
                buffer.append((char)i);
                this.stream.read();
            }
        }
        return buffer.toString();
    }

    /**
     * @return String
     * @throws IOException 
     */
    protected String readJspComment() throws IOException {
        int ln = this.stream.getLine();
        String comment = this.stream.readUntil("--%>");

        if(comment == null) {
            throw new RuntimeException("at line #" + ln + " The 'jsp:comment' direction must be ends with '--%>'"); 
        }
        return comment;
    }

    /**
     * @return String
     * @throws IOException
     */
    protected String readJspScriptlet() throws IOException {
        int ln = this.stream.getLine();
        this.stream.skipCRLF();
        String script = this.stream.readUntil("%>");

        if(script == null) {
            throw new RuntimeException("at line #" + ln + " The 'jsp:directive' direction must be ends with '%>'");
        }
        return script;
    }

    /**
     * @param nodeName
     * @return String
     * @throws IOException
     */
    public String readNodeContent(String nodeName) throws IOException {
        int ln = this.stream.getLine();
        String content = this.stream.readUntil("</" + nodeName + ">");

        if(content == null) {
            throw new RuntimeException("Exception at #" + ln + ", node not match: [" + nodeName + "].");
        }
        return content;
    }

    /**
     * @param source
     * @return String
     */
    protected String ltrim(String source) {
        if(source == null) {
            return "";
        }

        int i = 0;
        int length = source.length();

        while(i < length && source.charAt(i) <= ' ') {
            i++;
        }
        return (i > 0 ? source.substring(i) : source);
    }

    /**
     * @param content
     * @return boolean
     */
    protected boolean isEmpty(String content) {
        int i = 0;
        int length = content.length();

        while(i < length && content.charAt(i) <= ' ') {
            i++;
        }
        return (i >= length);
    }

    /**
     * @param content
     * @return boolean
     */
    protected boolean isEmpty(StringBuilder content) {
        int i = 0;
        int length = content.length();

        while(i < length && content.charAt(i) <= ' ') {
            i++;
        }
        return (i >= length);
    }
}
