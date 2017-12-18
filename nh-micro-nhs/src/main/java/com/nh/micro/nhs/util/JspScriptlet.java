package com.nh.micro.nhs.util;


public class JspScriptlet extends DataNode {
    /**
     *
     */
    public JspScriptlet() {
        super(NodeType.JSP_SCRIPTLET_NAME, NodeType.JSP_SCRIPTLET);
        this.setClosed(NodeType.PAIR_CLOSED);
    }

    /**
     * @param nodeName
     */
    public JspScriptlet(String nodeName) {
        super(NodeType.JSP_SCRIPTLET_NAME, NodeType.JSP_SCRIPTLET);
        this.setClosed(NodeType.PAIR_CLOSED);
    }

    /**
     * @param nodeName
     * @param nodeType
     */
    protected JspScriptlet(String nodeName, int nodeType) {
        super(NodeType.JSP_SCRIPTLET_NAME, NodeType.JSP_SCRIPTLET);
        this.setClosed(NodeType.PAIR_CLOSED);
    }

    /**
     * clone
     */
    @Override
    public JspScriptlet clone() {
        return this.copy(new JspScriptlet());
    }
}
