package com.nh.micro.rule.engine.core;

import org.apache.log4j.Logger;

/**
 * 
 * @author ninghao
 *
 */
public class GroovyLoadInnerTimer {
	private static Logger logger=Logger.getLogger(GroovyLoadInnerTimer.class);
	public static void daemonCheck(){
        logger.debug("groovyload daemon started.");

        for (;;) {
            try {
            	Thread.sleep(1000*3);
            	GroovyInitUtil.initGroovy();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                break;
            }
        }

        logger.debug("groovyload daemon stopped.");
	}
	
	public static void initThread(){
        Runnable daemonTask = new Runnable() {
            public void run() {
                daemonCheck();
            }
        };
        Thread daemonThread=null;
        daemonThread = new Thread(daemonTask);
        daemonThread.setDaemon(true);
        daemonThread.setName("LoadGroovyDaemon");
        daemonThread.start();
	}
}
