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
