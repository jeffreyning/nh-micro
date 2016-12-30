

package groovy.gongshi

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Map
import java.lang.String
import com.singularsys.jep.Jep;
import com.singularsys.jep.bigdecimal.BigDecComponents;
import java.math.MathContext;
import java.math.RoundingMode;

import com.nh.micro.rule.engine.core.GInputParam;
import com.nh.micro.rule.engine.core.GOutputParam;


class nrule{
 public void execGroovy(GInputParam gInputParam,GOutputParam gOutputParam){
Map inMap=gInputParam.getParamData();
Map outMap=gOutputParam.getResultObj();
  System.out.println("this is lilv01");
  if(inMap.get("hkr_flag").equals(true) && inMap.get("zy_flag").equals(true)){
Jep jep = new Jep(new BigDecComponents(new MathContext(2,RoundingMode.HALF_UP)));
if(inMap.get("he_tong_e")!=null){
jep.addVariable("he_tong_e", new BigDecimal(inMap.get("he_tong_e")));
}
if(inMap.get("yue_li_lv")!=null){
jep.addVariable("yue_li_lv", new BigDecimal(inMap.get("yue_li_lv")));
}
if(inMap.get("shi_ji_tian_shu")!=null){
jep.addVariable("shi_ji_tian_shu", new BigDecimal(inMap.get("shi_ji_tian_shu")));
}
String exp="he_tong_e*yue_li_lv";
jep.parse(exp);
BigDecimal result = (BigDecimal) jep.evaluate();
result=result.setScale(2,   BigDecimal.ROUND_HALF_UP);
outMap.put("result",result);
}
if(inMap.get("hkr_flag").equals(true) && inMap.get("zy_flag").equals(false)){
Jep jep = new Jep(new BigDecComponents(new MathContext(2,RoundingMode.HALF_UP)));
if(inMap.get("he_tong_e")!=null){
jep.addVariable("he_tong_e", new BigDecimal(inMap.get("he_tong_e")));
}
if(inMap.get("yue_li_lv")!=null){
jep.addVariable("yue_li_lv", new BigDecimal(inMap.get("yue_li_lv")));
}
if(inMap.get("shi_ji_tian_shu")!=null){
jep.addVariable("shi_ji_tian_shu", new BigDecimal(inMap.get("shi_ji_tian_shu")));
}
String exp="he_tong_e*yue_li_lv/30*shi_ji_tian_shu";
jep.parse(exp);
BigDecimal result = (BigDecimal) jep.evaluate();
result=result.setScale(2,   BigDecimal.ROUND_HALF_UP);
outMap.put("result",result);
}
if(inMap.get("hkr_flag").equals(false) && inMap.get("zy_flag").equals(true)){
Jep jep = new Jep(new BigDecComponents(new MathContext(2,RoundingMode.HALF_UP)));
if(inMap.get("he_tong_e")!=null){
jep.addVariable("he_tong_e", new BigDecimal(inMap.get("he_tong_e")));
}
if(inMap.get("yue_li_lv")!=null){
jep.addVariable("yue_li_lv", new BigDecimal(inMap.get("yue_li_lv")));
}
if(inMap.get("shi_ji_tian_shu")!=null){
jep.addVariable("shi_ji_tian_shu", new BigDecimal(inMap.get("shi_ji_tian_shu")));
}
String exp="he_tong_e*yue_li_lv";
jep.parse(exp);
BigDecimal result = (BigDecimal) jep.evaluate();
result=result.setScale(2,   BigDecimal.ROUND_HALF_UP);
outMap.put("result",result);
}

  return ;
 }

}

