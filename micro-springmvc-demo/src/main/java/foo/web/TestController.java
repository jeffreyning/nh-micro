package foo.web;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nh.micro.service.InjectGroovy;


@Controller
@RequestMapping("test")
@InjectGroovy(name="TestController")
public interface TestController {

	
@RequestMapping("echo")
@ResponseBody
public  Map echo(@RequestParam(value="str") String str,HttpServletRequest httpRequest);


}
