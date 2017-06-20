package com.nh.micro.template.vext;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

/**
 * 
 * @author ninghao
 *
 */
public class MicroSqlReplace extends Directive{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "sqlreplace";
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return LINE;
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node)
			throws IOException, ResourceNotFoundException, ParseErrorException,
			MethodInvocationException {
		List placeList=(List) context.get("placeList");
		if(placeList==null){
			return true;
		}
		Object value=node.jjtGetChild(0).value(context);
		placeList.add(value);
		writer.write("?");
		return true;
	}

}
