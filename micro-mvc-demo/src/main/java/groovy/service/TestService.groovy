package groovy.service;



import javax.annotation.Resource;


import foo.dto.MicroTest5;
import foo.repository.*;
import groovy.json.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.nh.micro.dao.mapper.DefaultPageInfo;
import com.nh.micro.dao.mapper.InjectDao;
import com.nh.micro.rule.engine.core.plugin.MicroAop;
import com.nh.micro.rule.engine.core.plugin.MicroDefaultLogProxy;
import com.nh.micro.template.MicroDbProxy;
import com.nh.micro.template.MicroTMProxy;

import groovy.template.MicroControllerTemplate;
import groovy.template.MicroServiceBizTemplate;


/**
 * 
 * @author ninghao
 *
 */
@MicroAop(name=[MicroDefaultLogProxy.class,MicroTMProxy.class,MicroDbProxy.class], property=["","",""])
class TestService extends MicroServiceBizTemplate  {  
	
	@InjectDao(name="TestDao")
	public TestDao testDao;
 

	public void test(String id){
		
		Map paramMap=new HashMap();
		paramMap.put("id", id);
		MicroTest5 microTest5=testDao.queryInfoById(paramMap);

		List<MicroTest5> list=testDao.getInfoListAllMapper(microTest5, ""); 
		
		DefaultPageInfo pageInfo=new DefaultPageInfo();
		pageInfo.setPageNo(1);
		List<MicroTest5> retList=testDao.queryInfosByPage(paramMap, pageInfo);
		Long total=pageInfo.getTotal();
		System.out.println("total="+total);
		
		
	}

}
