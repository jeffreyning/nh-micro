package com.nh.micro.nhs;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import com.nh.micro.nhs.util.AttributeList;
import com.nh.micro.nhs.util.Expression;
import com.nh.micro.nhs.util.HtmlUtil;
import com.nh.micro.nhs.util.JspDeclaration;
import com.nh.micro.nhs.util.JspDirective;
import com.nh.micro.nhs.util.JspExpression;
import com.nh.micro.nhs.util.JspScriptlet;
import com.nh.micro.nhs.util.Node;
import com.nh.micro.nhs.util.NodeType;
import com.nh.micro.nhs.util.NodeUtil;
import com.nh.micro.nhs.util.Parser;
import com.nh.micro.nhs.util.Path;
import com.nh.micro.nhs.util.Stack;
import com.nh.micro.nhs.util.Stream;
import com.nh.micro.nhs.util.StringUtil;
import com.nh.micro.nhs.util.TextNode;
import com.nh.micro.nhs.util.Variable;





public class JspParser extends Parser {


    private boolean ignoreJspTag = true;


    protected static final String JSP_DIRECTIVE_PAGE    = "jsp:directive.page";
    protected static final String JSP_DIRECTIVE_TAGLIB  = "jsp:directive.taglib";
    protected static final String JSP_DIRECTIVE_INCLUDE = "jsp:directive.include";
    protected static final String JSP_DECLARATION       = "jsp:declaration";
    protected static final String JSP_SCRIPTLET         = "jsp:scriptlet";
    protected static final String JSP_EXPRESSION        = "jsp:expression";

    protected static final String TPL_DIRECTIVE_TAGLIB  = "t:taglib";
    protected static final String TPL_DIRECTIVE_IMPORT  = "t:import";
    protected static final String TPL_DIRECTIVE_RENAME  = "t:rename";
    protected static final String TPL_DIRECTIVE_INCLUDE = "t:include";
    protected static final String TPL_DIRECTIVE_TEXT    = "t:text";
    protected static final String TPL_DIRECTIVE_COMMENT = "t:comment";


    /**
     * @param sourceFactory
     */
    public JspParser() {

    }


    
    public List parse(InputStream inputStream, String charset) throws Exception {


        //InputStream inputStream = new ByteArrayInputStream(content.getBytes(charset));

        try {
            int i;
            this.stream = Stream.getStream(inputStream, charset, 32);
            Stack<Node> stack = new Stack<Node>();
            List<Node> list = new ArrayList<Node>();
            this.stream.skipWhitespace();
    
            while((i = this.stream.peek()) != -1) {
                if(i == '<') {
                    this.stream.read();
                    this.parseStartTag(stack, list);
                }
                else if(i == '$' && this.stream.peek(1) == '{') {
                    this.stream.skip(2);
                    this.parseExpression(stack, list);
                }
                else if(i == '#' && this.stream.peek(1) == '{') {
                    this.stream.skip(2);
                    this.parseExpression2(stack, list);
                }                
                else {
                    this.parseText(stack, list);
                }
            }
    
            if(stack.peek() != null) {
                Node node = stack.peek();
                throw new Exception("Exception at line #" + node.getLine() + " " + NodeUtil.getDescription(node) + " not match !");
            }
            return list;
        }
        finally {
            try {
                inputStream.close();
            }
            catch(IOException e) {
            }
        }
    }    
    


    /**
     * @param stack
     * @param list
     * @throws IOException
     */
    private void parseText(Stack<Node> stack, List<Node> list) throws IOException {
        int line = this.stream.getLine();
        String text = this.readText();

        if(text.length() > 0) {
            this.pushTextNode(stack, list, text, line);
        }
    }

    /**
     * @param source
     * @param stack
     * @param list
     * @throws IOException
     */
    private void parseExpression(Stack<Node> stack, List<Node> list) throws IOException {
        int flag = 0;
        int line = this.stream.getLine();
        String expression = this.stream.readUntil('}');

        if(expression == null) {
            throw new RuntimeException("Exception at #" + line + ", bad expr.");
        }

        expression = expression.trim();

        if(expression.startsWith("?")) {
            this.pushTextNode(stack, list, "${" + expression.substring(1) + "}", line);
            return;
        }

        if(expression.startsWith("#") || expression.startsWith("&")) {
            flag = expression.charAt(0);
            expression = expression.substring(1).trim();
        }

        if(expression.length() > 0) {
            if(StringUtil.isJavaIdentifier(expression)) {
                Variable variable = new Variable();
                variable.setParent(stack.peek());
                variable.setOffset(list.size());
                variable.setLength(1);
                variable.setLine(line);
                variable.setFlag(flag);
                variable.setExpression(expression);
                list.add(variable);
            }
            else {
                Expression expr = new Expression();
                expr.setParent(stack.peek());
                expr.setOffset(list.size());
                expr.setLength(1);
                expr.setLine(line);
                expr.setFlag(flag);
                expr.setExpression(expression);
                list.add(expr);
            }
        }
    }

    private void parseExpression2(Stack<Node> stack, List<Node> list) throws IOException {
        int flag = 0;
        int line = this.stream.getLine();
        String expression = this.stream.readUntil('}');

        if(expression == null) {
            throw new RuntimeException("Exception at #" + line + ", bad expr.");
        }

        expression = expression.trim();

        if(expression.startsWith("?")) {
            this.pushTextNode(stack, list, "${" + expression.substring(1) + "}", line);
            return;
        }

        if(expression.startsWith("#") || expression.startsWith("&")) {
            flag = expression.charAt(0);
            expression = expression.substring(1).trim();
        }

        if(expression.length() > 0) {
            if(StringUtil.isJavaIdentifier(expression)) {
                Variable variable = new Variable();
                variable.setParent(stack.peek());
                variable.setOffset(list.size());
                variable.setLength(1);
                variable.setLine(line);
                variable.setFlag(flag);
                variable.setExpression("#"+expression);
                list.add(variable);
            }
            else {
                Expression expr = new Expression();
                expr.setParent(stack.peek());
                expr.setOffset(list.size());
                expr.setLength(1);
                expr.setLine(line);
                expr.setFlag(flag);
                expr.setExpression("#"+expression);
                list.add(expr);
            }
        }
    }
    /**
     * @param source
     * @param stack
     * @param list
     * @throws Exception
     */
    public void parseStartTag(Stack<Node> stack, List<Node> list) throws Exception {
        int n = this.stream.peek();
        int line = this.stream.getLine();

        if(n == '/') {
            this.stream.read();
            this.parseEndTag(stack, list);
        }
        else if(n == '%') {
            this.stream.read();
            this.parseJsp(stack, list);
        }
        else if(n == '!') {
            this.stream.read();
            this.pushTextNode(stack, list, "<!", line);
        }
        else {
            String nodeName = this.getNodeName();

            if(this.isTplDirective(nodeName)) {
                this.parseTplDirective(stack, list, nodeName);
            }
            else if(this.isJspDirective(nodeName)) {
                this.parseJspDirective(stack, list, nodeName);
            }
            else {
                this.parseJspTag(stack, list, nodeName);
            }
        }
    }

    /**
     * @param source
     * @param stack
     * @param list
     * @throws Exception
     */
    private void parseEndTag(Stack<Node> stack, List<Node> list) throws Exception {
        int line = this.stream.getLine();
        String nodeName = this.getNodeName();

        if(nodeName.length() > 0) {
            if(this.isTplDirective(nodeName)) {
                throw new Exception("at line " + line + ": " + nodeName + " not match !");
            }

            this.pushTextNode(stack, list, "</" + nodeName, line);
        }
        else {
            this.pushTextNode(stack, list, "</", line);
        }
    }

    /**
     * @param source
     * @param stack
     * @param list
     * @param nodeName
     * @throws Exception
     */
    private void parseTplDirective(Stack<Node> stack, List<Node> list, String nodeName) throws Exception {
        if(list.size() > 0) {
            this.clip(list.get(list.size() - 1), 1);
        }

        int line = this.stream.getLine();
        AttributeList attributes = this.getAttributeList();

        if(nodeName.equals(TPL_DIRECTIVE_TAGLIB)) {
            if(this.stream.read() == '/') {
                this.stream.skipUntil('>');
            }
            else {
                throw new Exception("The '" + nodeName + "' direction must be self-closed!");
            }

            String prefix = attributes.getText("prefix");
            String uri = attributes.getText("uri");
           
            this.stream.skipLine();
        }
        else if(nodeName.equals(TPL_DIRECTIVE_IMPORT)) {
            if(this.stream.read() == '/') {
                this.stream.skipUntil('>');
            }
            else {
                throw new Exception("The '" + nodeName + "' direction must be self-closed!");
            }

            String name = attributes.getText("name");
            String className = attributes.getText("className");
            String bodyContent = attributes.getText("bodyContent");
            String ignoreWhitespace = attributes.getText("ignoreWhitespace");
            String description = attributes.getText("description");
           
            this.stream.skipLine();
        }
        else if(nodeName.equals(TPL_DIRECTIVE_RENAME)) {
            if(this.stream.read() == '/') {
                this.stream.skipUntil('>');
            }
            else {
                throw new Exception("The '" + nodeName + "' direction must be self-closed!");
            }

            String from = attributes.getText("from");
            String name = attributes.getText("name");
         
            this.stream.skipLine();
        }
        else if(nodeName.equals(TPL_DIRECTIVE_INCLUDE)) {
            if(this.stream.read() == '/') {
                this.stream.skipUntil('>');
            }
            else {
                throw new Exception("The '" + nodeName + "' direction must be self-closed!");
            }

            String file = attributes.getText("file");
            String type = attributes.getText("type");
            String encoding = attributes.getText("encoding");

            this.stream.skipLine();
        }
        else if(nodeName.equals(TPL_DIRECTIVE_TEXT)) {
            this.stream.readUntil('>');
            String escape = attributes.getText("escape");
            String content = this.readNodeContent(nodeName);

            if("xml".equals(escape)) {
                content = HtmlUtil.encode(content);
            }
            this.pushTextNode(stack, list, content, line);
            this.stream.skipLine();
        }
        else if(nodeName.equals(TPL_DIRECTIVE_COMMENT)) {
            this.stream.readUntil('>');
            this.readNodeContent(nodeName);
            this.stream.skipLine();
        }
    }

    /**
     * @param source
     * @param stack
     * @param list
     * @param nodeName
     * @throws Exception
     */
    private void parseJspDirective(Stack<Node> stack, List<Node> list, String nodeName) throws Exception {
        int line = this.stream.getLine();
        AttributeList attributes = this.getAttributeList();

        if(nodeName.equals(JSP_DIRECTIVE_PAGE) || nodeName.equals(JSP_DIRECTIVE_TAGLIB) || nodeName.equals(JSP_DIRECTIVE_INCLUDE)) {
            if(this.stream.read() == '/') {
                this.stream.skipUntil('>');
            }
            else {
                throw new Exception("The '" + nodeName + "' direction must be self-closed!");
            }

            JspDirective node = JspDirective.getInstance(nodeName);
            node.setLine(line);
            node.setOffset(list.size());
            node.setAttributes(attributes.getAttributes());
            node.setClosed(NodeType.SELF_CLOSED);
            this.pushJspNode(stack, list, node);
        }
        else if(nodeName.equals(JSP_DECLARATION)) {
            this.stream.skipUntil('>');
            JspDeclaration node = new JspDeclaration();
            node.setOffset(list.size());
            node.setAttributes(attributes.getAttributes());
            node.setClosed(NodeType.PAIR_CLOSED);
            node.setLine(line);
            node.setTextContent(this.readNodeContent(nodeName));
            this.pushJspNode(stack, list, node);
        }
        else if(nodeName.equals(JSP_SCRIPTLET)) {
            this.stream.skipUntil('>');
            JspScriptlet node = new JspScriptlet();
            node.setOffset(list.size());
            node.setAttributes(attributes.getAttributes());
            node.setClosed(NodeType.PAIR_CLOSED);
            node.setLine(line);
            node.setTextContent(this.readNodeContent(nodeName));
            this.pushJspNode(stack, list, node);
        }
        else if(nodeName.equals(JSP_EXPRESSION)) {
            this.stream.skipUntil('>');
            JspExpression node = new JspExpression();
            node.setOffset(list.size());
            node.setAttributes(attributes.getAttributes());
            node.setClosed(NodeType.PAIR_CLOSED);
            node.setLine(line);
            node.setTextContent(this.readNodeContent(nodeName));
            this.pushJspNode(stack, list, node);
        }
    }

    /**
     * @param source
     * @param stack
     * @param list
     * @param nodeName
     * @throws Exception
     */
    private void parseJspTag(Stack<Node> stack, List<Node> list, String nodeName) throws Exception {

            int line = this.stream.getLine();
            this.pushTextNode(stack, list, "<" + nodeName, line);

    }

    /**
     * @param source
     * @param stack
     * @param list
     * @throws Exception
     */
    private void parseJsp(Stack<Node> stack, List<Node> list) throws Exception {
        int i = this.stream.peek();

        if(i == '@') {
            this.stream.read();

            if(list.size() > 0) {
                this.clip(list.get(list.size() - 1), 1);
            }

            String nodeName = null;
            int line = this.stream.getLine();
            AttributeList attributes = this.getAttributeList();

            if(this.stream.match("%>")) {
                this.stream.skip(2);
            }
            else {
                throw new Exception("at line #" + line + " The 'jsp:directive' direction must be ends with '%>'");
            }

            if(attributes.getValue("page") != null) {
                nodeName = NodeType.JSP_DIRECTIVE_PAGE_NAME;
                attributes.remove("page");
            }
            else if(attributes.getValue("taglib") != null) {
                nodeName = NodeType.JSP_DIRECTIVE_TAGLIB_NAME;
            }
            else if(attributes.getValue("include") != null) {
                nodeName = NodeType.JSP_DIRECTIVE_INCLUDE_NAME;
                String path = attributes.getText("file");

            }
            else {
                throw new Exception("Unknown jsp directive at line #" + line + " - <%@ " + NodeUtil.toString(attributes.getAttributes()) + "%>");
            }

            JspDirective node = JspDirective.getInstance(nodeName);
            node.setOffset(list.size());
            node.setLine(line);
            node.setAttributes(attributes.getAttributes());
            node.setClosed(NodeType.SELF_CLOSED);
            this.pushJspNode(stack, list, node);
        }
        else if(i == '!'){
            this.stream.read();
            int line = this.stream.getLine();
            String scriptlet = this.readJspScriptlet();

            if(list.size() > 0) {
                this.clip(list.get(list.size() - 1), 1);
            }

            JspDeclaration node = new JspDeclaration();
            node.setOffset(list.size());
            node.setLine(line);
            node.setClosed(NodeType.PAIR_CLOSED);
            node.setTextContent(scriptlet);
            this.pushJspNode(stack, list, node);
        }
        else if(i == '-'){
            i = this.stream.read();
            i = this.stream.read();
            int line = this.stream.getLine();

            if(i == '-') {
                this.readJspComment();
                this.stream.skipLine();
            }
            else {
                throw new Exception("bad jsp syntax at line #" + line + ": <%-");
            }
        }
        else if(i == '=') {
            this.stream.read();
            int line = this.stream.getLine();
            String expression = this.readJspScriptlet();

            if(!this.isEmpty(expression)) {
                JspExpression node = new JspExpression();
                node.setOffset(list.size());
                node.setLine(line);
                node.setClosed(NodeType.PAIR_CLOSED);
                node.setTextContent(expression);
                this.pushJspNode(stack, list, node);
            }
            else {
                throw new Exception("at line #" + line + " Invalid jsp expression !");
            }
        }
        else {
            int line = this.stream.getLine();
            String scriptlet = this.readJspScriptlet();

            if(list.size() > 0) {
                this.clip(list.get(list.size() - 1), 1);
            }

            JspScriptlet node = new JspScriptlet();
            node.setOffset(list.size());
            node.setLine(line);
            node.setTextContent(scriptlet);
            node.setClosed(NodeType.PAIR_CLOSED);
            this.pushJspNode(stack, list, node);
        }
    }

    /**
     * @param stack
     * @param list
     * @param node
     */
    private void pushNode(Stack<Node> stack, List<Node> list, Node node) {
        Node parent = stack.peek();

        if(parent != null) {
            node.setParent(parent);
        }
        stack.push(node);
        list.add(node);
    }

    /**
     * @param stack
     * @param list
     * @param node
     */
    private void pushJspNode(Stack<Node> stack, List<Node> list, Node node) {
        Node parent = stack.peek();

        if(parent != null) {
            node.setParent(parent);
        }
        list.add(node);
    }

    /**
     * @param stack
     * @param list
     * @param text
     * @param lineNumber
     */
    private void pushTextNode(Stack<Node> stack, List<Node> list, String text, int lineNumber) {
        TextNode textNode = null;
        Node parent = stack.peek();
        int size = list.size();

        if(size > 0) {
            Node node = list.get(size - 1);

            if(node instanceof TextNode) {
                StringBuilder buffer = new StringBuilder();

                if(node.getTextContent() != null) {
                    buffer.append(node.getTextContent());
                }
                buffer.append(text);

                textNode = (TextNode)node;
                textNode.setParent(parent);
                textNode.setTextContent(buffer.toString());
            }
            else {
                textNode = new TextNode();
                textNode.setParent(parent);
                textNode.setLine(lineNumber);
                textNode.setOffset(size);
                textNode.setLength(1);
                textNode.setTextContent(text);
                list.add(textNode);
            }
        }
        else {
            textNode = new TextNode();
            textNode.setParent(parent);
            textNode.setLine(lineNumber);
            textNode.setOffset(size);
            textNode.setLength(1);
            textNode.setTextContent(text);
            list.add(textNode);
        }
    }

    /**
     * @param stack
     * @param list
     * @param nodeName
     */
    private void popNode(Stack<Node> stack, List<Node> list, String nodeName) throws Exception {
        Node node = stack.peek();

        if(node == null) {
            int line = this.stream.getLine();
            throw new Exception("Exception at line #" + line + ": </" + nodeName + "> not match !");
        }

        if(node.getNodeName().equalsIgnoreCase(nodeName)) {
            stack.pop();
            node.setLength(list.size() - node.getOffset() + 1);
            list.add(node);
        }
        else {
            throw new Exception("Exception at line #" + node.getLine() + " " + NodeUtil.getDescription(node) + " not match !");
        }
    }



    /**
     * @param work
     * @param file
     * @return String
     */
    private String getAbsolutePath(String work, String file) {
        String path = Path.getStrictPath(file);

        if(path.startsWith("/")) {
            return path;
        }
        else {
            String parent = Path.getParent(work);
            return Path.join(parent, path);
        }
    }





    /**
     * type == 1 prefix clip
     * type == 2 suffix clip
     * @param node
     * @param type
     */
    protected void clip(Node node, int type) {
        if(node.getNodeType() != NodeType.TEXT) {
            return;
        }

        char c;
        int j = 0;
        String content = node.getTextContent();

        if(type == 1) {
            /**
             * 删除该文本节点的后缀空格
             * 也就是删除下�?��标签节点的前导空�?             * 只删除空格不删除其他不可见字�?             */
            for(j = content.length() - 1; j > -1; j--) {
                c = content.charAt(j);

                if(c == ' ' || c == '\t') {
                    continue;
                }
                else {
                    break;
                }
            }
            content = content.substring(0, j + 1);
        }
        else {
            /**
             * 删除该文本节点的前导回车
             * 也就是删除前�?��标签节点的后�?���?             * 只删除回车不删除其他不可见字�?             */
            int length = content.length();

            for(j = 0; j < length; j++) {
                c = content.charAt(j);

                if(c == '\r') {
                    continue;
                }
                else if(c == '\n') {
                    j++;
                    break;
                }
                else {
                    break;
                }
            }

            if(j <= length) {
                content = content.substring(j, length);
            }
        }
        ((TextNode)node).setTextContent(content);
    }

    /**
     * @param nodeName
     * @return boolean
     */
    private boolean isTplDirective(String nodeName) {
        if(nodeName.startsWith("t:")) {
            return (nodeName.equals(TPL_DIRECTIVE_TAGLIB)
                    || nodeName.equals(TPL_DIRECTIVE_IMPORT)
                    || nodeName.equals(TPL_DIRECTIVE_RENAME)
                    || nodeName.equals(TPL_DIRECTIVE_INCLUDE)
                    || nodeName.equals(TPL_DIRECTIVE_TEXT)
                    || nodeName.equals(TPL_DIRECTIVE_COMMENT));
        }
        else {
            return false;
        }
    }

    /**
     * @param nodeName
     * @return boolean
     */
    private boolean isJspDirective(String nodeName) {
        return (nodeName.equals(JSP_DIRECTIVE_PAGE)
                || nodeName.equals(JSP_DIRECTIVE_TAGLIB)
                || nodeName.equals(JSP_DIRECTIVE_INCLUDE)
                || nodeName.equals(JSP_DECLARATION)
                || nodeName.equals(JSP_SCRIPTLET)
                || nodeName.equals(JSP_EXPRESSION));
    }



    /**
     * @param inputStream
     * @param charset
     * @return String
     * @throws IOException
     */
    protected String getContent2(InputStream inputStream, String charset) throws IOException {
        int length = 0;
        byte[] buffer = new byte[8192];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            while((length = inputStream.read(buffer, 0, 8192)) >0) {
                bos.write(buffer, 0, length);
            }
            return new String(bos.toByteArray(), charset);
        }
        finally {
            try {
                inputStream.close();
            }
            catch(Exception e) {
            }
        }
    }





    /**
     * @return TagLibrary
     */


    /**
     * @param tagLibrary
     */


    /**
     * @param ignoreJspTag the ignoreJspTag to set
     */
    public void setIgnoreJspTag(boolean ignoreJspTag) {
        this.ignoreJspTag = ignoreJspTag;
    }

    /**
     * @return the ignoreJspTag
     */
    public boolean getIgnoreJspTag() {
        return this.ignoreJspTag;
    }



}
