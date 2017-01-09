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
import org.springframework.jdbc.core.PreparedStatementSetter;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import groovy.json.*;

class nrule{
	public void execGroovy(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		Map inMap=gInputParam.getParamData();
		Map outMap=gOutputParam.getResultObj();
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String groovySubName=httpRequest.getParameter("groovySubName");
		System.out.println("user");
		if(groovySubName.equals("getUserListAll")){
			getUserListAll(gInputParam,gOutputParam,gContextParam);
		}
		if(groovySubName.equals("getUserList4Page")){
			getUserList4Page(gInputParam,gOutputParam,gContextParam);
		}
		if(groovySubName.equals("createUser")){
			createUser(gInputParam,gOutputParam,gContextParam);
		}

		return ;
	}
	public void getUserListAll(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		JdbcTemplate jdbcTemplate=gContextParam.getContextMap().get("jdbcTemplate");
		String sql="select * from nh_micro_user";
		List userList=jdbcTemplate.queryForList(sql);
		//gOutputParam.setResultObj(deptList);
		Map retMap=new HashMap();
		retMap.put("rows", userList);
		retMap.put("total", userList.size());
		JsonBuilder jsonBuilder=new JsonBuilder(retMap);
		String retStr=jsonBuilder.toString();
		System.out.println(retStr);
		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
	
	public void getUserList4Page(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String page=httpRequest.getParameter("page");
		String rows=httpRequest.getParameter("rows");
		String sort=httpRequest.getParameter("sort");
		String order=httpRequest.getParameter("order");
		Integer pageNum=Integer.valueOf(page);
		Integer rowsNum=Integer.valueOf(rows);
		Integer startNum=(pageNum-1)*rowsNum;
		Integer endNum=(pageNum)*rowsNum;
		JdbcTemplate jdbcTemplate=gContextParam.getContextMap().get("jdbcTemplate");
		String selectCount="select count(*) from nh_micro_user";
		Integer total=jdbcTemplate.queryForInt(selectCount);
		
		String select="select * from nh_micro_user";
		String limit="limit "+startNum+","+endNum;
		String orderSql="order by user_id asc";
		if(sort!=null && !sort.equals("")){
			orderSql=orderSql+","+sort+" "+order;
		}
		String sql=select+" "+orderSql+" "+limit;
		System.out.println(sql);
		List userList=jdbcTemplate.queryForList(sql);
		//gOutputParam.setResultObj(deptList);
		Map retMap=new HashMap();
		retMap.put("rows", userList);
		retMap.put("total", total);
		JsonBuilder jsonBuilder=new JsonBuilder(retMap);
		String retStr=jsonBuilder.toString();
		System.out.println(retStr);
		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
	

	public void createUser(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		JdbcTemplate jdbcTemplate=gContextParam.getContextMap().get("jdbcTemplate");
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String user_id=httpRequest.getParameter("user_id");
		String user_name=httpRequest.getParameter("user_name");
		String user_type=httpRequest.getParameter("user_type");
		String user_remark=httpRequest.getParameter("user_remark");
		String sql="insert into nh_micro_user(user_id,user_name,user_type,user_remark) values(?,?,?,?)";
		Integer retStatus=jdbcTemplate.update(sql,new PreparedStatementSetter(){
			public void setValues(PreparedStatement ps) throws Exception {
		   ps.setString(1,user_id);
		   ps.setString(2,user_name);
		   ps.setString(3,user_type);
		   ps.setString(4,user_remark);
		   }
	   });
		gOutputParam.setResultObj(retStatus);
		return;
	}

}
