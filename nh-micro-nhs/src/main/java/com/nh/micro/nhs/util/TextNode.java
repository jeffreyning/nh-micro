package com.nh.micro.nhs.util;

public class TextNode extends DataNode {
    /**
     *
     */
    public TextNode() {
        super(NodeType.TEXT_NAME, NodeType.TEXT);
    }

    /**
     * @param nodeName
     */
    public TextNode(String nodeName) {
        super(NodeType.TEXT_NAME, NodeType.TEXT);
    }

    /**
     * @param nodeName
     * @param nodeType
     */
    protected TextNode(String nodeName, int nodeType) {
        super(NodeType.TEXT_NAME, NodeType.TEXT);
    }

    /**
     * @return TextNode
     */
    @Override
    public TextNode clone() {
        TextNode node = new TextNode();
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
}
