package com.nh.micro.db;

/**
 * 
 * @author ninghao
 *
 */
public class Cutil {
	public static final String R="<REPLACE>";
	public static String rep(String patten,String col){
		if(col==null){
			return null;
		}
		return patten.replace(R, col);
	}
	public static String rep(String patten,String col,boolean flag){
		if(flag==false){
			return null;
		}
		return patten.replace(R, col);
	}
	public static String jn(String split,String ... strings ){
		if(strings==null){
			return "";
		}
		int size=strings.length;
		StringBuilder sb=new StringBuilder("");
		for(int i=0;i<size;i++){
			if(strings[i]==null){
				continue;
			}
			if(sb.length()==0){
				sb.append(strings[i]);
			}else{
				sb.append(split).append(strings[i]);
			}
		}
		return sb.toString();
	}
	public static Cobj createCobj(String defaultSplit){
		return new Cobj(defaultSplit);
	}
	public static Cobj createCobj(){
		return new Cobj(",");
	}
}
