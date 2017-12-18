package com.nh.micro.nhs.util;

public class Variable extends DataNode {
    private int flag;
    private String expression;

    /**
     * default
     */
    public Variable() {
        super(NodeType.VARI_NAME, NodeType.VARIABLE);
    }

    /**
     * @param nodeName
     */
    public Variable(String nodeName) {
        super(NodeType.VARI_NAME, NodeType.VARIABLE);
    }

    /**
     * @param nodeName
     * @param nodeType
     */
    protected Variable(String nodeName, int nodeType) {
        super(NodeType.VARI_NAME, NodeType.VARIABLE);
    }

    /**
     * @param flag the flag to set
     */
    public void setFlag(int flag) {
        this.flag = flag;
    }

    /**
     * @return the flag
     */
    public int getFlag() {
        return this.flag;
    }

    /**
     * @param expression
     */
    public void setExpression(String expression) {
        this.expression = expression;
        super.setTextContent(expression);
    }

    /**
     * @return the expression
     */
    public String getExpression() {
        return this.expression;
    }

    /**
     * @param content
     */
    @Override
    public void setTextContent(String content) {
        this.setExpression(content);
    }

    /**
     * @return String
     */
    @Override
    public String getTextContent() {
        return this.expression;
    }

    /**
     * @return TextNode
     */
    @Override
    public Variable clone() {
        return this.copy(new Variable());
    }
}
