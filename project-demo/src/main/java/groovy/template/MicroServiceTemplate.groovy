package groovy.template;  



import com.nh.micro.template.*;


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
