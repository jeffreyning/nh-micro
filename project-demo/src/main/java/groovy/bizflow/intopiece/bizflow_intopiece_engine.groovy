package groovy.bizflow.intopiece

import java.util.Map;
import org.apache.log4j.Logger;

import groovy.template.flow.MicroFlowEngine;



class MicroManager extends MicroFlowEngine{
	private static Logger logger=Logger.getLogger(MicroManager.class);
	public String getFlowName(){
		return "bizflow_intopiece";
	}
	public String getFlowTableName(){
		return "bizflow_intopiece_list";
	}
	
	public String qaApprovalNodeName=getFlowName()+"_qa_approval";
	

	public String calcuNextNode(String id,String nodeId,String approvalStatus,String approvalText,Map extParam){
		if(getFlowBeginNode().equals(nodeId)){
			return qaApprovalNodeName;
		}
		if(qaApprovalNodeName.equals(nodeId)){
			return getFlowEndNode();
		}
		return null;
	}

}
