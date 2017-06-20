package com.nh.micro.template;

import com.nh.micro.rule.engine.core.GroovyExecUtil;

/**
 * 
 * @author ninghao
 *
 */
public class MicroTranAopImpl implements MicroTranAopInter {

	public Object execGroovyRetObjByDbTran(String groovyName,
			String methodName, Object... paramArray) {
		return GroovyExecUtil.execGroovyRetObj(groovyName, methodName, paramArray);
	}

}
