package com.project.frame.handler;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import net.sf.json.JSONObject;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.nh.micro.rule.engine.core.GContextParam;
import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;
import com.nh.micro.rule.engine.core.GroovyExecUtil;
import com.nh.esb.core.INhCmdConst;
import com.nh.esb.core.INhCmdHandler;
import com.nh.esb.core.NhCmdRequest;
import com.nh.esb.core.NhCmdResult;
//import com.nh.esb.core.PassUtil;
import com.nh.esb.service.servlet.NhEsbServiceServlet;
import com.nh.esb.service.servlet.NhServletCmdContextHolder;
import com.project.util.xa.MyXid;
import com.project.util.xa.XADataSourceHolder;
import com.project.util.xa.XidHolder;

public class WsXaTranCmdHandler implements INhCmdHandler  {
	private static Boolean checkPassFlag=false;//检查用户密码开关
	private static Map passMap=new HashMap();
	public static Map getPassMap() {
		return passMap;
	}
	public static void setPassMap(Map passMap) {
		WsXaTranCmdHandler.passMap = passMap;
	}
	public static Boolean getCheckPassFlag() {
		return checkPassFlag;
	}
	public  void setCheckPassFlag(Boolean checkPassFlag) {
		WsXaTranCmdHandler.checkPassFlag = checkPassFlag;
	}	
	@Override
	public void execHandler(NhCmdRequest request, NhCmdResult result) {

		if(checkPassFlag==true){
			String user=request.getUser();
			String passWord=request.getPassWord();
			String checkWord=(String) passMap.get(user);
			String md5="";
			try {
				//md5=PassUtil.createMd5(request,checkWord);
			} catch (Exception e) {
				result.setResultStatus(INhCmdConst.STATUS_ERROR);
				result.setResultCode("password_parse_error");
				return ;	
			}
			if(!passWord.equals(md5)){
				result.setResultStatus(INhCmdConst.STATUS_ERROR);
				result.setResultCode("password_error");
				return ;	
			}
		}		
		
		
		String subName=request.getSubName();
		if(subName==null || "".equals(subName)){
			result.setErrMsg("subname is null");
			result.setResultStatus(1);
			result.setResultCode("0100");			
			return;
		}
		if("execXaTranSql".equals(subName)){
			execXaTranSql(request,result);
		}else if("commitXaTran".equals(subName)){
			commitXaTran(request,result);
		}else if("rollbackXaTran".equals(subName)){
			rollbackXaTran(request,result);
		}else{
			result.setErrMsg("subname is not correct");
			result.setResultStatus(1);
			result.setResultCode("0101");			
			return;			
		}
	}

	
	//执行sql
	public void execXaTranSql(NhCmdRequest request, NhCmdResult result){
		HttpServletRequest httpRequest=NhServletCmdContextHolder.getNhServletCmdContext().get().getHttpRequest();
		
		//事务id
		String uuid=request.getBizId();
		
		//执行sql
		String sql=httpRequest.getParameter("sql");
		
		//检查是否为空
		if((uuid==null || "".equals(uuid))||(sql==null || "".equals(sql))){
			result.setErrMsg("bizid or remark sql is null");
			result.setResultStatus(1);
			result.setResultCode("0101");			
			return;				
		}
		
		//创建xid实例
		String branchId=UUID.randomUUID().toString();
		MyXid myXid=new MyXid(10,uuid.getBytes(),branchId.getBytes());
		
		//获取datasourceid
		String dataSourceId=httpRequest.getParameter("dataSourceId");
		AtomikosDataSourceBean atomDataSource=(AtomikosDataSourceBean) XADataSourceHolder.holderMap.get(dataSourceId);
		
		//获取自动提交标识
		String lastFlag=httpRequest.getParameter("lastFlag");
		
		XAConnection xaConnection=null;
		XAResource xaResource=null;
		Connection conn=null;
		
		//异常标识
		boolean errorFlag=false;
		try {
			XADataSource xaDataSource=atomDataSource.getXaDataSource();
			xaConnection=xaDataSource.getXAConnection();
			xaResource=xaConnection.getXAResource();
			xaResource.setTransactionTimeout(30);
			//保存resource
			XidHolder.appendXa(uuid, myXid,xaResource);			
			conn=xaConnection.getConnection();

			xaResource.start(myXid,XAResource.TMNOFLAGS);
			callSql(request, result, conn);
			xaResource.end(myXid, XAResource.TMSUCCESS);

		} catch (Exception e) {
			errorFlag=true;
			e.printStackTrace();
			try {
				xaResource.end(myXid, XAResource.TMFAIL);
			} catch (XAException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			List<Map> xaList=XidHolder.getXaList(uuid);
			if(xaList!=null){
				rollbackXaTranInner(xaList);
				XidHolder.removeXaList(uuid);
			}
			
			result.setErrMsg(e.toString());
			result.setResultStatus(1);
			result.setResultCode("0011");
		}finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}	
		
		if(lastFlag!=null && "1".equals(lastFlag) && errorFlag==false){
			List<Map> xaList=XidHolder.getXaList(uuid);
			commitXaTranInner(xaList);
			XidHolder.removeXaList(uuid);
		}
	}	
	
	public int callSql(NhCmdRequest request, NhCmdResult result,Connection conn) throws Exception{
		HttpServletRequest httpRequest=NhServletCmdContextHolder.getNhServletCmdContext().get().getHttpRequest();		
		String sql=httpRequest.getParameter("sql");
		String paramType=httpRequest.getParameter("paramType");
		String param=httpRequest.getParameter("param");
		
		List<String> paramList=new ArrayList();
		if(param!=null && !"".equals(param)){
			if(paramType==null || "".equals(paramType) || "split".equals(paramType)){
				String[] pa=param.split(",");
				paramList=Arrays.asList(pa);
			}else if(paramType.equals("json")){
				paramList=(List) JSONObject.toBean(JSONObject.fromObject(param),List.class);
			}
		}

		PreparedStatement state=conn.prepareStatement(sql);
		int size=paramList.size();
		for(int i=0;i<size;i++){
			String temp=paramList.get(i);
			state.setObject(i+1, temp);
		}
		int status=state.executeUpdate();
		return status;
		
	}
	
/*	//执行sql
	public void execXaTranGroovy(NhCmdRequest request, NhCmdResult result){
		String uuid=request.getBizId();
		String branchId=UUID.randomUUID().toString();
		MyXid myXid=new MyXid(10,uuid.getBytes(),branchId.getBytes());
		String callType=request.getCallType();
		AtomikosDataSourceBean atomDataSource=(AtomikosDataSourceBean) XADataSourceHolder.holderMap.get(callType);
		HttpServletRequest httpRequest=NhServletCmdContextHolder.getNhServletCmdContext().get().getHttpRequest();
		String lastFlag=httpRequest.getParameter("lastFlag");
		XAConnection xaConnection=null;
		XAResource xaResource=null;
		Connection conn=null;
		boolean errorFlag=false;
		try {
			XADataSource xaDataSource=atomDataSource.getXaDataSource();
			xaConnection=xaDataSource.getXAConnection();
			xaResource=xaConnection.getXAResource();
			XidHolder.appendXa(uuid, myXid,xaResource);			
			conn=xaConnection.getConnection();

			xaResource.start(myXid,XAResource.TMNOFLAGS);
			callGroovy(request,result,conn);
			xaResource.end(myXid, XAResource.TMSUCCESS);

		} catch (Exception e) {
			errorFlag=true;
			e.printStackTrace();
			try {
				xaResource.end(myXid, XAResource.TMFAIL);
			} catch (XAException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			List<Map> xaList=XidHolder.getXaList(uuid);
			if(xaList!=null){
				rollbackXaTranInner(xaList);
				XidHolder.removeXaList(uuid);
			}
			
			result.setErrMsg(e.toString());
			result.setResultStatus(1);
			result.setResultCode("0011");
		}finally{
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}	
		
		if(lastFlag!=null && "1".equals(lastFlag) && errorFlag==false){
			List<Map> xaList=XidHolder.getXaList(uuid);
			commitXaTranInner(xaList);
			XidHolder.removeXaList(uuid);
		}
	}
	
	//调用groovy
	public void callGroovy(NhCmdRequest request, NhCmdResult result,Connection conn){
		
		String inputData=request.getCmdData();
		Map inMap=(Map) JSONObject.toBean(JSONObject.fromObject(inputData),Map.class);
		if(inMap==null){
			inMap=new HashMap();
		}
		Map outMap=new HashMap();
		GInputParam gInputParam=new GInputParam(inMap);
		GOutputParam gOutputParam=new GOutputParam(outMap);
		GContextParam gContextParam=new GContextParam();
		gContextParam.getContextMap().put("httpRequest", NhServletCmdContextHolder.getNhServletCmdContext().get().getHttpRequest());
		gContextParam.getContextMap().put("httpResponse", NhServletCmdContextHolder.getNhServletCmdContext().get().getHttpResponse());
		gContextParam.getContextMap().put("httpSession", NhServletCmdContextHolder.getNhServletCmdContext().get().getHttpSession());
		gContextParam.getContextMap().put("jdbcConn", conn);
		
		String groovyName=NhServletCmdContextHolder.getNhServletCmdContext().get().getHttpRequest().getParameter("groovyName");
		String groovySubName=NhServletCmdContextHolder.getNhServletCmdContext().get().getHttpRequest().getParameter("groovySubName");
		if(groovySubName==null ||"".equals(groovySubName)){
			groovySubName="execGroovy";
		}else{
			//groovySubName="exec"+String.valueOf(groovySubName.charAt(0)).toUpperCase()+groovySubName.substring(1);
		}


		boolean status=GroovyExecUtil.execGroovy(groovyName, groovySubName,gInputParam, gOutputParam,gContextParam);
		if(status==false){
			result.setResultStatus(INhCmdConst.STATUS_ERROR);
			result.setErrMsg("exec_groovy_error");
			return;
		}
		
		String retStr=JSONObject.fromObject(gOutputParam).toString();
		result.setResultData(retStr);		
	}*/
	
	
	private int commitXaTranInner(List<Map> xaList){
		if(xaList==null){
			return 1;
		}
		boolean prepareStatus=true;
		for(Map xaInfo:xaList){
			XAResource xaResource=(XAResource) xaInfo.get("xaResource");
			MyXid myXid=(MyXid) xaInfo.get("myXid");
			int oneStatus=0;
			try {
				oneStatus = xaResource.prepare(myXid);
			} catch (XAException e) {
				e.printStackTrace();
				oneStatus=XAResource.TMFAIL;
			}
			if(oneStatus!=XAResource.XA_OK){
				prepareStatus=false;
				break;
			}
			
		}
		for(Map xaInfo:xaList){
			XAResource xaResource=(XAResource) xaInfo.get("xaResource");
			MyXid myXid=(MyXid) xaInfo.get("myXid");				
			if(prepareStatus==true){
				try {
					xaResource.commit(myXid, false);
				} catch (XAException e) {
					e.printStackTrace();
				}
			}else{
				try {
					xaResource.rollback(myXid);
				} catch (XAException e) {
					e.printStackTrace();
				}
			}
		}	
		if(prepareStatus==false){
			return 2;
		}
		return 0;
	}
	
	//commit分布式事务
	public void commitXaTran(NhCmdRequest request, NhCmdResult result) {
		String uuid=request.getBizId();
		List<Map> xaList=XidHolder.getXaList(uuid);
		int status=commitXaTranInner(xaList);
		if(status!=0){
			result.setErrMsg("xa commit fail");
			result.setResultStatus(1);
			result.setResultCode("0011");
		}
		XidHolder.removeXaList(uuid);
	}	
	
	private int rollbackXaTranInner(List<Map> xaList){
		boolean flag=true;
		for(Map xaInfo:xaList){
			XAResource xaResource=(XAResource) xaInfo.get("xaResource");
			MyXid myXid=(MyXid) xaInfo.get("myXid");
			try {
				xaResource.rollback(myXid);
			} catch (XAException e) {
				flag=false;
				e.printStackTrace();
			}
		}
		if(flag==false){
			return 1;
		}
		return 0;		
	}
	
	//rollback分布式事务
	public void rollbackXaTran(NhCmdRequest request, NhCmdResult result) {
		String uuid=request.getBizId();
		List<Map> xaList=XidHolder.getXaList(uuid);
		int status=rollbackXaTranInner(xaList);
		if(status!=0){
			result.setErrMsg("rollback xa fail");
			result.setResultStatus(1);
		}
		XidHolder.removeXaList(uuid);
	}

}
