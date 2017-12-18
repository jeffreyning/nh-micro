package com.nh.micro.nhs.util;

import java.util.List;
import java.util.Map;


public class NodeUtil {
    /**
     * @param nodes
     * @return String
     */
    public static String getDescription(List<Node> nodes) {
        StringBuilder buffer = new StringBuilder();

        for(Node node : nodes) {
            buffer.append(getDescription(node));
            buffer.append("\r\n");
        }
        return buffer.toString();
    }

    /**
     * @param node
     * @return String
     */
    public static String getDescription(Node node) {
        int nodeType = node.getNodeType();
        StringBuilder buffer = new StringBuilder();

        if(nodeType == NodeType.TEXT) {
            buffer.append("<text>");
            buffer.append(node.getTextContent());
            buffer.append("</text>");
            return buffer.toString();
        }

        if(nodeType == NodeType.COMMENT) {
            buffer.append(node.getTextContent());
            return buffer.toString();
        }

        if(nodeType == NodeType.VARIABLE || nodeType == NodeType.EXPRESSION) {
            buffer.append("${");
            buffer.append(node.getTextContent());
            buffer.append("}");
            return buffer.toString();
        }

        buffer.append("<");
        buffer.append(node.getNodeName());
        Map<String, Attribute> attributes = node.getAttributes();

        if(attributes != null && attributes.size() > 0) {
            for(Map.Entry<String, Attribute> entrySet : attributes.entrySet()) {
                buffer.append(" ");
                buffer.append(entrySet.getKey());
                buffer.append("=\"");
                buffer.append(HtmlUtil.encode(entrySet.getValue().getText()));
                buffer.append("\"");
            }
        }

        if(node.getClosed() == NodeType.PAIR_CLOSED) {
            buffer.append(">...");
            buffer.append("</");
            buffer.append(node.getNodeName());
            buffer.append(">");
        }
        else {
            buffer.append("/>");
        }
        return buffer.toString();
    }

    /**
     * @param attributes
     * @return String
     */
    public static String toString(Map<?, ?> attributes) {
        StringBuilder buffer = new StringBuilder();

        if(attributes != null && attributes.size() > 0) {
            for(Map.Entry<?, ?> entrySet : attributes.entrySet()) {
                buffer.append(entrySet.getKey());
                buffer.append("=\"");
                buffer.append(entrySet.getValue().toString());
                buffer.append("\"");
                buffer.append(" ");
            }

            if(buffer.length() > 0) {
                buffer.deleteCharAt(buffer.length() - 1);
            }
        }
        return buffer.toString();
    }
}
