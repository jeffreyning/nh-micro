package com.nh.micro.datasource.delay;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

/**
 * 
 * @author ninghao
 *
 */
public class MicroExpireCache {
    private static final Logger LOG = Logger.getLogger(MicroExpireCache.class.getName());

    private ConcurrentMap cacheObjMap = new ConcurrentHashMap();

    private DelayQueue<MicroDelayItem> q = new DelayQueue();

    private Thread daemonThread;

    private MicroDelayHandler microDelayHandler=null;
    public MicroDelayHandler getMicroDelayHandler() {
		return microDelayHandler;
	}

	public void setMicroDelayHandler(MicroDelayHandler microDelayHandler) {
		this.microDelayHandler = microDelayHandler;
	}

	public MicroExpireCache() {


    }

	public void init(){
        Runnable daemonTask = new Runnable() {
            public void run() {
                daemonCheck();
            }
        };

        daemonThread = new Thread(daemonTask);
        daemonThread.setDaemon(true);
        daemonThread.setName("Cache Daemon");
        daemonThread.start();
	}
	
    private void daemonCheck() {

        LOG.info("cache service started.");

        for (;;) {
            try {
                MicroDelayItem delayItem = q.take();
                if (delayItem != null) {
                    // 超时对象处理
                    String item = delayItem.getItem();
                    Object temp=cacheObjMap.get(item);
                    cacheObjMap.remove(item);
                    if(microDelayHandler!=null){
                    	microDelayHandler.doDelayRemove(item, temp);
                    }
                }
            } catch (InterruptedException e) {
                LOG.error(e.getMessage(), e);
                break;
            }
        }

            LOG.info("cache service stopped.");
    }

    // 添加缓存对象
    public void put(String key, Object value, long time, TimeUnit unit) {
        Object oldValue = cacheObjMap.put(key, value);
        if (oldValue != null){
        	MicroDelayItem temp=new MicroDelayItem(key,0);
            q.remove(temp);
        }

        long nanoTime = TimeUnit.NANOSECONDS.convert(time, unit);
        q.put(new MicroDelayItem(key, nanoTime));
    }

    public Object get(String key) {
        return cacheObjMap.get(key);
    }
    
    public void remove(String key) {
    	MicroDelayItem item=new MicroDelayItem(key,0);
    	q.remove(item);
        cacheObjMap.remove(key);
    }    
}
