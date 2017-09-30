package groovy.template.mquery;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.nh.micro.db.*;

import com.nh.micro.rule.engine.core.*;
import com.nh.micro.db.MicroMetaDao;


import groovy.json.*;
import groovy.template.MicroMvcTemplate;
import net.sf.json.JSONArray
import net.sf.json.JSONObject

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import java.util.concurrent.*;

class MicroMergeQueryTemplate extends MicroMvcTemplate{
	public static Map mergeQueryMap=new HashMap();
	private static Logger logger=Logger.getLogger(MicroMergeQueryTemplate.class);
	public String getOriginSql(HttpServletRequest httpRequest){
		String originSql=httpRequest.getParameter("originSql");
		return originSql;
	} 
	
	public String getMemOrderStr(HttpServletRequest httpRequest){
		String memOrderStr=httpRequest.getParameter("memOrderStr");
		return memOrderStr;
	}
	
	private List<String> dbNameList=new ArrayList();
	public List getDbNameList(){
		return this.dbNameList;
	}
	
	private List<String> viewNameList=new ArrayList();
	public List getViewNameList(){
		return this.viewNameList;
	}

	

	public List getDbNameList(HttpServletRequest httpRequest){
		String dbNameList=httpRequest.getParameter("dbNameList");
		dbNameList=dbNameList+",";
		String[] dbNameArray=dbNameList.split(",");
		
		return Arrays.asList(dbNameArray);
	}
	

	public List getViewNameList(HttpServletRequest httpRequest){
		String viewNameList=httpRequest.getParameter("viewNameList");
		String[] viewNameArray=viewNameList.split(",");
		return Arrays.asList(viewNameArray);
	}
	
	
	//返回数据过滤
	public void resultFilter(Map retMap){
		return;
	}

	//单库单表查询
	public static void querySqlByThread(String originSql,String dbName,String viewName,Integer limitIndex, Integer rowNum, List allList,Map inputHolder,Map allSubMap){
		String limitStr=limitIndex+","+rowNum;
		String replaceSql=originSql.replace("<REP_VIEW_NAME>", viewName);
		String querySql=replaceSql+" limit "+limitStr;
		querySql="select SQL_CALC_FOUND_ROWS "+querySql.substring(6) ;
		System.out.println(querySql);
		MicroMetaDao microDao=MicroMetaDao.getInstance(dbName);
		List result=microDao.queryObjJoinByCondition(querySql);
		System.out.println("sublist="+result.toString());
		allList.addAll(result);
		
		String countSql="SELECT FOUND_ROWS() as total";
		List tempList=microDao.queryObjJoinByCondition(countSql);
		Long subtotal=0l;
		if(tempList!=null){
			Map tempMap=(Map) tempList.get(0);
			subtotal=(Long) tempMap.get("total");
		}
		
		//累加总记录数
		int total=inputHolder.get("total");
		inputHolder.put("total", total+subtotal);
		
		Map subList=allSubMap.get(dbName);
		if(subList==null){
			subList=new HashMap();
			allSubMap.put(dbName, subList);
		}
		subList.put(viewName, result);
		
	}

	
	public Map mergeService(String mergeQueryId,List<String> dbNameList,List<String> viewNameList, String originSql, String orderStr, Integer rowNum, Map leftIndexMap, Map rightIndexMap, Integer directFlag, Integer curPage,Integer totalPage ){
		Map outIndexMap=new HashMap();
		//首页不能向前翻页
		if((curPage==null || curPage<=1) && directFlag==2){
			return null;
		}
		if(totalPage!=null && totalPage<=curPage && directFlag==1){
			return null;
		}
		
		List allList=new ArrayList();
		Map allSubList=new HashMap();
		long total=0l;
		long totalLimitIndex=0l;
		long totalLeftIndex=0l;
		List threadList=new ArrayList();
		Map inputHolder=new HashMap();
		inputHolder.put("total", 0);
		for(String dbName:dbNameList){
			for(String viewName:viewNameList){
				int leftIndex=0;
				if(leftIndexMap!=null){
					Map tempDbMap=leftIndexMap.get(dbName);
					if(tempDbMap!=null){
						Integer tempStartIndex=tempDbMap.get(viewName);
						if(tempStartIndex!=null){
							//记录查询原有起始位置
							leftIndex=tempStartIndex;
						}
					}
				}
				
				int rightIndex=0;
				if(rightIndexMap!=null){
					Map tempDbMap=rightIndexMap.get(dbName);
					if(tempDbMap!=null){
						Integer tempEndIndex=tempDbMap.get(viewName);
						if(tempEndIndex!=null){
							//记录查询处理后起始位置
							rightIndex=tempEndIndex;
						}
					}
				}
				
				//向前翻页还是向后翻页
				int limitIndex=0;
				
				if(directFlag==null || directFlag==0){
					limitIndex=0;
				}else if(directFlag==1){
					limitIndex=rightIndex;
				}else if(directFlag==2){
					limitIndex=leftIndex-rowNum;
					if(limitIndex<0){
						limitIndex=0;
					}
				}else if(directFlag==3){
					limitIndex=0;
				}

				totalLeftIndex=totalLeftIndex+leftIndex;
				//累加总起始位置
				totalLimitIndex=totalLimitIndex+limitIndex;
				final String tempViewName=viewName;
				final String tempDbName=dbName;
				Thread tempThread=new Thread(new Runnable(){
					public void run(){
						System.out.println("viewName======="+tempViewName);
						MicroMergeQueryTemplate.querySqlByThread(originSql, tempDbName,tempViewName, limitIndex, rowNum, allList, inputHolder, allSubList);
					}
				});
				tempThread.start();
				threadList.add(tempThread);
			}
		}
		
		int threadSize=threadList.size();
		for(int i=0;i<threadSize;i++){
			Thread t=threadList.get(i);
			t.join();
		}
		int realSize=allList.size();
		System.out.println("alllist="+allList.toString());
		List tList=sortList(allList,orderStr);
		System.out.println("alllist after sort="+tList.toString());
		int last=rowNum;
		if(realSize<rowNum){
			last=realSize;
		}
		

		Map nleftMap=new HashMap();
		Map nrightMap=new HashMap();
		List retList=tList.subList(0,last);

		if(retList.size()>0){
			Map oldIndexMap=null;
			if(directFlag==null || directFlag==0){
				oldIndexMap=new HashMap();
			}else if(directFlag==1){
				oldIndexMap=rightIndexMap;
			}else if(directFlag==2){
				oldIndexMap=leftIndexMap;
			}
			calcuIndexMap( oldIndexMap, directFlag, allSubList , retList, dbNameList, viewNameList, outIndexMap);

			if(directFlag==null || directFlag==0 ){
				nrightMap=outIndexMap;
	
			}else if(directFlag==1){
				nrightMap=outIndexMap;
				nleftMap=rightIndexMap;
	
			}else if(directFlag==2){
				nleftMap=outIndexMap;
				nrightMap=leftIndexMap;
	
			}
		}else{
			if(directFlag==null || directFlag==0){
				nleftMap=new HashMap();
				nrightMap=new HashMap();
			}else if(directFlag==1){
				nleftMap=rightIndexMap;
				nrightMap=rightIndexMap;
			}else if(directFlag==2){
				nleftMap=leftIndexMap;
				nrightMap=leftIndexMap;
			}

		}
		
		int curSize=retList.size();
		long realTotalLimit=0l;
			if(directFlag==null || directFlag==0 ){
				realTotalLimit=0l
			}else if(directFlag==1){
				realTotalLimit=totalLimitIndex;
			}else if(directFlag==2){
				realTotalLimit=totalLeftIndex-curSize;
	
			}
		
		Map retMap=new HashMap();
		total=inputHolder.get("total");
		retMap.put("total", total);
		retMap.put("rows", retList); 
		retMap.put("curSize", curSize);
		retMap.put("totalLimitIndex", realTotalLimit);
		long thisPageNum=realTotalLimit/rowNum+1;
		long totalPageNum=total/rowNum+1;
		retMap.put("thisPageNum", thisPageNum);
		retMap.put("totalPageNum", totalPageNum);
		
		retMap.put("leftIndexMap", nleftMap);
		retMap.put("rightIndexMap", nrightMap);
		retMap.put("status", "0");
		
		if(mergeQueryId!=null && !"".equals(mergeQueryId)){
			Map cacheMap=mergeQueryMap.get(mergeQueryId);
			if(cacheMap==null){
				cacheMap=new HashMap();
				mergeQueryMap.put(mergeQueryId,cacheMap);
				
			}

			cacheMap.put("total", total);
			cacheMap.put("curSize", curSize);
			cacheMap.put("totalLimitIndex", realTotalLimit);
			cacheMap.put("curPage", thisPageNum);
			cacheMap.put("totalPage", totalPageNum);
			cacheMap.put("leftIndexMap", nleftMap);
			cacheMap.put("rightIndexMap", nrightMap);
		}
		return retMap;
	}
	
	//计算偏移量
	public void calcuIndexMap(Map oldIndexMap,Integer directFlag,Map allSubList ,List retList,List dbNameList,List viewNameList,Map outIndexMap){
		Map nIndexMap=outIndexMap;
		for(String dbName:dbNameList){
			Map dbIndexMap=nIndexMap.get(dbName);
			if(dbIndexMap==null){
				dbIndexMap=new HashMap();
				nIndexMap.put(dbName, dbIndexMap);
			}
			for(String viewName:viewNameList){
				int index=0;
				if(oldIndexMap!=null){
					Map tempDbMap=oldIndexMap.get(dbName);
					if(tempDbMap!=null){
						Integer tempStartIndex=tempDbMap.get(viewName);
						if(tempStartIndex!=null){
							//记录查询原有起始位置
							index=tempStartIndex;
						}
					}
				}
				
				int selected=0;
				Map tempMap=allSubList.get(dbName);
				if(tempMap!=null){
					List subList=tempMap.get(viewName);
					List retainList=null;
					if(subList!=null){
						subList.retainAll(retList);
						retainList=subList;
					}
					if(retainList!=null){
						selected=retainList.size();
					}
					
				}
				int nIndex=0;
				if(directFlag==null || directFlag==0){
					nIndex=selected;
				}else if(directFlag==1){
					nIndex=index+selected;
				}else if(directFlag==2){
					nIndex=index-selected;
					if(nIndex<0){
						nIndex=0;
					}
				}
				dbIndexMap.put(viewName, nIndex);
			}
	
		}

	}
	

	//内存数据排序
	public List sortList(List<Map> allList,String orderStr){
		List orderList=new ArrayList();
		if(orderStr!=null && !"".equals(orderStr)){
			String[] orderArray=orderStr.split(",");
			for(String temp:orderArray){
				String[] sortArray=temp.split(" ");
				if(sortArray.length==2){
					orderList.add(sortArray);
				}
			}
		}
		Collections.sort(allList, new Comparator() {
		  @Override
		  public int compare(Object oleft, Object oright) {
			  int listSize=orderList.size();
			  for(int i=0;i<listSize;i++){
				  String[] rowArray=orderList.get(i);
				  String colName=rowArray[0];
				  String colSort=rowArray[1];
				  Object colLeft=((Map)oleft).get(colName);
				  Object colRight=((Map)oright).get(colName);
				  int compareStatus=((Comparable)colLeft).compareTo(colRight);
				  if(compareStatus!=0){
					  if(colSort.equalsIgnoreCase("desc")){
						  return compareStatus*-1;
					  }else if(colSort.equalsIgnoreCase("asc")){
					  		return compareStatus;
					  }
				  }
			  }
			  return 0;

		  }
	
		});
		return allList;
	}
	
	
 

	public void getInfoList4Page(GInputParam gInputParam,GOutputParam gOutputParam,GContextParam gContextParam){
		HttpSession httpSession=gContextParam.getContextMap().get("httpSession");
		HttpServletRequest httpRequest = gContextParam.getContextMap().get("httpRequest");
		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		
		//每页记录数
		String rows=httpRequest.getParameter("rows");
		Integer rowNumInt=10;
		if(rows!=null && !rows.equals("")){
			rowNumInt=Integer.valueOf(rows);
		}

		//当前页
		String curPageStr=httpRequest.getParameter("curPage");
		Integer curPage=null;
		if(curPageStr!=null && !curPageStr.equals("")){
			curPage=Integer.valueOf(curPageStr);
		}
		
		//记录总数
		String totalPageStr=httpRequest.getParameter("totalPage");
		Integer totalPage=null;
		if(totalPageStr!=null && !totalPageStr.equals("")){
			totalPage=Integer.valueOf(totalPageStr);
		}
		
		//翻页方向标识 0首页\1后翻\2前翻
		String directFlagStr=httpRequest.getParameter("directFlag");
		Integer directFlag=0;
		if(directFlagStr!=null && !"".equals(directFlagStr)){
			directFlag=Integer.valueOf(directFlagStr);
		}
		
		Map leftIndexMap=null;
		Map rightIndexMap=null;
		
		//存在queryId
		String mergeQueryId=httpRequest.getParameter("mergeQueryId");
		System.out.println("mergeQueryId====="+mergeQueryId);
		if(mergeQueryId!=null && !"".equals(mergeQueryId)){
			Map cacheMap=mergeQueryMap.get(mergeQueryId);
			if(cacheMap==null){
				cacheMap=new HashMap();
				mergeQueryMap.put(mergeQueryId,cacheMap);
				
			}
			curPageStr=cacheMap.get("curPage");
			if(curPageStr!=null && !curPageStr.equals("")){
				curPage=Integer.valueOf(curPageStr);
			}
			totalPageStr=cacheMap.get("totalPage");
			if(totalPageStr!=null && !totalPageStr.equals("")){
				totalPage=Integer.valueOf(totalPageStr);
			}
			leftIndexMap=cacheMap.get("leftIndexMap");
			rightIndexMap=cacheMap.get("rightIndexMap");
		}else{
			//偏移量
			String leftIndexMapStr=httpRequest.getParameter("leftIndexMap");
			String rightIndexMapStr=httpRequest.getParameter("rightIndexMap");
	
			if(directFlag!=null && directFlag!=0){
				if(leftIndexMapStr!=null && !leftIndexMapStr.equals("")){
					leftIndexMap=new JsonSlurper().parseText(leftIndexMapStr);
				}
				if(rightIndexMapStr!=null && !rightIndexMapStr.equals("")){
					rightIndexMap=new JsonSlurper().parseText(rightIndexMapStr);
				}
	
			}
		}
		

		

		
		//数据库列表
		List<String> dbNameList=getDbNameList(httpRequest);
		//分表名称列表
		List<String> viewNameList=getViewNameList(httpRequest);
		//查询sql
		String originSql=getOriginSql(httpRequest);
		//内存排序字符
		String memOrderStr=getMemOrderStr(httpRequest);


		//分表合并查询
		Map retMap=mergeService(mergeQueryId,dbNameList,viewNameList, originSql, memOrderStr, 10, leftIndexMap, rightIndexMap, directFlag, curPage,totalPage);
		if(retMap==null){
			retMap=new HashMap();
			retMap.put("status", "error");

		}
		
		//返回数据过滤
		resultFilter(retMap);

		
		JsonBuilder jsonBuilder=new JsonBuilder(retMap);
		String retStr=jsonBuilder.toString();
		System.out.println(retStr);

		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
	
	public void execMock(HttpServletRequest httpRequest, HttpServletResponse httpResponse){
		//每页记录数
		String rows=httpRequest.getParameter("rows");
		Integer rowNumInt=10;
		if(rows!=null && !rows.equals("")){
			rowNumInt=Integer.valueOf(rows);
		}

		//当前页
		String curPageStr=httpRequest.getParameter("curPage");
		Integer curPage=null;
		if(curPageStr!=null && !curPageStr.equals("")){
			curPage=Integer.valueOf(curPageStr);
		}
		
		//记录总数
		String totalPageStr=httpRequest.getParameter("totalPage");
		Integer totalPage=null;
		if(totalPageStr!=null && !totalPageStr.equals("")){
			totalPage=Integer.valueOf(totalPageStr);
		}
		
		//翻页方向标识 0首页\1后翻\2前翻
		String directFlagStr=httpRequest.getParameter("directFlag");
		Integer directFlag=0;
		if(directFlagStr!=null && !"".equals(directFlagStr)){
			directFlag=Integer.valueOf(directFlagStr);
		}
		
		Map leftIndexMap=null;
		Map rightIndexMap=null;
		
		//存在queryId
		String mergeQueryId=httpRequest.getParameter("mergeQueryId");
		if(mergeQueryId!=null && !"".equals(mergeQueryId)){
			Map cacheMap=mergeQueryMap.get(mergeQueryId);
			if(cacheMap==null){
				cacheMap=new HashMap();
				mergeQueryMap.put(mergeQueryId,cacheMap);
				
			}
			curPageStr=cacheMap.get("curPage");
			if(curPageStr!=null && !curPageStr.equals("")){
				curPage=Integer.valueOf(curPageStr);
			}
			totalPageStr=cacheMap.get("totalPage");
			if(totalPageStr!=null && !totalPageStr.equals("")){
				totalPage=Integer.valueOf(totalPageStr);
			}
			leftIndexMap=cacheMap.get("leftIndexMap");
			rightIndexMap=cacheMap.get("rightIndexMap");
		}else{
			//偏移量
			String leftIndexMapStr=httpRequest.getParameter("leftIndexMap");
			String rightIndexMapStr=httpRequest.getParameter("rightIndexMap");
	
			if(directFlag!=null && directFlag!=0){
				if(leftIndexMapStr!=null && !leftIndexMapStr.equals("")){
					leftIndexMap=new JsonSlurper().parseText(leftIndexMapStr);
				}
				if(rightIndexMapStr!=null && !rightIndexMapStr.equals("")){
					rightIndexMap=new JsonSlurper().parseText(rightIndexMapStr);
				}
	
			}
		}
		

		

		
		//数据库列表
		List<String> dbNameList=getDbNameList();
		//分表名称列表
		List<String> viewNameList=getViewNameList();
		//查询sql
		String originSql=getOriginSql(httpRequest);
		//内存排序字符
		String memOrderStr=getMemOrderStr(httpRequest);


		//分表合并查询
		Map retMap=mergeService(mergeQueryId,dbNameList,viewNameList, originSql, memOrderStr, 10, leftIndexMap, rightIndexMap, directFlag, curPage,totalPage);
		if(retMap==null){
			retMap=new HashMap();
			retMap.put("status", "error");

		}
		
		//返回数据过滤
		resultFilter(retMap);

		
		JsonBuilder jsonBuilder=new JsonBuilder(retMap);
		String retStr=jsonBuilder.toString();
		System.out.println(retStr);

		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		httpRequest.setAttribute("forwardFlag", "true");
		return;
	}
	

}
