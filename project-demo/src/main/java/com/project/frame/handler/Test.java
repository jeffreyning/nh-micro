package com.project.frame.handler;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.singularsys.jep.Jep;
import com.singularsys.jep.bigdecimal.BigDecComponents;

public class Test {
	public static void main(String[] args) throws Exception {
		  Jep jep = new Jep(new BigDecComponents(new MathContext(2,RoundingMode.HALF_UP)));
		  //Jep jep = new Jep(new BigDecComponents());
		  jep.addVariable("a", new BigDecimal("10"));
		  //jep.addVariable("b", 3);
		   String exp="10*a/3*3";
		   jep.parse(exp);
		   BigDecimal result = (BigDecimal) jep.evaluate();
		   System.out.println("计算结果： " + result);
		   System.out.println("计算结果： " + result.setScale(2,   BigDecimal.ROUND_HALF_UP));
		   System.out.println(0.123*0.12);
		   
		   Map temp=new HashMap();
		   JSONObject jsonObj=new JSONObject();
		   temp.putAll(jsonObj);
	}
}
