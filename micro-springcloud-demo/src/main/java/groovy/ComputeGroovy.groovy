package groovy



import javax.annotation.Resource;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.log4j.Logger;


class ComputeGroovy {
private final Logger logger = Logger.getLogger(getClass());

//@Resource(name="discoveryClient")
//public DiscoveryClient client;

    public Integer add(@RequestParam Integer a, @RequestParam Integer b) {
        //GroovyExecUtil.execGroovyRetObj("TestGroovy", "test");

        Integer r = a + b;
		System.out.println(r);
		//ServiceInstance instance = client.getLocalServiceInstance();
        //logger.info("/add, host:" + instance.getHost() + ", service_id:" + instance.getServiceId() + ", result:" + r);
        return r;
    }
}
