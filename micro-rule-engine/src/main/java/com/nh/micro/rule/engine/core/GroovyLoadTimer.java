package com.nh.micro.rule.engine.core;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nh.cache.base.NhCacheHolder;
import com.nh.cache.base.NhCacheObject;

/**
 * 
 * @author ninghao
 * 
 */
public class GroovyLoadTimer {
	protected static Log logger = LogFactory.getLog(GroovyLoadTimer.class);

	public static boolean loadGroovy4Cache(String key) {

		NhCacheObject nhCacheObject = NhCacheHolder.getCacheObject(key);
		if (nhCacheObject == null) {
			return false;
		}
		String cacheVersion = nhCacheObject.getCacheVersion();
		if (cacheVersion == null) {
			return false;
		}
		String cacheType = nhCacheObject.getCacheType();
		if (cacheType == null || !cacheType.equals("groovy")) {
			return false;
		}
		String content = nhCacheObject.getCacheData();
		if (content == null || content.equals("")) {
			return false;
		}
		try {
			logger.info("begin loadGroovy4Cache key=" + key);
			GroovyLoadUtil.loadGroovyStampCheck(key, content, cacheVersion);
			logger.info("finish success loadGroovy4Cache key=" + key);
			return true;
		} catch (Exception e) {
			logger.error("loadGroovy4Cache error", e);
			return false;
		}
	}

	public void doJob() {
		Map holderMap = NhCacheHolder.getCacheHolder();
		if (holderMap == null) {
			return;
		}
		Iterator it = holderMap.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			loadGroovy4Cache(key);
		}
	}
}
