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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='产品管理 用户登录';
