# Pecado

这是一个 Spring Cloud 微服务项目。

* Pecado-dp 开发平台
* Pecado-gateway 网关
* Pecado-ims 权限管理
* Pecado-system 系统管理
* Pecado-uaa 认证中心

一开始的主要目标是完成微服务框架下的自动化测试。

* 衍生自 [Paper](https://github.com/batizhao/paper)
* 关于测试的[说明](https://github.com/batizhao/pecado/blob/master/docs/test.md)

## 环境

* JDK 11
* MySQL8
* Maven
* Nacos 注册配置中心
* RocketMQ 异步消息
* Seata 分布式事务
* Sentinel 限流降级

## 快速开始

* 顺序启动 Nacos、RocketMQ、Seata
* 导入 Nacos 配置
* 执行  db/db.sql
* mvn clean install
* 启动 5 个微服务
* 启动  [pecado-ui](https://github.com/batizhao/pecado-ui)



