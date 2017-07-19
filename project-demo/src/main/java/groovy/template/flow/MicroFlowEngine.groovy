package groovy.template.flow


import java.util.Map;
import org.apache.log4j.Logger;

import com.nh.micro.rule.engine.core.*;

import groovy.json.*;
import groovy.template.MicroMvcTemplate;

import groovy.template.MicroServiceTemplate;

/**
 * 
 * @author ninghao
 *
 */
class MicroFlowEngine extends MicroServiceTemplate{
	private static Logger logger=Logger.getLogger(MicroFlowEngine.class);

	public String flowName="bizflow";
	public String flowTableName="bizflow_list";
	public String flowCurnode="dbcol_ext_"+getFlowName()+"_flow_curnode";
	public String flowStatus="dbcol_ext_"+getFlowName()+"_flow_status";
	public String flowFinish="dbcol_ext_"+getFlowName()+"_flow_finish";
	public String flowEndNode=getFlowName()+"_end";
	public String flowBeginNode=getFlowName()+"_begin";
	
	public String getFlowName(){
		return this.flowName;
	}

	public String getFlowTableName(){
		return this.flowTableName;
	}
	
	public String getFlowCurnode(){
		return this.flowCurnode;
	}
	
	public String getFlowStatus(){
		return this.flowStatus;
	}
	
	public String getFlowFinish(){
		return this.flowFinish;
	}
	
	public String getFlowEndNode(){
		return this.flowEndNode;
	}
	
	public String getFlowBeginNode(){
		return this.flowBeginNode;
	}
	
	private void setCurNode(String id,String nodeId){
		Map paramMap=new HashMap();
		paramMap.put(getFlowCurnode(), nodeId);
		updateInfoByIdService(id,getFlowTableName(),paramMap);
	}
	
	private void setApprovalStatus(String id,String nodeId,String approvalStatus,String approvalText){
		Map paramMap=new HashMap();
		String statusKey="dbcol_ext_"+nodeId+"_status";
		String textKey="dbcol_ext_"+nodeId+"_text";
		paramMap.put(statusKey, approvalStatus);
		paramMap.put(textKey, approvalText);
		updateInfoByIdService(id,getFlowTableName(),paramMap);
	}
	
	private void initStart(String id,String nodeId,String approvalStatus,Map extParam){
		Map paramMap=new HashMap();
		paramMap.put(getFlowCurnode(), "");
		paramMap.put(getFlowStatus(), "");
		paramMap.put(getFlowFinish(), "start");
		updateInfoByIdService(id,getFlowTableName(),paramMap);	
	}
	
	private void initEnd(String id,String nodeId,String approvalStatus,Map extParam){
		Map paramMap=new HashMap();
		paramMap.put(getFlowCurnode(), "");
		paramMap.put(getFlowStatus(), approvalStatus);
		paramMap.put(getFlowFinish(), "finish");
		updateInfoByIdService(id,getFlowTableName(),paramMap);
	}
	
	public String execStart(String id,Map extParam){
		initStart(id,"","",extParam);
		String nodeName=execFlow(id,getFlowBeginNode(),"start","",extParam);
		return nodeName;
	}

	
	public String execFlow(String id,String nodeId,String approvalStatus,String approvalText,Map extParam){
		setApprovalStatus(id,nodeId,approvalStatus,approvalText);
		String nextNode="";
		nextNode=calcuNextNode(id,nodeId,approvalStatus,approvalText,extParam);
		if(nextNode!=null && !"".equals(nextNode)){
			setCurNode(id,nextNode);
			GroovyExecUtil.execGroovyRetObj(nextNode, "execInit",id,nodeId,approvalStatus,extParam);
			if(getFlowEndNode().equals(nextNode)){
				initEnd(id,nodeId,approvalStatus,extParam);
			}
		}
		return nextNode;
	}
	
	public String calcuNextNode(String id,String nodeId,String approvalStatus,String approvalText,Map extParam){
		return "";
	}
	
	
}
