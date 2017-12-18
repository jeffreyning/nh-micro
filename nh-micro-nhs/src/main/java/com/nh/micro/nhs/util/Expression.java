package com.nh.micro.nhs.util;


public class Expression extends DataNode {
    private int flag;
    private String expression;

    /**
     * default
     */
    public Expression() {
        super(NodeType.EXPR_NAME, NodeType.EXPRESSION);
    }

    /**
     * @param nodeName
     */
    public Expression(String nodeName) {
        super(NodeType.EXPR_NAME, NodeType.EXPRESSION);
    }

    /**
     * @param nodeName
     * @param nodeType
     */
    protected Expression(String nodeName, int nodeType) {
        super(NodeType.EXPR_NAME, NodeType.EXPRESSION);
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
     * @return Expression
     */
    @Override
    public Expression clone() {
        return this.copy(new Expression());
    }
}
