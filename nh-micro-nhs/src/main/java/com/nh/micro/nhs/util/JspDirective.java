package com.nh.micro.nhs.util;



public class JspDirective extends DataNode {
    /**
     *
     */
    public JspDirective() {
        super(NodeType.JSP_DIRECTIVE_PAGE_NAME, NodeType.JSP_DIRECTIVE_PAGE);
    }

    /**
     * @param nodeName
     */
    public JspDirective(String nodeName) {
        super(nodeName, NodeType.JSP_DIRECTIVE_PAGE);
    }

    /**
     * @param nodeName
     * @param nodeType
     */
    protected JspDirective(String nodeName, int nodeType) {
        super(nodeName, nodeType);
    }

    /**
     * @param nodeName
     * @return JspDirective
     */
    public static JspDirective getInstance(String nodeName) {
        if(nodeName.equals("jsp:directive.page")) {
            return getPageDirective();
        }
        else if(nodeName.equals("jsp:directive.taglib")) {
            return getTaglibDirective();
        }
        else if(nodeName.equals("jsp:directive.include")) {
            return getIncludeDirective();
        }
        else {
            return getPageDirective();
        }
    }

    /**
     * @return JspDirective
     */
    public static JspDirective getPageDirective() {
        return new JspDirective(NodeType.JSP_DIRECTIVE_PAGE_NAME, NodeType.JSP_DIRECTIVE_PAGE);
    }

    /**
     * @return JspDirective
     */
    public static JspDirective getTaglibDirective() {
        return new JspDirective(NodeType.JSP_DIRECTIVE_TAGLIB_NAME, NodeType.JSP_DIRECTIVE_TAGLIB);
    }

    /**
     * @return JspDirective
     */
    public static JspDirective getIncludeDirective() {
        return new JspDirective(NodeType.JSP_DIRECTIVE_INCLUDE_NAME, NodeType.JSP_DIRECTIVE_INCLUDE);
    }

    /**
     * clone
     */
    @Override
    public JspDirective clone() {
        return this.copy(new JspDirective());
    }
}
