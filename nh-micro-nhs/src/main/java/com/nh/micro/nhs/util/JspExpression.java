package com.nh.micro.nhs.util;


public class JspExpression extends DataNode {
    /**
     *
     */
    public JspExpression() {
        super(NodeType.JSP_EXPRESSION_NAME, NodeType.JSP_EXPRESSION);
        this.setClosed(NodeType.PAIR_CLOSED);
    }

    /**
     * @param nodeName
     */
    public JspExpression(String nodeName) {
        super(NodeType.JSP_EXPRESSION_NAME, NodeType.JSP_EXPRESSION);
        this.setClosed(NodeType.PAIR_CLOSED);
    }

    /**
     * @param nodeName
     * @param nodeType
     */
    protected JspExpression(String nodeName, int nodeType) {
        super(NodeType.JSP_EXPRESSION_NAME, NodeType.JSP_EXPRESSION);
        this.setClosed(NodeType.PAIR_CLOSED);
    }

    /**
     * clone
     */
    @Override
    public JspExpression clone() {
        return this.copy(new JspExpression());
    }
}
