package com.nh.micro.rule.engine.core;

import java.util.ArrayList;
import java.util.List;

public class GroovyAopChain {
	public List<GroovyAopInter> handlerList=new ArrayList();

	public List getHandlerList() {
		return handlerList;
	}

	public void setHandlerList(List handlerList) {
		this.handlerList = handlerList;
	}
	
	public static GroovyAopInter firstAop=null;
	public static GroovyAopInter getFirstAop() {
		return firstAop;
	}

	public static void setFirstAop(GroovyAopInter firstAop) {
		GroovyAopChain.firstAop = firstAop;
	}

	public void init(){
		int size=handlerList.size();
		if(size<=0){
			return;
		}
		GroovyAopChain.firstAop=handlerList.get(0);
		for(int i=0;i<size;i++){
			GroovyAopInter oneAop=handlerList.get(i);
			if(i+1<size){
				GroovyAopInter twoAop=handlerList.get(i+1);
				oneAop.setNextAop(twoAop);
			}
		}
	}
}
