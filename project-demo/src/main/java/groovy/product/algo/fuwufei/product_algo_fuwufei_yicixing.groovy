package groovy.product.algo.fuwufei

import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;
import com.nh.micro.rule.engine.core.GContextParam;


import com.singularsys.jep.Jep
import com.singularsys.jep.bigdecimal.BigDecComponents

import java.math.BigDecimal;
import java.math.MathContext
import java.math.RoundingMode

import groovy.product.algo.ProductAlgoConst;

class ProductAlgoFuwufeiYicixing {
	
	/**
	 * 计算首期足月服务费
	 * @return
	 */
	public BigDecimal firstPeriodFullMonth(Map inMap){
		return total(inMap);
	}
	
	
	/**
	 * 计算首期不足月本金
	 * @return
	 */
	public BigDecimal firstPeriodNoMonth(Map inMap){
		return total(inMap);
	}

	/**
	 * 计算中间期本金
	 * @return
	 */
	public BigDecimal periodMonth(Map inMap){
		Jep jep = new Jep(new BigDecComponents(new MathContext(34, RoundingMode.HALF_UP)));
		jep.parse("0");
		BigDecimal result = (BigDecimal)jep.evaluate();
		return result.setScale(2, RoundingMode.HALF_UP);
	}

	
	/**
	 * 计算末期足月本金
	 * 合同金额
	 * @param contractAmt 合同金额
	 * @return
	 */
	public BigDecimal lastPeriodFullMonth(Map inMap){
		Jep jep = new Jep(new BigDecComponents(new MathContext(34, RoundingMode.HALF_UP)));
		jep.parse("0");
		BigDecimal result = (BigDecimal)jep.evaluate();
		return  result.setScale(2, RoundingMode.HALF_UP);
	}

	
	/**
	 * 计算末期不足月本金
	 * 合同金额
	 * @param contractAmt 合同金额
	 * @return
	 */
	public BigDecimal lastPeriodNoMonth(Map inMap){
		Jep jep = new Jep(new BigDecComponents(new MathContext(34, RoundingMode.HALF_UP)));
		jep.parse("0");
		BigDecimal result = (BigDecimal)jep.evaluate();
		return  result.setScale(2, RoundingMode.HALF_UP);
	}

	/**
	 * 计算总本金
	 * 合同金额
	 * @param contractAmt 合同金额
	 * @return
	 */
	public BigDecimal total(Map inMap){
		
		//合同额
		BigDecimal contractAmt=null;
		if(inMap.get(ProductAlgoConst.contractAmt)!=null && !"".equals(inMap.get(ProductAlgoConst.contractAmt))){
			contractAmt=new BigDecimal(inMap.get(ProductAlgoConst.contractAmt).toString());
		}
		
		//月利率
		BigDecimal monthRate=null;
		if(inMap.get(ProductAlgoConst.monthRate)!=null && !"".equals(inMap.get(ProductAlgoConst.monthRate))){
			monthRate=new BigDecimal(inMap.get(ProductAlgoConst.monthRate).toString());
		}
		
		//一次性综合费率
		BigDecimal singleGeneralRate=null;
		if(inMap.get(ProductAlgoConst.singleGeneralRate)!=null && !"".equals(inMap.get(ProductAlgoConst.singleGeneralRate))){
			singleGeneralRate=new BigDecimal(inMap.get(ProductAlgoConst.singleGeneralRate).toString());
		}
		
		Jep jep = new Jep(new BigDecComponents(new MathContext(34, RoundingMode.HALF_UP)));
		// 合同额
		jep.addVariable("contractAmt", contractAmt);

		// 一次性综合费率
		//jep.addVariable("singleGeneralRate", singleGeneralRate);
		jep.addVariable("singleGeneralRate", new BigDecimal("0.1"));
		// 月利率
		jep.addVariable("monthRate", monthRate);

		jep.parse("contractAmt * (singleGeneralRate - monthRate)");
		BigDecimal result = (BigDecimal)jep.evaluate();
		return  result.setScale(2, RoundingMode.HALF_UP);
	}
	
				
}
