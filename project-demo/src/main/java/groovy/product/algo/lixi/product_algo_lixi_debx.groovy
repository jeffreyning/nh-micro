package groovy.product.algo.lixi

import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;
import com.nh.micro.rule.engine.core.GContextParam;


import com.singularsys.jep.Jep
import com.singularsys.jep.bigdecimal.BigDecComponents

import java.math.BigDecimal;
import java.math.MathContext
import java.math.RoundingMode
import groovy.product.algo.ProductAlgoConst;

class nrule {
	
	/**
	 * 计算首期足月利息
	 * @param contractAmt 合同金额
	 * @param monthRate 月利率
	 * @return
	 */
	public BigDecimal firstPeriodFullMonth(Map inMap){
		return periodMonth(inMap);
	}
	
	
	/**
	 * 计算首期不足月利息
	 * @param contractAmt 合同金额
	 * @param monthRate 月利率
	 * @param firstPeriodDays 首期时间天数 
	 * @return
	 */
	public BigDecimal firstPeriodNoMonth(Map inMap){
		return null;
	}

	/**
	 * 计算中间期利息 
	 * 合同金额*月利率
	 * @param contractAmt 合同金额
	 * @param monthRate 月利率
	 * @return
	 */
	public BigDecimal periodMonth(Map inMap){
		
		BigDecimal contractAmt=null;
		if(inMap.get(ProductAlgoConst.contractAmt)!=null && !"".equals(inMap.get(ProductAlgoConst.contractAmt))){
			contractAmt=new BigDecimal(inMap.get(ProductAlgoConst.contractAmt).toString());
		}
		
		BigDecimal monthRate=null;
		if(inMap.get(ProductAlgoConst.monthRate)!=null && !"".equals(inMap.get(ProductAlgoConst.monthRate))){
			monthRate=new BigDecimal(inMap.get(ProductAlgoConst.monthRate).toString());
		}
		
		BigDecimal contractPeriods=null;
		if(inMap.get(ProductAlgoConst.contractPeriods)!=null && !"".equals(inMap.get(ProductAlgoConst.contractPeriods))){
			contractPeriods=new BigDecimal(inMap.get(ProductAlgoConst.contractPeriods).toString());
		}
		
		Jep jep = new Jep(new BigDecComponents(new MathContext(34, RoundingMode.HALF_UP)));
		// 合同额
		jep.addVariable("contractAmt", contractAmt);

		// 月利率
		jep.addVariable("monthRate", monthRate);

		// 期数
		jep.addVariable("contractPeriods", contractPeriods);

		jep.parse("((contractAmt * monthRate * ((1 + monthRate) ^ contractPeriods)) / ((1 + monthRate) ^ contractPeriods - 1))");
		BigDecimal result = (BigDecimal)jep.evaluate();
		return result.setScale(2, RoundingMode.HALF_UP);
	}

	
	/**
	 * 计算末期足月利息
	 * 合同金额*月利率
	 * @param contractAmt 合同金额
	 * @param monthRate 月利率
	 * @param hopePeriod 合同期数
	 * @return
	 */
	public BigDecimal lastPeriodFullMonth(Map inMap){
		return periodMonth(inMap);
	}

	
	/**
	 * 计算末期不足月利息
	 * 合同金额*月利率
	 * @param contractAmt 合同金额
	 * @param monthRate 月利率
	 * @param hopePeriod 产品期数
	 * @param firstPeriodDays 实际首期天数
	 * @return
	 */
	public BigDecimal lastPeriodNoMonth(Map inMap){
		return null;
	}

	/**
	 * 计算预期总利息
	 * 合同金额*月利率*产品期数
	 * @param contractAmt 合同金额
	 * @param monthRate 月利率
	 * @param hopePeriod 合同期数
	 * @return
	 */
	public BigDecimal total(Map inMap){
		
		BigDecimal contractAmt=null;
		if(inMap.get(ProductAlgoConst.contractAmt)!=null && !"".equals(inMap.get(ProductAlgoConst.contractAmt))){
			contractAmt=new BigDecimal(inMap.get(ProductAlgoConst.contractAmt).toString());
		}
		
		BigDecimal monthRate=null;
		if(inMap.get(ProductAlgoConst.monthRate)!=null && !"".equals(inMap.get(ProductAlgoConst.monthRate))){
			monthRate=new BigDecimal(inMap.get(ProductAlgoConst.monthRate).toString());
		}
		
		BigDecimal contractPeriods=null;
		if(inMap.get(ProductAlgoConst.contractPeriods)!=null && !"".equals(inMap.get(ProductAlgoConst.contractPeriods))){
			contractPeriods=new BigDecimal(inMap.get(ProductAlgoConst.contractPeriods).toString());
		}
		
		Jep jep = new Jep(new BigDecComponents(new MathContext(34, RoundingMode.HALF_UP)));
		// 合同额
		jep.addVariable("contractAmt", contractAmt);

		// 月利率
		jep.addVariable("monthRate", monthRate);

		// 期数
		jep.addVariable("contractPeriods", contractPeriods);

		jep.parse("((contractAmt * monthRate * ((1 + monthRate) ^ contractPeriods)) / ((1 + monthRate) ^ contractPeriods - 1))");
		jep.parse(((BigDecimal)jep.evaluate()).setScale(2, RoundingMode.HALF_UP) + " * contractPeriods - contractAmt");
		BigDecimal result = (BigDecimal)jep.evaluate();
		return result.setScale(2, RoundingMode.HALF_UP);
	}
	
				
}
