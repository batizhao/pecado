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

* 顺序启动 Nacos、~~RocketMQ~~、~~Seata~~
* 导入 Nacos 配置
* 执行  db/db.sql
* mvn clean install
* 加入 ```127.0.0.1  pecado-nacos``` 到 hosts
* 启动 5 个微服务
* 启动  [pecado-ui](https://github.com/batizhao/pecado-ui)

## 使用 ArgoCD 部署到 K8s

```sh 
$ docker build -t harbor.pecado.com/pecado/uaa:1.1 .
$ docker push harbor.pecado.com/pecado/uaa:1.1
$ kubectl port-forward svc/argocd-server -n argocd 8080:443

$ argocd app create pecado-uaa --repo git@github.com:batizhao/pecado.git --path pecado-uaa/helm --revision v1.1 --dest-server https://172.31.21.180:8443 --dest-namespace default

application 'pecado-uaa' created

$ argocd app get pecado-uaa
Name:               pecado-uaa
Project:            default
Server:             https://172.31.21.180:8443
Namespace:          default
URL:                https://localhost:8080/applications/pecado-uaa
Repo:               git@github.com:batizhao/pecado.git
Target:             v1.1
Path:               pecado-uaa
SyncWindow:         Sync Allowed
Sync Policy:        <none>
Sync Status:        Synced to v1.1 (55922aa)
Health Status:      Healthy

GROUP  KIND        NAMESPACE  NAME        STATUS  HEALTH   HOOK  MESSAGE
       Service     default    pecado-uaa  Synced  Healthy        service/pecado-uaa unchanged
apps   Deployment  default    pecado-uaa  Synced  Healthy        deployment.apps/pecado-uaa unchanged

$ argocd app sync guestbook
```





