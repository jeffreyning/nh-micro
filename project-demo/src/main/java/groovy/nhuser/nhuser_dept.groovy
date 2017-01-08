package groovy.nhuser

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Map
import java.lang.String

import com.nh.cache.base.*
import java.util.Set
import net.sf.json.JSONObject
import com.nh.micro.rule.engine.core.GroovyExecUtil;

import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;
import com.nh.micro.rule.engine.core.GContextParam;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.jdbc.core.JdbcTemplate;

class nrule{
	public void execGroovy(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		Map inMap=gInputParam.getParamData();
		Map outMap=gOutputParam.getResultObj();
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String groovySubName=httpRequest.getParameter("groovySubName");
		System.out.println("dept");
		if(groovySubName.equals("getDept")){
			getDept(gInputParam,gOutputParam,gContextParam);
		}
		return ;
	}
	public void getDept(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		JdbcTemplate jdbcTemplate=gContextParam.getContextMap().get("jdbcTemplate");
		String sql="select * from nh_micro_dept";
		List deptList=jdbcTemplate.queryForList(sql);
		gOutputParam.setResultObj(deptList);
		return;
	}

}
