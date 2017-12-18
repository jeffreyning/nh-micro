package com.nh.micro.nhs;


/**
 * 
 * @author ninghao
 *
 */
public class CheckSqlUtil {
	public static void checkSql(StringBuilder sb) {
		String temp=sb.toString().toLowerCase();
		temp=temp.replace("\r", " ");
		temp=temp.replace("\n", " ");
		temp=temp.replace("\t", " ");
		String[] tempArray=temp.split(" set ");
		if(tempArray.length>1){
			int length=0;
			for(int i=0;i<tempArray.length;i++){
				String tempStr=tempArray[i];
				
				if(i==0){
					length=length+tempStr.length()+5;
					continue;
				}
				int equalStart=tempStr.indexOf("=");
				int douStart=tempStr.indexOf(",");
				if(equalStart>=0 && douStart>=0 && douStart<equalStart){
					int repIndex=length+douStart;
					sb.setCharAt(repIndex, ' ');
				}
				length=length+tempStr.length()+5;
			}
		}
	}
}
