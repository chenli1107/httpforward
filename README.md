## 简介
用于http转发。
## 场景
外网想访问公司内网的http接口。
## 模块介绍
fwd-common：
fwd-client：用于访问真实的web服务。需配置fwd-server地址、真实web服务在fwd-server中的映射关系
fwd-server：提供web服务，通过注册的web映射关系转换，将http请求转发给fwd-client，由fwd-client访问，最终将访问结果返回给用户
web-demo：用于测试web的服务

## 原理
fwd-client向fwd-server注册web映射，将访问fwd-server的http转换为真实的地址，并发送到fwd-client，由fwd-client访问真实地址，并将结果发送回fwd-server，再输出给用户
