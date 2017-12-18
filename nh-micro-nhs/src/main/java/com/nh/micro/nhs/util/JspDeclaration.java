package com.nh.micro.nhs.util;


public class JspDeclaration extends DataNode {
    /**
     *
     */
    public JspDeclaration() {
        super(NodeType.JSP_DECLARATION_NAME, NodeType.JSP_DECLARATION);
        this.setClosed(NodeType.PAIR_CLOSED);
    }

    /**
     * @param nodeName
     */
    public JspDeclaration(String nodeName) {
        super(NodeType.JSP_DECLARATION_NAME, NodeType.JSP_DECLARATION);
        this.setClosed(NodeType.PAIR_CLOSED);
    }

    /**
     * @param nodeName
     * @param nodeType
     */
    protected JspDeclaration(String nodeName, int nodeType) {
        super(NodeType.JSP_DECLARATION_NAME, NodeType.JSP_DECLARATION);
        this.setClosed(NodeType.PAIR_CLOSED);
    }

    /**
     * clone
     */
    @Override
    public JspDeclaration clone() {
        return this.copy(new JspDeclaration());
    }
}
