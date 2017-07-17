package groovy.product.algo

import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;
import com.nh.micro.rule.engine.core.GContextParam;


import com.singularsys.jep.Jep
import com.singularsys.jep.bigdecimal.BigDecComponents

import java.math.MathContext
import java.math.RoundingMode;

import java.text.SimpleDateFormat;
import com.nh.micro.rule.engine.core.GroovyExecUtil;
//import com.nh.micro.cache.base.NhCacheHolderFactory;
//import com.google.gson.Gson;
import groovy.json.*;
import groovy.product.algo.ProductAlgoConst;

class productAlgoRepayplan implements ProductAlgoConst {
	public void execGroovy(GInputParam gInputParam,GOutputParam gOutputParam) throws Exception {
		Map<String, Object> inMap = gInputParam.getParamData();
		Map<String, Object> outMap = gOutputParam.getResultObj();

		String productId=inMap.get(ProductAlgoConst.productId);
		//Object obj=NhCacheHolderFactory.getHolder("default").getCacheObject(productId);
		
		//Map<String, Object> cacheMap = new Gson().fromJson(obj.getCacheData(),Map.class);
		
		Map dataMap=GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "getInfoByBizIdService", productId,"nh_micro_product_center_list","meta_key");
		String sdata=dataMap.get("dbcol_ext_sdata");
		Map rootMap=new JsonSlurper().parseText(sdata);
		Map cacheMap=(Map)rootMap.get("root");
		String isLixiUpFlag=cacheMap.get(ProductAlgoConst.isLixiUpFlag);
		String isBenjinUpFlag=cacheMap.get(ProductAlgoConst.isBenjinUpFlag);
		String isFuwufeiUpFlag=cacheMap.get(ProductAlgoConst.isFuwufeiUpFlag);
		//String datalist=cacheMap.get(ProductAlgoConst.productPhaseList);
		List datas=cacheMap.get("inputList");
		//List datas=new JsonSlurper().parseText(datalist);

		
		String payDateStr=inMap.get(ProductAlgoConst.payDate);
		String dueDateStr=inMap.get(ProductAlgoConst.dueDate);
		String repayDayStr=inMap.get(ProductAlgoConst.repayDay);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date payDate=null;
		if(payDateStr!=null && !"".equals(payDateStr)){
			payDate=format.parse(payDateStr);
		}
		
		Date dueDate=null;
		if(dueDateStr!=null && !"".equals(dueDateStr)){
			dueDate=format.parse(dueDateStr);
		}
		
		Integer repayDay=0;
		if(repayDayStr!=null && !"".equals(repayDayStr)){
			repayDay=Integer.valueOf(repayDayStr);
		}

		String contractAmt=inMap.get(ProductAlgoConst.contractAmt);
		String monthRate=inMap.get(ProductAlgoConst.monthRate);
		String contractPeriods=inMap.get(ProductAlgoConst.contractPeriods);
		String singleGeneralRate=inMap.get(ProductAlgoConst.singleGeneralRate);
		

		Integer firstPeriodDays=getFirstPeriodDays(payDate,repayDay);
		List<Map> planList=getRepayDates(payDate,dueDate,repayDay);
		List lixiList=new ArrayList();
		List benjinList=new ArrayList();
		List fuwufeiList=new ArrayList();
		
		for(Map rowMap:planList){
			Boolean isNoMonth=rowMap.get(ProductAlgoConst.isNoMonth);
			String periodType=rowMap.get(ProductAlgoConst.periodType);
			Integer period=Integer.valueOf(rowMap.get(ProductAlgoConst.period));
			
			Map configMap=getPhaseByPeriod(datas,period);
			String lixiAlgId=configMap.get(ProductAlgoConst.productLixiAlgId);
			String benjinAlgId=configMap.get(ProductAlgoConst.productBenjinAlgId);
			String fuwufeiAlgId=configMap.get(ProductAlgoConst.productFuwufeiAlgId);
			String stageMonthRate=configMap.get(ProductAlgoConst.monthRate);
			//String singleGeneralRate=configMap.get(ProductAlgoConst.singleGeneralRate);
			
			Map paramMap=new HashMap();
			paramMap.put(ProductAlgoConst.isNoMonth, isNoMonth);
			paramMap.put(ProductAlgoConst.periodType, periodType);
			paramMap.put(ProductAlgoConst.firstPeriodDays, firstPeriodDays);
			paramMap.put(ProductAlgoConst.contractAmt, contractAmt);
			if(stageMonthRate!=null && !"".equals(stageMonthRate)){
				paramMap.put(ProductAlgoConst.monthRate, stageMonthRate);
			}else{
				paramMap.put(ProductAlgoConst.monthRate, monthRate);
			}
			paramMap.put(ProductAlgoConst.contractPeriods, contractPeriods);
			paramMap.put(ProductAlgoConst.singleGeneralRate, singleGeneralRate);

			System.out.println(paramMap.toString());
			String subMethod="";
			if(periodType.equals("first")){
				if(isNoMonth==true){
					subMethod= "firstPeriodNoMonth";
				}else{
					subMethod="firstPeriodFullMonth";
				}
			}else if(periodType.equals("mid")){
				subMethod="periodMonth";
			}else if(periodType.equals("last")){
				if(isNoMonth==true){
					subMethod="lastPeriodNoMonth";
				}else{
					subMethod="lastPeriodFullMonth";
				}
			}
			
			
			BigDecimal lixi=GroovyExecUtil.execGroovyRetObj("product_algo_lixi_xxhb",subMethod,paramMap);
			//rowMap.put("lixi", lixi);
			lixiList.add(lixi);
			BigDecimal benjin=GroovyExecUtil.execGroovyRetObj("product_algo_benjin_xxhb",subMethod,paramMap);
			//rowMap.put("benjin", benjin);
			benjinList.add(benjin);
			BigDecimal fuwufei=GroovyExecUtil.execGroovyRetObj("product_algo_fuwufei_yicixing",subMethod,paramMap);
			//rowMap.put("fuwufei", fuwufei);
			fuwufeiList.add(fuwufei);
		}
		List realList=new ArrayList();
		isLixiUpFlag="true"; 
		boolean zeroFlag=false;
		if("true".equals(isLixiUpFlag) || "true".equals(isBenjinUpFlag) || "true".equals(isFuwufeiUpFlag)){
			Map zeroMap=new HashMap();
			zeroMap.put(ProductAlgoConst.periodType, ProductAlgoConst.periodType_zero);
			zeroMap.put(ProductAlgoConst.period, Integer.valueOf(0));
			zeroMap.put(ProductAlgoConst.dueDate, payDateStr);
			realList.add(zeroMap);
			zeroFlag=true;
		}
		realList.addAll(planList);
		int realSize=realList.size();
		int planSize=planList.size();
		int lixiStartIndex=0;
		int benjinStartIndex=0;
		int fuwufeiStartIndex=0;
		if(!"true".equals(isLixiUpFlag) && zeroFlag==true){
			lixiStartIndex=1;
		}
		if(!"true".equals(isBenjinUpFlag) && zeroFlag==true){
			benjinStartIndex=1;
		}
		if(!"true".equals(isFuwufeiUpFlag) && zeroFlag==true){
			fuwufeiStartIndex=1;
		}
		for(int i=0;i<planSize;i++){
			Map tempMap=realList.get(lixiStartIndex++);
			BigDecimal lixi=lixiList.get(i);
			tempMap.put(ProductAlgoConst.interest, lixi);
		}
		for(int i=0;i<planSize;i++){
			Map tempMap=realList.get(benjinStartIndex++);
			BigDecimal benjin=benjinList.get(i);
			tempMap.put(ProductAlgoConst.capital, benjin);
		}
		for(int i=0;i<planSize;i++){
			Map tempMap=realList.get(fuwufeiStartIndex++);
			BigDecimal fuwufei=fuwufeiList.get(i);
			tempMap.put(ProductAlgoConst.serviceFee, fuwufei);
		}
		
		outMap.put("result", realList);
						
	}
	public static Map getPhaseByPeriod(List<Map> phaseList,int period){
		for(Map rowMap:phaseList){
			String start=rowMap.get("product_start_qishu");
			Integer startInt=null;
			if(start!=null && !"".equals(start)){
				startInt=Integer.valueOf(start);
			}
			
			String end=rowMap.get("product_end_qishu");
			Integer endInt=null;
			if(end!=null && !"".equals(end)){
				endInt=Integer.valueOf(end);
			}
			if((startInt==null || period>=startInt) && (endInt==null || period<=endInt)){
				return rowMap;
			}
		}
		return null;
	}
	
	public static int getFirstPeriodDays(Date payDate, int repayDay){
		Calendar payCalendar = Calendar.getInstance();
		payCalendar.setTime(payDate);
		Calendar firstRepayCalendar = Calendar.getInstance();
		firstRepayCalendar.setTime(payDate);
		if (repayDay == 0) {
			firstRepayCalendar.add(Calendar.DATE, -1);
			repayDay = firstRepayCalendar.get(Calendar.DATE);
			firstRepayCalendar.add(Calendar.DATE, 1);
		}
		if (repayDay > firstRepayCalendar.get(Calendar.DATE)) {
			firstRepayCalendar.add(Calendar.MONTH, 1);
		}
		firstRepayCalendar.set(Calendar.DATE, firstRepayCalendar.getActualMaximum(Calendar.DATE) < repayDay ? firstRepayCalendar.getActualMaximum(Calendar.DATE) : repayDay);
		int firstDays=daysBetween(payCalendar,firstRepayCalendar);
		return firstDays;
		
	}
	
	public static List<Map> getRepayDates(Date payDate, Date dueDate,  int repayDay) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(payDate);
		if (repayDay == 0) {
			calendar.add(Calendar.DATE, -1);
			repayDay = calendar.get(Calendar.DATE);
			calendar.add(Calendar.DATE, 1);
		}
		if (repayDay > calendar.get(Calendar.DATE)) {
			calendar.add(Calendar.MONTH, -1);
		}
		
		Boolean isNoMonthFlag=true;
		if(repayDay==calendar.get(Calendar.DATE)){
			isNoMonthFlag=true;
		}else{
			isNoMonthFlag=false;
		}
		
		
		int months = getMidPeriods(payDate, dueDate, repayDay) + 1;
		List<Map> resultList = new ArrayList<Map>(months + 1);
		for (int i = 0; i < months; i++) {
			calendar.add(Calendar.MONTH, 1);
			calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE) < repayDay ? calendar.getActualMaximum(Calendar.DATE) : repayDay);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String periodDueDate=sdf.format(calendar.getTime());
			if(i==0){//首期
				Map rowMap=new HashMap();
				rowMap.put(ProductAlgoConst.period, Integer.valueOf(1));
				rowMap.put(ProductAlgoConst.isNoMonth, isNoMonthFlag);
				rowMap.put(ProductAlgoConst.periodType, ProductAlgoConst.periodType_first);
				rowMap.put(ProductAlgoConst.dueDate, periodDueDate);
				resultList.add(rowMap);
			}else{//非首期
				Map rowMap=new HashMap();
				rowMap.put(ProductAlgoConst.period, Integer.valueOf(i+1));
				rowMap.put(ProductAlgoConst.isNoMonth, false);
				rowMap.put(ProductAlgoConst.periodType, ProductAlgoConst.periodType_mid);
				rowMap.put(ProductAlgoConst.dueDate, periodDueDate);
				resultList.add(rowMap);
			}
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String lastDueDate=sdf.format(dueDate.getTime());
		Map rowMap=new HashMap();
		rowMap.put(ProductAlgoConst.period, Integer.valueOf(months+1));
		rowMap.put(ProductAlgoConst.isNoMonth, isNoMonthFlag);
		rowMap.put(ProductAlgoConst.periodType, ProductAlgoConst.periodType_last);
		rowMap.put(ProductAlgoConst.dueDate, lastDueDate);
		resultList.add(rowMap);
		return resultList;
	}
	
	public static int getMidPeriods(Date payDate, Date dueDate, int repayDay) {
		Calendar start = Calendar.getInstance();
		start.setTime(payDate);
		
		if (repayDay == 0) {
			start.add(Calendar.DATE, -1);
			repayDay = start.get(Calendar.DATE);
			start.add(Calendar.DATE, 1);
		}
		// 找到首期的年月
		if (repayDay <= start.get(Calendar.DATE)) {
			start.add(Calendar.MONTH, 1);
		}
		// 找到倒数第二期的年月
		Calendar lastSecond = Calendar.getInstance();
		lastSecond.setTime(dueDate);
		if (lastSecond.get(Calendar.DATE) <= repayDay) {
			lastSecond.add(Calendar.MONTH, -1);
		}
		return (lastSecond.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12 + lastSecond.get(Calendar.MONTH) - start.get(Calendar.MONTH);
	}
	
	public static int daysBetween(GregorianCalendar pFormer,GregorianCalendar pLatter){
		GregorianCalendar vFormer = pFormer,vLatter = pLatter;
		boolean vPositive = true;
		if( pFormer.before(pLatter) ){
			vFormer = pFormer;
			vLatter = pLatter;
		}else{
			vFormer = pLatter;
			vLatter = pFormer;
			vPositive = false;
		}
 
		vFormer.set(Calendar.MILLISECOND,0);
		vFormer.set(Calendar.SECOND,0);
		vFormer.set(Calendar.MINUTE,0);
		vFormer.set(Calendar.HOUR_OF_DAY,0);
		vLatter.set(Calendar.MILLISECOND,0);
		vLatter.set(Calendar.SECOND,0);
		vLatter.set(Calendar.MINUTE,0);
		vLatter.set(Calendar.HOUR_OF_DAY,0);
 
		int vCounter = 0;
		while(vFormer.before(vLatter)){
			vFormer.add(Calendar.DATE, 1);
			vCounter++;
		}
		if( vPositive)
			return vCounter;
		else
			return -vCounter;
	}
	
	
	
}
