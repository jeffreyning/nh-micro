package com.nh.micro.nhs;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.nh.micro.nhs.JspParser;
import com.nh.micro.nhs.util.Node;
import com.nh.micro.nhs.util.NodeType;

/**
 * 
 * @author ninghao
 *
 */

public class NhsParseHandler implements ILoadHandler {

	public static StringBuilder getCurSb(String curId, Map retMap){
		StringBuilder sb=(StringBuilder) retMap.get(curId);
		if(sb==null){
			sb=new StringBuilder();
			retMap.put(curId, sb);
		}
		return sb;
	}
	public static Map toMidGroovy(List<Node> list) throws Exception{
		Map retMap=new HashMap();

		String curId=null;
		List<Node> nodeList=list;
		for(Node node:nodeList){
			if(node.getNodeType()==NodeType.JSP_DECLARATION){
				String temp=node.getTextContent();
				if(temp.contains("</sql>")){
					curId=null;
					continue;
				}else if(temp.contains("<sql")){
					int first0=temp.indexOf("=");
					int first1=temp.indexOf(">");
					String methodName=temp.substring(first0+1, first1);
					methodName=methodName.trim();
					methodName=methodName.replace("\"", "");
					curId=methodName;
					continue;
				}				
			}else if(node.getNodeType()==NodeType.JSP_SCRIPTLET){
				if(curId==null){
					continue;
				}
				StringBuilder sb=getCurSb(curId,retMap);
				sb.append(node.getTextContent()).append("\r\n");
			}else if(node.getNodeType()==NodeType.TEXT){
				
				String temp=node.getTextContent();

				if(curId==null){
					continue;
				}
				StringBuilder sb=getCurSb(curId,retMap);
				InputStream is = new ByteArrayInputStream(temp.getBytes("UTF-8"));
				Reader reader=new InputStreamReader(is);
				BufferedReader br=new BufferedReader(reader);
				String line=null;
				while((line=br.readLine()) != null){
					String tempLine=line.replace("\"", "\\\"");
					sb.append("sb.append(\"").append(tempLine).append("\");\r\n");
				}
			}else if(node.getNodeType()==NodeType.JSP_EXPRESSION){
				if(curId==null){
					continue;
				}		
				StringBuilder sb=getCurSb(curId,retMap);
				String value=node.getTextContent();
				sb.append("sb.append(").append(value).append(");\r\n");
			}else if(node.getNodeType()==NodeType.EXPRESSION || node.getNodeType()==NodeType.MIX_EXPRESSION){
				if(curId==null){
					continue;
				}
				StringBuilder sb=getCurSb(curId,retMap);
				
				String value=node.getTextContent();
				String realVal=value;
				boolean flag=false;
				if(value.startsWith("#")){
					realVal=value.substring(1);
					flag=true;
				}
				if(flag==false){
					sb.append("sb.append(").append(value).append(");\r\n");
				}else{
					sb.append("sb.append(\"?\");\r\n");
					sb.append("repList.add("+realVal+");\r\n");
				}
			}
		}
		return retMap;
		
	}
	
	public static String toGroovy(String groovyName, Map midMap){
		StringBuilder sb=new StringBuilder();
		sb.append("import com.nh.micro.nhs.CheckSqlUtil;\r\n");
		sb.append("public class "+groovyName+" {\r\n");
		Set<String> keySet=midMap.keySet();
		for(String key:keySet){
			String value=((StringBuilder) midMap.get(key)).toString();
			sb.append("  public String "+key+"(Object[] paramArray, List repList){\r\n");
			sb.append("  StringBuilder sb=new StringBuilder();\r\n");
			sb.append(value);
			sb.append("\r\n");
			sb.append("CheckSqlUtil.checkSql(sb);\r\n");
			sb.append("  return sb.toString();");
			sb.append("\r\n  }\r\n");
		}
		sb.append("\r\n}");

		return sb.toString();
		
	}

	@Override
	public boolean check(String name, String content) {
		return true;
	}
	
	@Override
	public String parse(String name, String content) throws Exception{
		
		JspParser jspParse=new JspParser();
		InputStream inputStream=new ByteArrayInputStream(content.getBytes("UTF-8"));
		List<Node> nodeList=jspParse.parse(inputStream, "UTF-8");
		
		Map retMap=NhsParseHandler.toMidGroovy(nodeList);
		String retStr=NhsParseHandler.toGroovy(name,retMap);
		return retStr;
	}

}
