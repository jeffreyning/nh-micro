# nh-micro
micro service and dynamic script

MVC框架的缺点
系统按照Controller、Service、Dao层次划分，虽然在建设初期技术结构清晰。但每个业务逻辑代码都分散到不同代码框，业务结构不直观，不利于后期业务重构。
业务代码与MVC技术栈深度耦合，某层技术框架升级，导致整体服务不可复用。
由于代码分散，支持版本化成本很高。



脚本化微服务(NHMicro开源框架)实现MVC反模式
Controller层改造

使用微服务交互接口取代Controller，或者仍使用原有Controller技术栈，但调用NHMicro微服务提供的脚本调用Util。
Service层改造

uService层功能代码脚本化，脚本化有利于快速调整调用流程，有利于版本化。
Dao层改造

统一封装Dao（NHMicro开源框架已经提供Dao封装），基于Spring-jdbcTemplate封装统一Dao。



脚本化微服务(NHMicro开源框架)-实现技术栈中的虚拟机
NHMicro微服务架构中交互层和dao层不含有业务属性，需求变更时不需要修改微服务交互层代码和微服务Dao层代码。

一个脚本实现一个业务功能，业务框架清晰。

脚本内容修改后，设置新的名称，可以和旧脚本部署在同一个系统中，实现版本化。

脚本可以被复制到另一个不同MVC栈的系统中，实现代码复用。

NHMicro微服务架构是Java技术栈中的虚拟机，一次脚本编写到处复用。


nh-micro框架开源代码和demo已经开源地址为

https://github.com/jeffreyning/nh-micro


micro-rule-engine项目为脚本化核心引擎

核心引擎基于groovy技术实现，支持groovy脚本热加载。

服务业务逻辑均使用groovy实现脚本化。



nh-micro-db项目为标准化dao层

标准化dao层基于jdbctemplate封装，同时支持MySQL和Oracle，可以在框架外单独使用。

忽略orm映射功能，内置根据id进行增删改查接口，强化参数类型兼容性。

使开发人员不必关系查询条件参数或查询结果的数据类型。提高开发效率。



nh-micro-template为nh-micro-db层工具化封装

可以快速构建增删改查应用。目前支持mysql，后续支持oracle。

自动过滤参数中有效字段。如果使用mysql5.7+版本，支持动态字段功能。

可以在insert或update时自动创建不存在的字段。

动态字段功能基于mysql的json字段实现，没有ddl操作，不受运维制约。

封装sql模板功能，可以实现mybatis相同的功能。



project-demo项目为使用nh-micro框架的完整演示

同时也是脚本微服务的默认的开发平台和默认运行容器。

nh-micro框架可以与任何使用spring框架的技术栈兼容，

因此project-demo项目并不是必须的运行环境和开发技术栈环境。

project-demo项目，内置的登录，用户管理，角色管理，部门管理，菜单管理，字典管理等基础功能。

这些功能均使用groovy脚本实现，可以直接使用project-demo项目进行业务开发，也可以将nh-micro技术框架移植到开发人员自有项目中使用，使开发人员有更高的起点更关注业务实现，而不是重复进行基础功能实现。

后续会添加封装好的业务功能groovy脚本，方便快速支撑业务。


内置产品中心功能
基于nhmicro框架封装了产品中心功能：包括产品信息增删改查、阶梯利率设置、本金利息服务费算法实现、还款计划试算。
相关脚本有：
Micro_product_center_list.groovy，现实产品列表和产品信息增删改查功能。
Product_algo_repayplan.groovy，实现还款计划试算功能。
Product_algo_fuwufei_yicixing.groovy,实现服务费（一次性服务费）计算功能。
Product_algo_lixi_xxhb.groovy,实现利息(先息后本)计算功能。
产品中心需要表nh_micro_product_center_list

内置合同模板管理功能
可以将word模板转为pdf合同
在合同模板列表中创建记录，上传word（2003版）合同模板文件，点击编译后就可以下载pdf了。
在编写word文件是可以使用${somekey}作为占位符，下载pdf时输入json对占位符进行替换。

内置贷款进件管理功能
提交贷款进件、查看修改进件信息、上传附件、提交审批。

内置信审审核管理功能

内置互联网金融p2p理财功能和互联网页面

内置动态热部署数据源配置功能
内置动态热部署api服务功能
内置分库分表查询合并分页功能
内置分布式事务管理功能

nhmicro微服务框架开发技术说明
框架描述
封装统一的dao层（micro-db），业务逻辑在groovy中实现。
groovy脚本放置在groovy路径下，启动时自动加载。修改时自动热部署提高调试效率。
通过复用MicroMvcTemplate和MicroServiceTemplate，降低代码量提高开发效率。
采用NhEsbServiceServlet作为controller的，调用MicroMvcTemplate子类groovy脚本。
采用springmvc作为controller的，调用MicroServiceTemplate子类groovy脚本。
Java调用groovy或groovy间调用，使用GroovyExecUtil.execGroovyRetObj(groovy文件名,方法名,…)

1,
MicroMvcTemplate封装方法有
a,分页查询 getInfoList4Page
b,插入新记录 createInfo
c,更新记录updateInfo
d，删除记录delInfo
e，根据id查询记录getInfoById
f,不分页查询记录getInfoListAll

2,
MicroServiceTemplate封装方法有
a,分页查询getInfoList4PageService
b,创建记录createInfoService
c,更新记录updateInfoService
e,删除记录delInfoService
f，使用占位符拼装sql sqlTemplateService
还有其他重载方法可直接根据sql进行增删改查，分页操作。

3,通过MicroMvcTemplate和MicroServiceTemplate与数据库交互，输入输出map中的value都是string类型

4、不使用封装好模板，直接执行查询sql的方法是，
List infoList=(MicroMetaDao.getInstance()).queryObjJoinByCondition(sql);
或调用？占位的sql
public List<Map<String, Object>> queryObjJoinByCondition(String sql,Object[] paramArray)

直接写sql进行更新操作  int status=(MicroMetaDao.getInstance()).updateObjByCondition(sql)
或调用？占位的sql
updateObjByCondition(sql,paramArray)

MicroMetaDao()底层是jdbctemplate,有特别复杂的操作时可调用getMicroJdbcTemplate() 获取MicroMetaDao底层jdbctemplate

5、根据物理uuid查一条记录的封装
(microdb2.0以后版本支持表的id字段和bizid字段为数字)
在service层可使用
public Map getInfoByIdService(Map requestParamMap,String tableName)
public Map getInfoByIdService(String id,String tableName)

在dao层可使用queryObjJoinById
Map retMap=(MicroMetaDao.getInstance()).queryObjJoinById(tableName, id);

6、根据业务bizid查一条记录的封装
在service层可使用
public Map getInfoByBizIdService(String bizId,String tableName,String bizCol)

在dao层可以使用
Map rowMap=(MicroMetaDao.getInstance()).queryObjJoinByBizId("cms_template_list", bizid值,bizid在表中的列名称);

7、使用groovy mvc和service模板时，传入的map参数和返回的结果map的value都是string类型的
比如requestParamMap.put("update_time",DateTimeUtil.getNowStrTime());
日期字段，insert或update时，可以用"now()"小写的。如requestParamMap.put("update_time","now()");

8、groovy中获取springbean或全局变量
在groovy中调用获取springbean,  MicroContextHolder.getContext().getBean("xxxx springbeanid");
在groovy中获取配置的全局变量，MicroContextHolder.getContextMap().get("xxxx key");
MicroContextHolder需要在xml中配置，map中可以配置其他的全局变量
<bean class="com.minxin.micro.rule.engine.context.MicroContextHolder" lazy-init="false">
<property name="contextMap">
<map>

</map>
</property>
</bean> 

9、关于事务配置
microdb内部使用jdbctemplate，也使用spring事务进行配置
如果controller层使用springmvc，建议在controller层配置事务
如果使用NhEsbServiceServlet作为controller层如下配置在WsGroovyCmdHandler中开启事务
        <!--micro 声明式事物管理，配置事物管理advice-->
    <tx:advice id="txAdviceMicro" transaction-manager="txManager">
        <tx:attributes>
            <tx:method name="execHandler" propagation="REQUIRED"/>
        </tx:attributes>
    </tx:advice>

    <!--micro 配置事物管理advice作用范围与作用条件-->
    <aop:config>
        <aop:pointcut id="serviceLayerTransactionMicro" expression="execution( * com.project.frame.handler.*..*(..))"/>
        <aop:advisor pointcut-ref="serviceLayerTransactionMicro" advice-ref="txAdviceMicro"/>
    </aop:config>



或使用MicroServiceTemplateSupport中的execGroovyRetObjByDbTran方法。

groovy中遇到需要局部使用特殊事务控制时，使用显示编程事务实现。
TransactionTemplate transactionTemplate = (TransactionTemplate) MicroDbHolder.getDbSource("default_transaction");
transactionTemplate.execute(new TransactionCallback() {
	@Override
	public Object doInTransaction(TransactionStatus status) {
		xxxx
	}

});

10，支持like查询
将key从requestParamMap删除，拼装自定义where条件，调用service模板的方法
public Map getInfoList4PageService(Map requestParamMap,String tableName,Map pageMap,String cusWhere)
例如：
Map requestParamMap=getRequestParamMap(httpRequest);
		String noticeNameValue=requestParamMap.get("dbcol_ext_notice_name");
		requestParamMap.remove("dbcol_ext_notice_name");
		String cusWhere="";
		if(noticeNameValue!=null && !"".equals(noticeNameValue)){
			cusWhere="meta_content->>'\$.dbcol_ext_notice_name' like '%"+noticeNameValue+"%'";
		}
		Map pageMap=new HashMap();
		pageMap.put("page", page);
		pageMap.put("rows", rows);
		pageMap.put("sort", sort);
		pageMap.put("order", order);
		Map retMap=GroovyExecUtil.execGroovyRetObj("MicroServiceTemplate", "getInfoList4PageService",requestParamMap, tableName,pageMap,cusWhere);


11、如果使用了NhEsbServiceServlet作为controller层，groovy中返回自定义httpResponse时，
需要设置httpRequest.setAttribute("forwardFlag", "true");否则可能追加多余字段返回。
		HttpServletResponse httpResponse = gContextParam.getContextMap().get("httpResponse");
		httpResponse.getOutputStream().write(retStr.getBytes("UTF-8"));
		
		httpRequest.setAttribute("forwardFlag", "true");

12、增改查时都可以输入部分自定义sql
查询时（分页，不分页）
tableName可输入join连接字符串连接多个表
cusWhere可输入自定义的where条件sql串（可以和原功能叠加）
cusSelect可输入自定义select字符串
public  Map getInfoList4PageService(Map requestParamMap,String tableName,Map pageMap,String cusWhere,String cusSelect) 
public List getInfoListAllService(Map requestParamMap,String tableName,Map sortMap,String cusWhere,String cusSelect)
例如getInfoList4PageService(Map requestParamMap,"t_account a left join t_user u on a.user_code=u.user_code",Map pageMap,String cusWhere,"a.*,u.user_name")


插入时
cusCol可输入自定义的列字符串（可以和原功能叠加）
cusValue可输入自定义value字符串（可以和原功能叠加）
public Integer createInfoService(Map requestParamMap,String tableName,String cusCol,String cusValue) 
例如插入时记录数据库时间createInfoService(requestParamMap, tableName,"create_time","now()") 

更新时
cusCondition可输入自定义where条件sql串（可以和原功能叠加）
cusSetStr可输入自定义set字符串（可以和原功能叠加）
public Integer updateInfoService(Map requestParamMap,String tableName,String cusCondition,String cusSetStr)
例如更新时记录数据库时间updateInfoService(requestParamMap, tableName,cusCondition,"update_time=now()") 


13,MicroServiceTemplate中添加sqlTemplateService方法，用于做sql替换
替换的语法是velocity
判断是否为null #if(\${param.p1})
判断是否为"" #if(\${param.p1}!='')
判断不为null且不为"" #if(\$!{param.p1}!='')
例如以下的代码
		Map paramMap=new HashMap();
		paramMap.put("p1", "1");
		paramMap.put("p2", "2");
		paramMap.put("p3", "3");
		paramMap.put("p4", "4");
		paramMap.put("p5", "5");
		String sql=
			"select * from aaa where 1=1"+
				"#if(\${param.p1})"+
					" and c1='\${param.p2}'   "+
				"#end"+
				"#if(\${param.p2})"+
					" and c2= #sqlreplace(\${param.p2}) "+
				"#end"+
				"#if(\${param.p3})"+
					" and c3 like '%\${param.p3}%' "+
				"#end"
				;

		List placeList=new ArrayList();
		String retStr=sqlTemplateService(sql,paramMap,placeList);
		System.out.println(retStr);
		System.out.println(placeList);

返回如下的sql替换结果
select * from aaa where 1=1 and c1=1  true  and c2=? true and c3 like '%3%' true 
[2]

14，MicroServiceTemplate中生成序列号
public Integer getSeqByMysql(String seqKey)


15，MicroServiceTemplate中直接根据查询sql分页
不必拼装count查询sql
public Map getInfoList4PageServiceByMySql(String sql,Map pageMap) 




