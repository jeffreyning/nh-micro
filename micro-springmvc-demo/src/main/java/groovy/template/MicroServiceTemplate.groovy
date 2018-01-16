package groovy.template;  

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.slf4j.Logger
import org.slf4j.LoggerFactory


import com.nh.micro.rule.engine.context.MicroContextHolder;
import com.nh.micro.rule.engine.core.GroovyExecUtil;
import com.nh.micro.template.MicroServiceTemplateSupport


import groovy.template.MicroServiceBizTemplate;

/**
 * 
 * @author ninghao
 *
 */
class MicroServiceTemplate extends MicroServiceTemplateSupport{

	public Integer filterView(String tableName,Map paramMap,String bizId,String bizCol,String type){

		String checkKey="checkview_"+tableName;
		Object obj=GroovyExecUtil.getGroovyObj(checkKey);
		if(obj==null){
			return 0;
		}
		Integer retInt=GroovyExecUtil.execGroovyRetObj(checkKey, "checkView", tableName, paramMap, bizId, bizCol, type);
		return retInt;
		
	}
}
