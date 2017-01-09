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
		if(groovySubName.equals("updateDept")){
			updateDept(gInputParam,gOutputParam,gContextParam);
		}
		if(groovySubName.equals("createDept")){
			createDept(gInputParam,gOutputParam,gContextParam);
		}
		if(groovySubName.equals("delDept")){
			delDept(gInputParam,gOutputParam,gContextParam);
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
	public void updateDept(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		JdbcTemplate jdbcTemplate=gContextParam.getContextMap().get("jdbcTemplate");
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String dept_id=httpRequest.getParameter("dept_id");
		String dept_name=httpRequest.getParameter("dept_name");
		String dept_type=httpRequest.getParameter("dept_type");
		String dept_remark=httpRequest.getParameter("dept_remark");
		String sql="update nh_micro_dept set dept_name=?,dept_type=?,dept_remark=? where dept_id=?";
		Integer retStatus=jdbcTemplate.update(sql,new PreparedStatementSetter(){
			public void setValues(PreparedStatement ps) throws Exception {
           ps.setString(1,dept_name);
           ps.setString(2,dept_type);
		   ps.setString(3,dept_remark);
		   ps.setString(4,dept_id);
       	}
       });
		gOutputParam.setResultObj(retStatus);
		return;
	}
	public void createDept(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		JdbcTemplate jdbcTemplate=gContextParam.getContextMap().get("jdbcTemplate");
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String dept_id=httpRequest.getParameter("dept_id");
		String dept_name=httpRequest.getParameter("dept_name");
		String dept_type=httpRequest.getParameter("dept_type");
		String dept_remark=httpRequest.getParameter("dept_remark");
		String parent_id=httpRequest.getParameter("parent_id");
		String sql="insert into nh_micro_dept(dept_id,dept_name,parent_id,dept_type,dept_remark) values(?,?,?,?,?)";
		Integer retStatus=jdbcTemplate.update(sql,new PreparedStatementSetter(){
			public void setValues(PreparedStatement ps) throws Exception {
		   ps.setString(1,dept_id);
		   ps.setString(2,dept_name);
		   ps.setString(3,parent_id);
		   ps.setString(4,dept_type);
		   ps.setString(5,dept_remark);
		   }
	   });
		gOutputParam.setResultObj(retStatus);
		return;
	}
	public void delDept(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		JdbcTemplate jdbcTemplate=gContextParam.getContextMap().get("jdbcTemplate");
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		String dept_id=httpRequest.getParameter("dept_id");
		String sql="delete from nh_micro_dept where dept_id=?";
		Integer retStatus=jdbcTemplate.update(sql,new PreparedStatementSetter(){
			public void setValues(PreparedStatement ps) throws Exception {
		   ps.setString(1,dept_id);
		   }
	    });
		gOutputParam.setResultObj(retStatus);
		return;
	}
}
