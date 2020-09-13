## 简介
用于http转发，内网穿透。

类似工具：花生壳、ngrok、net123。
## 场景
外网想访问公司内网的http接口。暂仅支持http的postjson、get请求
## 模块介绍
fwd-common：

fwd-client：部署在内网，由其访问内网web服务

fwd-server：部署在外网，用户访问其代web，其将请求转发至fwd-client，达到穿透的目的
* urlReg：获取服务端凭证。client与server建立连接必须有正确的凭证；
* nettyweb：提供web服务。其代理转发所有个性化的url；
* nettyclient：接收处理client发送过来的数据；
  

web-demo：用于测试web的服务

## 原理
在fwd-server注册与内网的web映射，并获取凭证；
fwd-client根据凭证与fwd-server建立连接；
用户访问fwd-server代理的个性化url；
url请求流程：浏览器-》fwd-server-》fwd-client-》内网web；
url相应流程：内网web-》fwd-client-》fwd-server-》浏览器；

## 示例步骤
1. 启动fwd-server
2. 在fwd-server注册代理的内网web服务，获取凭证
http://localhost:28181/reg
{
    "clientWebServerPath": "http://localhost:9999",
    "proxyServerPath": "/tttt"
}
注意clientWebServerPath为完整前缀，含http；
3. 修改客户端的凭证，启动客户端
4. 访问http://localhost:28190/tttt/cfg将可访问到http://localhost:9999/cfg

## 待完善：
* fwd-server的nettyweb改用gateway、zuul做代理转发
  
