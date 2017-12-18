package com.nh.micro.nhs.util;

import java.util.LinkedHashMap;
import java.util.Map;


public abstract class Node {
    private String nodeName;
    private int nodeType;
    private int offset;
    private int length;
    private int line;
    private int closed;
    private Node parent;
    private Map<String, Attribute> attributes;

    /**
     * @param nodeName
     */
    public Node(String nodeName) {
        this(nodeName, NodeType.NODE);
    }

    /**
     * @param nodeName
     * @param nodeType
     */
    public Node(String nodeName, int nodeType) {
        this.nodeName = nodeName;
        this.nodeType = nodeType;
        this.closed   = 1;
        this.attributes = new LinkedHashMap<String, Attribute>();
    }

    /**
     * @param nodeName
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * @return String
     */
    public String getNodeName() {
        return this.nodeName;
    }

    /**
     * @return the nodeType
     */
    public int getNodeType() {
        return this.nodeType;
    }

    /**
     * @param nodeType the nodeType to set
     */
    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * @param offset the offset to set
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * @return the address
     */
    public int getOffset() {
        return this.offset;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * @return the length
     */
    public int getLength() {
        return this.length;
    }

    /**
     * @return int
     */
    public int getClosed() {
        return this.closed;
    }

    /**
     * @param closed
     */
    public void setClosed(int closed) {
        this.closed = closed;
    }

    /**
     * @return int
     */
    public int getLine() {
        return this.line;
    }

    /**
     * @param line
     */
    public void setLine(int line) {
        this.line = line;
    }

    /**
     * @param name
     * @param value
     */
    public void setAttribute(String name, Attribute value) {
        if(value != null) {
            this.attributes.put(name, value);
        }
        else {
            this.attributes.remove(name);
        }
    }

    /**
     * @param name
     * @param value
     */
    public void setAttribute(String name, String value) {
        if(value != null) {
            this.attributes.put(name, new Attribute(NodeType.TEXT, name, value));
        }
        else {
            this.attributes.remove(name);
        }
    }

    /**
     * @param name
     * @return Attribute
     */
    public Attribute getAttribute(String name) {
        return this.attributes.get(name);
    }

    /**
     * @param name
     * @return Object
     */
    public Object getAttributeValue(String name) {
        Attribute attribute = this.attributes.get(name);

        if(attribute != null) {
            return attribute.getValue();
        }
        return null;
    }

    /**
     * @param name
     * @return String
     */
    public String getAttributeText(String name) {
        Attribute attribute = this.attributes.get(name);

        if(attribute != null) {
            return attribute.getText();
        }
        return null;
    }

    /**
     * @param name
     */
    public void removeAttribute(String name) {
        this.attributes.remove(name);
    }

    /**
     * @return the attributes
     */
    public Map<String, Attribute> getAttributes() {
        return this.attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(Map<String, Attribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the parent
     */
    public Node getParent() {
        return this.parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * @return String
     */
    public String getTextContent() {
        throw new UnsupportedOperationException("unsupported: " + this.nodeName);
    }

    /**
     * @return String
     */
    public String getAttributesHtml() {
        StringBuilder buffer = new StringBuilder();

        if(this.attributes != null && this.attributes.size() > 0) {
            for(Map.Entry<String, Attribute> entry : this.attributes.entrySet()) {
                buffer.append(entry.getKey());
                buffer.append("=\"");
                buffer.append(entry.getValue().getValue());
                buffer.append("\" ");
            }

            if(buffer.length() > 0) {
                buffer.setLength(buffer.length() - 1);
            }
        }
        return buffer.toString();
    }

    /**
     * @param source
     * @return String
     */
    public String encode(String source) {
        if(source == null) {
            return "";
        }

        StringBuilder buffer = new StringBuilder();

        for(int i = 0, length = source.length(); i < length; i++) {
            char c = source.charAt(i);

            switch(c) {
                case '"':
                    buffer.append("&quot;");
                    break;
                case '<':
                    buffer.append("&lt;");
                    break;
                case '>':
                    buffer.append("&gt;");
                    break;
                case '&':
                    buffer.append("&amp;");
                    break;
                case '\'':
                    buffer.append("&#39;");
                    break;
                default:
                    buffer.append(c);
                    break;
            }
        }
        return buffer.toString();
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return this.toString(this.getOffset(), true);
    }

    /**
     * @param index
     * @param sort
     * @return String
     */
    public String toString(int index, boolean sort) {
        StringBuilder buffer = new StringBuilder();

        if(index == this.getOffset()) {
            buffer.append("<");
            buffer.append(this.getNodeName());
            buffer.append(" lineNumber=\"");
            buffer.append(this.getLine());
            buffer.append("\" offset=\"");
            buffer.append(this.getOffset());
            buffer.append("\" length=\"");
            buffer.append(this.getLength());
            buffer.append("\"");
            Map<String, Attribute> attributes = this.getAttributes();

            if(attributes != null && attributes.size() > 0) {
                if(sort == true) {
                    java.util.TreeMap<String, Attribute> treeMap = new java.util.TreeMap<String, Attribute>();
                    treeMap.putAll(attributes);
                    attributes = treeMap;
                }

                for(Map.Entry<String, Attribute> entry : attributes.entrySet()) {
                    buffer.append(" ");
                    buffer.append(entry.getKey());
                    buffer.append("=\"");
                    buffer.append(this.encode(entry.getValue().getText()));
                    buffer.append("\"");
                }
            }

            if(this.closed == NodeType.SELF_CLOSED) {
                buffer.append("/>");
            }
            else {
                buffer.append(">");
            }
        }
        else {
            if(this.closed == NodeType.PAIR_CLOSED) {
                buffer.append("</");
                buffer.append(this.getNodeName());
                buffer.append(">");
            }
        }
        return buffer.toString();
    }
}
