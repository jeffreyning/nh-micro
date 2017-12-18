package com.nh.micro.nhs.util;



public class AppendableNode extends Node {
    private StringBuilder buffer;

    /**
     * default
     */
    public AppendableNode() {
        this(NodeType.TEXT_NAME, NodeType.TEXT);
    }

    /**
     * @param nodeName
     */
    public AppendableNode(String nodeName) {
        super(nodeName);
        this.buffer = new StringBuilder();
    }

    /**
     * @param nodeName
     * @param nodeType
     */
    public AppendableNode(String nodeName, int nodeType) {
        super(nodeName, nodeType);
        this.buffer = new StringBuilder();
    }

    /**
     * @param content
     * @return AppendableNode
     */
    public AppendableNode append(String content) {
        this.buffer.append(content);
        return this;
    }

    /**
     * @param c
     * @return AppendableNode
     */
    public AppendableNode append(char c) {
        this.buffer.append(c);
        return this;
    }

    /**
     * @param content
     */
    public void setTextContent(String content) {
        this.buffer.setLength(0);
        this.buffer.append(content);
    }

    /**
     * @return String
     */
    @Override
    public String getTextContent() {
        return this.buffer.toString();
    }
}
