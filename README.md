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




数据库创建
以下是推荐使用mysql5.7版支持json的建表sql


-- ----------------------------
-- Table structure for nh_micro_dept
-- ----------------------------
DROP TABLE IF EXISTS `nh_micro_dept`;
CREATE TABLE `nh_micro_dept` (
  `id` varchar(50) NOT NULL,
  `meta_key` varchar(50) DEFAULT NULL COMMENT '元数据标识',
  `meta_name` varchar(100) DEFAULT NULL COMMENT '元数据名称',
  `meta_type` varchar(100) DEFAULT NULL COMMENT '元数据类型',
  `meta_content` json DEFAULT NULL COMMENT '元数据内容',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门';

-- ----------------------------
-- Table structure for nh_micro_dict_items
-- ----------------------------
DROP TABLE IF EXISTS `nh_micro_dict_items`;
CREATE TABLE `nh_micro_dict_items` (
  `id` varchar(50) NOT NULL,
  `meta_key` varchar(50) DEFAULT NULL,
  `meta_name` varchar(100) DEFAULT NULL COMMENT '名称',
  `meta_type` varchar(100) DEFAULT NULL COMMENT '元数据类型',
  `meta_content` json DEFAULT NULL COMMENT '内容',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `meta_key` (`meta_key`,`meta_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典项';

-- ----------------------------
-- Table structure for nh_micro_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `nh_micro_dictionary`;
CREATE TABLE `nh_micro_dictionary` (
  `id` varchar(50) NOT NULL,
  `meta_key` varchar(50) DEFAULT NULL,
  `meta_name` varchar(100) DEFAULT NULL COMMENT '名称',
  `meta_type` varchar(100) DEFAULT NULL COMMENT '元数据类型',
  `meta_content` json DEFAULT NULL COMMENT '内容',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `meta_key` (`meta_key`,`meta_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='字典';


-- ----------------------------
-- Table structure for nh_micro_menu
-- ----------------------------
DROP TABLE IF EXISTS `nh_micro_menu`;
CREATE TABLE `nh_micro_menu` (
  `id` varchar(50) NOT NULL,
  `meta_key` varchar(50) DEFAULT NULL COMMENT '元数据标识',
  `meta_name` varchar(100) DEFAULT NULL COMMENT '元数据名称',
  `meta_type` varchar(100) DEFAULT NULL COMMENT '元数据类型',
  `meta_content` json DEFAULT NULL COMMENT '元数据内容',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单管理';

-- ----------------------------
-- Table structure for nh_micro_ref_menu_role
-- ----------------------------
DROP TABLE IF EXISTS `nh_micro_ref_menu_role`;
CREATE TABLE `nh_micro_ref_menu_role` (
  `id` varchar(50) NOT NULL,
  `meta_key` varchar(50) DEFAULT NULL COMMENT '元数据标识',
  `meta_name` varchar(100) DEFAULT NULL COMMENT '元数据名称',
  `meta_type` varchar(100) DEFAULT NULL COMMENT '元数据类型',
  `meta_content` json DEFAULT NULL COMMENT '元数据内容',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户菜单关联';

-- ----------------------------
-- Table structure for nh_micro_ref_user_dept
-- ----------------------------
DROP TABLE IF EXISTS `nh_micro_ref_user_dept`;
CREATE TABLE `nh_micro_ref_user_dept` (
  `id` varchar(50) NOT NULL,
  `meta_key` varchar(50) DEFAULT NULL COMMENT '元数据标识',
  `meta_name` varchar(100) DEFAULT NULL COMMENT '元数据名称',
  `meta_type` varchar(100) DEFAULT NULL COMMENT '元数据类型',
  `meta_content` json DEFAULT NULL COMMENT '元数据内容',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户部门关联';

-- ----------------------------
-- Table structure for nh_micro_ref_user_role
-- ----------------------------
DROP TABLE IF EXISTS `nh_micro_ref_user_role`;
CREATE TABLE `nh_micro_ref_user_role` (
  `id` varchar(50) NOT NULL,
  `meta_key` varchar(50) DEFAULT NULL COMMENT '元数据标识',
  `meta_name` varchar(100) DEFAULT NULL COMMENT '元数据名称',
  `meta_type` varchar(100) DEFAULT NULL COMMENT '元数据类型',
  `meta_content` json DEFAULT NULL COMMENT '元数据内容',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关联';

-- ----------------------------
-- Table structure for nh_micro_role
-- ----------------------------
DROP TABLE IF EXISTS `nh_micro_role`;
CREATE TABLE `nh_micro_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `meta_key` varchar(50) DEFAULT NULL COMMENT '元数据标识',
  `meta_name` varchar(100) DEFAULT NULL COMMENT '元数据名称',
  `meta_type` varchar(100) DEFAULT NULL COMMENT '元数据类型',
  `meta_content` json DEFAULT NULL COMMENT '元数据内容',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='角色管理';


-- ----------------------------
-- Table structure for nh_micro_user
-- ----------------------------
DROP TABLE IF EXISTS `nh_micro_user`;
CREATE TABLE `nh_micro_user` (
  `id` varchar(50) NOT NULL,
  `meta_key` varchar(50) DEFAULT NULL COMMENT '元数据标识',
  `meta_name` varchar(100) DEFAULT NULL COMMENT '元数据名称',
  `meta_type` varchar(100) DEFAULT NULL COMMENT '元数据类型',
  `meta_content` json DEFAULT NULL COMMENT '元数据内容',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户管理';

-- ----------------------------
-- Table structure for nh_micro_product_center_list 
-- ----------------------------
DROP TABLE IF EXISTS `nh_micro_product_center_list`;
CREATE TABLE `nh_micro_product_center_list` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `meta_key` varchar(50) DEFAULT NULL COMMENT '元数据标识（预留字段）',
  `meta_name` varchar(100) DEFAULT NULL COMMENT '元数据名称',
  `meta_type` varchar(100) DEFAULT NULL COMMENT '元数据类型',
  `meta_content` json DEFAULT NULL COMMENT '元数据内容',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8


-- ----------------------------
-- Table structure for 合同模板管理 
-- ----------------------------
DROP TABLE IF EXISTS `contract_temp_list`;
CREATE TABLE `contract_temp_list` (
  `id` varchar(50) NOT NULL,
  `meta_key` varchar(50) DEFAULT NULL COMMENT '元数据标识（预留字段）',
  `meta_name` varchar(100) DEFAULT NULL COMMENT '元数据名称',
  `meta_type` varchar(100) DEFAULT NULL COMMENT '元数据类型',
  `meta_content` json DEFAULT NULL COMMENT '元数据内容',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `html_text` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='合同模板管理';

-- ----------------------------
-- Table structure for 进件管理 
-- ----------------------------
DROP TABLE IF EXISTS `bizflow_intopiece_list`;
CREATE TABLE `bizflow_intopiece_list` (
  `id` varchar(50) NOT NULL,
  `meta_key` varchar(50) DEFAULT NULL COMMENT '元数据标识（预留字段）',
  `meta_name` varchar(100) DEFAULT NULL COMMENT '元数据名称',
  `meta_type` varchar(100) DEFAULT NULL COMMENT '元数据类型',
  `meta_content` json DEFAULT NULL COMMENT '元数据内容',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='进件管理';


-- ----------------------------
-- Table structure for 信审管理 
-- ----------------------------
DROP TABLE IF EXISTS `bizflow_creditaudit_list`;
CREATE TABLE `bizflow_creditaudit_list` (
  `id` varchar(50) NOT NULL,
  `meta_key` varchar(50) DEFAULT NULL COMMENT '元数据标识（预留字段）',
  `meta_name` varchar(100) DEFAULT NULL COMMENT '元数据名称',
  `meta_type` varchar(100) DEFAULT NULL COMMENT '元数据类型',
  `meta_content` json DEFAULT NULL COMMENT '元数据内容',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='信审管理';


-- ----------------------------
-- Table structure for 账户管理 
-- ----------------------------
DROP TABLE IF EXISTS `t_front_account`;
CREATE TABLE `t_front_account` (
  `id` varchar(50) NOT NULL,
  `meta_key` varchar(50) DEFAULT NULL COMMENT '元数据标识（预留字段）',
  `meta_name` varchar(100) DEFAULT NULL COMMENT '元数据名称',
  `meta_type` varchar(100) DEFAULT NULL COMMENT '元数据类型',
  `meta_content` json DEFAULT NULL COMMENT '元数据内容',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间（注册日期）',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `user_code` varchar(36) DEFAULT NULL COMMENT '用户编码',
  `accumulated_income` decimal(20,2) DEFAULT '0.00' COMMENT '累计收益',
  `principal_received` decimal(20,2) DEFAULT '0.00' COMMENT '待收本金',
  `interest_received` decimal(20,2) DEFAULT '0.00' COMMENT '待收利息',
  `frozen_amount` decimal(20,2) DEFAULT '0.00' COMMENT '冻结金额',
  `total_investment` decimal(20,2) DEFAULT '0.00' COMMENT '投资总额',
  `available_balance` decimal(20,2) DEFAULT '0.00' COMMENT '可用余额',
  `account_type` varchar(2) DEFAULT NULL COMMENT '账户类型1、个人2、公司',
  `cumulative_rincipal` decimal(20,2) DEFAULT '0.00' COMMENT '累计收回本金',
  `data_version` int(11) DEFAULT '0' COMMENT '数据版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账户表';

-- ----------------------------
-- Table structure for 投资信息管理 
-- ----------------------------
DROP TABLE IF EXISTS `t_front_invest`;
CREATE TABLE `t_front_invest` (
  `id` varchar(50) NOT NULL,
  `order_number` varchar(50) DEFAULT NULL COMMENT '订单号',
  `user_code` varchar(50) DEFAULT NULL COMMENT '客户编码',
  `user_name` varchar(50) DEFAULT NULL COMMENT '客户姓名',
  `bid_name` varchar(18) DEFAULT NULL COMMENT '标的名称',
  `bid_code` varchar(18) DEFAULT NULL COMMENT '标的id',
  `product_name` varchar(50) DEFAULT NULL COMMENT '产品名称',
  `product_code` varchar(20) DEFAULT NULL COMMENT '产品编号',
  `invest_amount` decimal(20,2) DEFAULT NULL COMMENT '投资金额',
  `invest_type` varchar(50) DEFAULT NULL COMMENT '投资类型:0.首次投资 1.追加投资 2.续投',
  `trade_status` int(2) unsigned zerofill DEFAULT NULL COMMENT '交易状态:0.购买失败 1.购买成功  3.支付中 4.待支付，5持有中（收益中）,6回款成功',
  `repay_progress` varchar(50) DEFAULT NULL COMMENT '还款进度',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `account_id` varchar(50) DEFAULT NULL COMMENT '账户信息id',
  `expire_profit` decimal(20,2) DEFAULT NULL COMMENT '到期收益',
  `create_time` varchar(19) DEFAULT NULL COMMENT '创建时间',
  `interrest_date` varchar(19) DEFAULT NULL COMMENT '计息日',
  `trade_end_date` varchar(19) DEFAULT NULL COMMENT '交易到期日',
  `back_amount` decimal(20,2) DEFAULT NULL COMMENT '到期回款金额',
  `payment_no` varchar(100) DEFAULT NULL COMMENT '支付交易流水号,还款给投资人时需要',
  `source_id` varchar(20) DEFAULT NULL COMMENT '购买平台  1、pc  2、android  3、ios  4、h5',
  `reback_date` varchar(19) DEFAULT NULL COMMENT '回款日',
  `isgenerate_pack` varchar(5) DEFAULT NULL COMMENT '是否已经生成电子合同，‘0’ 否 ‘1’ 是',
  `order_rate` double(8,6) DEFAULT NULL COMMENT '订单利率',
  `freeze_status` int(2) DEFAULT NULL COMMENT '冻结状态：0-购买冻结 1-购买成功解冻 2-购买失败返还',
  `success_date` varchar(19) DEFAULT NULL COMMENT '支付成功时间',
  `periods` varchar(19) DEFAULT NULL COMMENT '期限',
  `cycle_type` varchar(5) DEFAULT NULL COMMENT '''周期类型:0.月 1.天 2.固定到期日 3.年'',',
  `account_pay` decimal(20,2) DEFAULT NULL COMMENT '账户余额支付金额',
  `bank_pay` decimal(20,2) DEFAULT NULL COMMENT '银行支付金额',
  `contract_url` varchar(300) DEFAULT NULL COMMENT '电子合同url地址',
  `real_profit` decimal(20,2) DEFAULT '0.00' COMMENT '实际收益',
  `real_back_amount` decimal(20,2) DEFAULT '0.00' COMMENT '实际回款金额',
  `flag` varchar(2) DEFAULT '0' COMMENT '数据状态:0.未抓取 1.已抓取 2.已返回 3.已推送',
  `increment_id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`increment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=441 DEFAULT CHARSET=utf8 COMMENT='客户投资信息表';



-- ----------------------------
-- Table structure for 交易记录管理 
-- ----------------------------
DROP TABLE IF EXISTS `t_front_recharge`;
CREATE TABLE `t_front_recharge` (
  `id` varchar(50) NOT NULL,
  `meta_key` varchar(50) DEFAULT NULL,
  `meta_name` varchar(100) DEFAULT NULL COMMENT '名称',
  `meta_type` varchar(100) DEFAULT NULL COMMENT '元数据类型',
  `meta_content` json DEFAULT NULL COMMENT '内容',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `recharge_money` decimal(20,2) DEFAULT '0.00' COMMENT '交易金额',
  `recharge_user_code` varchar(36) DEFAULT NULL COMMENT '客户编号',
  `recharge_type` varchar(2) DEFAULT NULL COMMENT '交易类型（1充值2提现3投资4回款5手续费6红包）',
  `recharge_status` varchar(2) DEFAULT NULL COMMENT '交易状态（1处理中2成功3失败）',
  `account_balance` decimal(20,2) DEFAULT '0.00' COMMENT '账户余额',
  `inner_recharge_number` varchar(50) DEFAULT NULL COMMENT '内部交易号',
  `thirdparty_recharge_number` varchar(50) DEFAULT NULL COMMENT '第三方交易号',
  `pay_way` varchar(2) DEFAULT NULL COMMENT '支付方式（1快捷支付2网银支付3账户余额）',
  `approve_time` datetime DEFAULT NULL COMMENT '审核时间',
  `success_back_time` datetime DEFAULT NULL COMMENT '成功返回时间',
  `thirdparty_code_syn` varchar(10) DEFAULT NULL COMMENT '支付平台同步返回的消息状态码',
  `thirdparty_msg__syn` varchar(200) DEFAULT NULL COMMENT '支付平台同步返回的状态信息',
  `thirdparty_code_aysn` varchar(10) DEFAULT NULL COMMENT '支付平台异步回调返回的状态信息',
  `thirdparty_msg_aysn` varchar(200) DEFAULT NULL COMMENT '支付平台异步调用返回的状态信息',
  `source` char(1) DEFAULT NULL COMMENT '渠道 1、pc  2、android  3、ios  4、h5',
  PRIMARY KEY (`id`),
  UNIQUE KEY `meta_key` (`meta_key`,`meta_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资金管理 交易记录表';

-- ----------------------------
-- Table structure for p2p产品管理 
-- ----------------------------
DROP TABLE IF EXISTS `t_front_product`;
CREATE TABLE `t_front_product` (
  `id` varchar(50) DEFAULT '',
  `product_code` varchar(20) NOT NULL COMMENT '产品编码',
  `product_type` varchar(2) DEFAULT '1' COMMENT '产品分类 1 保理 2 金所',
  `product_name` varchar(50) DEFAULT NULL COMMENT '产品名称',
  `periods` varchar(19) DEFAULT NULL COMMENT '期数',
  `start_invest_money` decimal(20,2) DEFAULT NULL COMMENT '起投金额',
  `years_income` varchar(20) DEFAULT NULL COMMENT '年化收益',
  `product_url` varchar(2000) DEFAULT NULL COMMENT '产品详情url',
  `stepping` decimal(20,2) DEFAULT NULL COMMENT '步进',
  `cycle_type` varchar(5) DEFAULT NULL COMMENT '周期类型:0.月 1.天 2.固定到期日 3.年',
  `create_time` varchar(19) DEFAULT NULL COMMENT '创建时间',
  `create_name` varchar(50) DEFAULT NULL COMMENT '创建人名称',
  `create_id` bigint(12) DEFAULT NULL COMMENT '创建人id',
  `interrest_mode` varchar(10) DEFAULT NULL COMMENT '起息规则方式 INSU 投资成功计息 FSSU 满标计息',
  `interrest_date` int(11) DEFAULT NULL COMMENT '起息规则 n 表示t+n',
  `per_investment` decimal(20,2) unsigned zerofill DEFAULT NULL COMMENT '单笔最高金额',
  `product_cycle` varchar(8) DEFAULT NULL COMMENT '封闭期',
  `prod_alias` varchar(500) DEFAULT NULL COMMENT '产品别名',
  `repayment_mode` varchar(10) DEFAULT NULL COMMENT '还款方式 DQBX：到期本息 XXHB：先息后本',
  `product_state` varchar(3) DEFAULT NULL COMMENT '产品状态 1：正常 2：失败',
  `page_code` varchar(50) DEFAULT NULL COMMENT '页面参数模板id',
  `model_code` varchar(50) DEFAULT NULL COMMENT '模板编码id',
  `model_name` varchar(50) DEFAULT NULL COMMENT '模板名称',
  `product_amount` decimal(20,2) DEFAULT NULL COMMENT '项目金额',
  `collect_start_time` varchar(19) DEFAULT NULL COMMENT '募集开始时间',
  `collect_end_time` varchar(19) DEFAULT NULL COMMENT '募集结束时间',
  `pay_type` varchar(10) DEFAULT NULL COMMENT '支付方式 OPAY 在线支付 OAPP 在线预约',
  `pay_bus_id` varchar(20) DEFAULT NULL COMMENT '支付主体 JZGK:金置高科',
  `is_stair_rate` varchar(2) DEFAULT 'N' COMMENT '是否使用阶梯利率  Y：使用 N：不使用',
  `increment_id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`increment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=160 DEFAULT CHARSET=utf8 COMMENT='产品信息表';

