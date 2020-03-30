# blog
一个基于SpringBoot+Spring JPA+Bootstrap的博客项目  
## 项目构建工具
gradle4.7
## SpringBoot版本
springBootVersion=2.0.1.RELEASE
## Mysql数据库
Mysql数据库5.5及以上  
sql脚本在/resources/sql下,第一次运行需要在配置文件中将这两行取消注释  
`spring.datasource.data=classpath:sql/import.sql`  
`spring.datasource.initialization-mode=always`  
之后将配置文件中的  
`spring.jpa.hibernate.ddl=create改为update`
## ElasticSearch配置
#### 版本5.6.8及以上
在application.yml中将ES配置为自己的
## 涉及到的文件上传服务器
[文件服务器地址](https://github.com/yidou120/mongodb-file-server)
## 存在的bug  
- 首页的分页只能显示一页
- 博主的个人主页 最热栏目点击无效果
## 未完成的功能
- 编写博客中上传图片功能
- 博客显示页面右侧相关博客展示列表功能