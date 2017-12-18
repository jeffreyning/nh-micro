package com.nh.micro.nhs.util;


public abstract class DataNode extends Node {
    private String content;

    protected DataNode() {
        super(NodeType.DATA_NAME, NodeType.CDATA);
    }

    /**
     * @param nodeName
     */
    protected DataNode(String nodeName) {
        super(nodeName, NodeType.CDATA);
    }

    /**
     * @param nodeName
     * @param nodeType
     */
    protected DataNode(String nodeName, int nodeType) {
        super(nodeName, nodeType);
        this.setLength(1);
    }

    /**
     * @return String
     */
    public String getNodeHtml() {
        return this.content;
    }

    /**
     * @param content
     */
    public void setTextContent(String content) {
        if(content != null) {
            this.content = content;
        }
    }

    /**
     * @return String
     */
    @Override
    public String getTextContent() {
        return this.content;
    }

    /**
     * @return String
     */
    public String trim() {
        if(this.content != null) {
            this.content = this.content.trim();
        }
        else {
            this.content = "";
        }
        return this.content;
    }

    /**
     * clear the buffer
     */
    public void clear() {
        this.content = null;
    }

    /**
     * @return String
     */
    public int getContentLength() {
        return (this.content != null ? this.content.length() : 0);
    }

    /**
     * @param node
     * @return T
     */
    public <T extends DataNode> T copy(T node) {
        node.setNodeName(this.getNodeName());
        node.setNodeType(this.getNodeType());
        node.setOffset(this.getOffset());
        node.setLength(this.getLength());
        node.setLine(this.getLine());
        node.setClosed(this.getClosed());
        node.setParent(this.getParent());
        node.setTextContent(this.getTextContent());
        return node;
    }

    /**
     * @return DataNode
     */
    @Override
    public abstract DataNode clone();
}