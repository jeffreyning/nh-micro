package groovy.bizflow.creditaudit


import java.util.Map;
import org.apache.log4j.Logger;
import com.nh.micro.rule.engine.core.*;
import groovy.template.flow.MicroFlowEngine;

class MicroManager extends MicroFlowEngine{
	private static Logger logger=Logger.getLogger(MicroManager.class);


	public String getFlowName(){
		return "bizflow_creditaudit";
	}
	public String getFlowTableName(){
		return "bizflow_creditaudit_list";
	}
	public String firstApprovalNodeName=getFlowName()+"_first_approval";
	public String finalApprovalNodeName=getFlowName()+"_final_approval";

	public String calcuNextNode(String id,String nodeId,String approvalStatus,String approvalText,Map extParam){
		if(getFlowBeginNode().equals(nodeId)){
			if(extParam==null){
				return firstApprovalNodeName;
			}
			String applicationAmount=extParam.get("dbcol_ext_application_credit_amount");
			Integer amount=Integer.valueOf(applicationAmount);
			if(amount>1500){
				return finalApprovalNodeName;
			}
			
			return firstApprovalNodeName;
		}
		if(firstApprovalNodeName.equals(nodeId)){
			if("yes".equals(approvalStatus)){
				return finalApprovalNodeName;
			}else if("no".equals(approvalStatus) || "back".equals(approvalStatus)){
				return getFlowEndNode();
			}

		}
		if(finalApprovalNodeName.equals(nodeId)){
			if("yes".equals(approvalStatus) || "no".equals(approvalStatus)){
				return getFlowEndNode();
			}
			else if("back".equals(approvalStatus)){
				return finalApprovalNodeName;
			}
		}
		return null;
	}
	
}
