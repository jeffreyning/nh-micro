package com.nh.micro.template;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class MicroServiceTemplateUtil {
	public static String sqlTemplateService(String template,Map paramMap,List placeList){
		VelocityEngine ve = new VelocityEngine();
		ve.addProperty("userdirective", "com.minxin.micro.template.vext.MicroSqlReplace");
		ve.init();
		VelocityContext context = new VelocityContext();
		context.put("param", paramMap);
		context.put("placeList", placeList);
		StringWriter writer = new StringWriter();
		ve.evaluate(context, writer, "", template);
		return writer.toString();
	}
	public static String sqlTemplateService(String template,Map paramMap){
		return sqlTemplateService(template,paramMap,new ArrayList());
	}
}
