package groovy;

import javax.servlet.http.HttpServletRequest;
import com.nh.micro.service.InjectGroovy;

import foo.service.TestService;

class TestController  {  
	@InjectGroovy(name="TestService")
	public TestService testService;
	

	public Map echo(String str,HttpServletRequest httpRequest) {
		System.out.println("this is controller proxy");
		testService.test("111");
		Map retMap=new HashMap();
		retMap.put("status", "0");
		return retMap;
	}

}
