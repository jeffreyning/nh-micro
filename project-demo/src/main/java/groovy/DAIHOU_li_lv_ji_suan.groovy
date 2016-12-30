package groovy

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Map
import java.lang.String

import com.nh.cache.base.*
import java.util.Set
import net.sf.json.JSONObject
import com.nh.micro.rule.engine.core.GroovyExecUtil;

import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;

class nrule{
 public void execGroovy(GInputParam gInputParam,GOutputParam gOutputParam){
Map inMap=gInputParam.getParamData();
Map outMap=gOutputParam.getResultObj();
  System.out.println("this is lilvjishuan");
  String productId=(String) inMap.get("product_id");
  String key="DAIHOU_"+productId
  NhCacheObject nhCacheObject=NhCacheHolder.getCacheObject(key);
  String productConf=nhCacheObject.getCacheData();
  JSONObject productInfoObj=JSONObject.fromObject(productConf);
  String liLvGongShiId=productInfoObj.get("li_lv_gong_shi_id");
  String groovyName="DAIHOU_"+liLvGongShiId;
  JSONObject liLvGongShiParamObj= productInfoObj.get("li_lv_gong_shi_param");
  Map subInMap=new HashMap();
  subInMap.putAll(liLvGongShiParamObj);
  subInMap.putAll(inMap);
  System.out.println(groovyName);
  GInputParam subGInputParam=new GInputParam(subInMap);
  boolean execStatus=GroovyExecUtil.execGroovySimple4Obj(groovyName, subGInputParam, gOutputParam);
  if(execStatus==false){
	  gOutputParam.setResultStatus(1);
  }
  return ;
 }

}
