package com.nh.micro.rule.engine.core;

import java.util.Iterator;
import java.util.Map;


import org.apache.log4j.Logger;



/**
 * 
 * @author ninghao
 * 
 */
public class GroovyLoadTimer {
	//protected static Log logger = LogFactory.getLog(GroovyLoadTimer.class);
	private static Logger logger=Logger.getLogger(GroovyLoadUtil.class);
	public void doJob() throws Exception {
		GroovyInitUtil.initGroovy();
	}
}
