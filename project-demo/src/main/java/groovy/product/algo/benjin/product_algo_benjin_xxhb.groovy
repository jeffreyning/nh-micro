package groovy.product.algo.benjin

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
	 * 计算首期足月本金
	 * @return
	 */
	public BigDecimal firstPeriodFullMonth(Map inMap){
		Jep jep = new Jep(new BigDecComponents(new MathContext(34, RoundingMode.HALF_UP)));
		jep.parse("0");
		BigDecimal result = (BigDecimal)jep.evaluate();
		return result.setScale(2, RoundingMode.HALF_UP);
	}
	
	
	/**
	 * 计算首期不足月本金
	 * @return
	 */
	public BigDecimal firstPeriodNoMonth(Map inMap){
		Jep jep = new Jep(new BigDecComponents(new MathContext(34, RoundingMode.HALF_UP)));
		jep.parse("0");
		BigDecimal result = (BigDecimal)jep.evaluate();
		return result.setScale(2, RoundingMode.HALF_UP);
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
		BigDecimal contractAmt=null;
		if(inMap.get(ProductAlgoConst.contractAmt)!=null && !"".equals(inMap.get(ProductAlgoConst.contractAmt))){
			contractAmt=new BigDecimal(inMap.get(ProductAlgoConst.contractAmt).toString());
		}
		Jep jep = new Jep(new BigDecComponents(new MathContext(34, RoundingMode.HALF_UP)));
		jep.addVariable("contractAmt", contractAmt);
		jep.parse("contractAmt");
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
		BigDecimal contractAmt=null;
		if(inMap.get(ProductAlgoConst.contractAmt)!=null && !"".equals(inMap.get(ProductAlgoConst.contractAmt))){
			contractAmt=new BigDecimal(inMap.get(ProductAlgoConst.contractAmt).toString());
		}
		Jep jep = new Jep(new BigDecComponents(new MathContext(34, RoundingMode.HALF_UP)));
		jep.addVariable("contractAmt", contractAmt);
		jep.parse("contractAmt");
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
		BigDecimal contractAmt=null;
		if(inMap.get(ProductAlgoConst.contractAmt)!=null && !"".equals(inMap.get(ProductAlgoConst.contractAmt))){
			contractAmt=new BigDecimal(inMap.get(ProductAlgoConst.contractAmt).toString());
		}
		Jep jep = new Jep(new BigDecComponents(new MathContext(34, RoundingMode.HALF_UP)));
		jep.addVariable("contractAmt", contractAmt);
		jep.parse("contractAmt");
		BigDecimal result = (BigDecimal)jep.evaluate();
		return  result.setScale(2, RoundingMode.HALF_UP);
	}
	
				
}
