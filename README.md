# AWS 无服务架构 demo项目


##  一、AWS无服务架构

### AWS无服务器计算

无服务器计算是云原生架构，能够将更多的运营职责转移到AWS，从而提高灵活性和创新能力。无服务器计算可以在不考虑服务器的情况下构建并运行应用程序和服务。它消除了基础设施管理任务，例如服务器或集群配置、修补、操作系统维护和容量预置。


### 无服务器计算优势

* 无服务器管理 

   无需预置或维护任何服务器。无需安装、维护或管理任何软件或运行时。

* 灵活扩展  

  应用程序可自动扩展，或通过切换占用资源（如吞吐量、内存）的单位数（而不是切换单个服务器的单位数）来调整容量，从而实现扩展。

* 按价值付费 

  为一致的吞吐量或执行持续时间（而不是服务器单元）付费。

* 自动化的高可用性

  无服务器应用程序提供内置可用性和容错功能。无需构建这些功能，因为运行此应用程序的服务在默认情况下会提供这些功能。


### AWS Lambda 计算服务

利用 Lambda，不必预配置自己的实例；Lambda
会代执行所有的运行和管理活动，包括容量预配置、监控服务器队运行状况、向底层计算资源应用安全补丁、部署代码、在前端运行Web服务以及监控和记录代码。AWSLambda为代码提供轻松的扩展和高可用性，从而无需做额外努力。




### AWS Lambda 运行机制


利用容器重用来提高函数性能当请求达到一定峰值后，才会启动新的Lambda实例进行响应，如下图所示：

![image](https://github.com/dikers/serverless/blob/master/doc/picture/9.jpg?raw=true)
 所以在代码设计要整体考虑。限制变量/对象在每次调用时的重新初始化，而是使用静态初始化/构造函数、全局/静态变量以及单例。保持运行并重复使用连接(HTTP，数据库等)，它们在上次调用时建立。



## 二、 项目功能和技术架构介绍。

### 项目功能介绍

![image](https://github.com/dikers/serverless/blob/master/doc/picture/8.jpg?raw=true)


项目本身的功能比较简单， 通过用户输入的关键字，实时返回查询结果， 并显示在页面上。 [项目演示地址](http://dikers.de)



### 项目架构设计

![image](http://)  




####  用AWS S3托管静态网站

前端用jquery 做静态页面 ， 将静态页面上次到AWS S3上， 设置S3 为托管网站，
再使用AWS CloudFront 做CDN内容分发， 提高网站访问速度。


####  Api Gateway 做数据转发
静态页面发出的动态请求，会通过Api Gateway做负载均衡然后转发给Lambda服务器,
需要配置安全组，已经访问策略， 只有来自Api Gateway能访问Lambda的指定端口。
同时可以配置 AWS WAF （web application firewall ） AWS Shield 和 AWS
Firewall Manager 来保护系统遭受网络攻击。
[官网介绍](https://console.aws.amazon.com/waf/home?region=us-east-1#/intro)



####  Lambda 做数据处理
Service 层用java 实现数据计算和处理， 实现AWS Lambda接口函数，
接受用户输入参数，然后去数据库中进行查询，并将数据返回。
[Lambda 入门指南](https://docs.aws.amazon.com/zh_cn/lambda/latest/dg/getting-started.html)

[代码说明](https://github.com/dikers/serverless/tree/master/src/main/java/example)

####  RDS 做数据存储

使用AWS RDS mysql 作为数据存储单元， 数据库需要在多可用区做备份，
同时可以根据负责情况做读写分离，以提高吞吐量。 或者可以使用 AWS Aurora
来替换Mysql数据， Aurora兼容mysql， 同时访问速度达到了mysql 的5倍，
可以用更低的费用，达到更高的吞吐量。
[Aurora 官方介绍](https://aws.amazon.com/cn/rds/aurora/?nc2=h_m1)

[初始化sql](https://github.com/dikers/serverless/tree/master/db)


####  AWS Glacier 做数据定期的归档
Amazon S3 Glacier
是一款安全、持久且成本极低的云存储服务，适用于数据存档和长期备份。它能够提供
99.999999999% 的持久性以及全面的安全与合规功能，可以帮助满足最严格的监管要求。
在项目中， 会用Glacier来保存数据库快照和日志记录。
[AWS Glacier官方介绍](https://aws.amazon.com/cn/glacier/?nc2=h_m1)






## 三、 项目实施步骤


### 1. 开通AWS 账号

直接到[亚马逊注册中心](https://portal.aws.amazon.com/billing/signup?redirect_url=https%3A%2F%2Faws.amazon.com%2Fregistration-confirmation&language=zh_cn#/start)开通账号，
包含12个月的免费套餐，可以用来学习。 需要绑定信用卡，
**注意不需要使用服务的时候，及时关闭资源，避免产生不必要的费用。**
建议设置计费警告， 当超过免费套餐额度的时候， 会收到邮件通知。
 

### 2. 申请域名， 使用Route53解析


Amazon Route 53 是一种可用性高、可扩展性强的云域名系统 (DNS) Web 服务.

首先需要[注册域名](https://docs.aws.amazon.com/zh_cn/Route53/latest/DeveloperGuide/domain-register.html)
**需要一些费用** 

申请的域名如下：例如 example.com 和子域 www.example.com



### 3. 开通 S3, 进行静态内容托管

[托管静态网站 -- 官方教程](https://aws.amazon.com/cn/getting-started/projects/host-static-website/?c_1)

![image](https://d1.awsstatic.com/Projects/v1/AWS_StaticWebsiteHosting_Architecture_4b.da7f28eb4f76da574c98a8b2898af8f5d3150e48.png)

*   需要创建三个存储桶（s3）。 
    1.  一个存储桶包含内容。
    2.  一个存储桶用来重定向请求。
    3.  一个用来保存日志文件。

*   创建CloudFront 做内容分发， 加快各地区的访问速度。 

*   在Route53 中添加两个record， 用来做路由转发。 

   1.  example.com 存储桶包含内容
   2.  www.example.com 用来重定向请求 

     在下图所在页面对S3 进行路由的配置。

![image](https://github.com/dikers/serverless/blob/master/doc/picture/10.jpg?raw=true)


*  存储桶的公有访问设置，需要能修改访问策略，否则修改公有访问权限会修改不成功，见下图。 

![image](https://github.com/dikers/serverless/blob/master/doc/picture/13.jpg?raw=true) 

* 设置访问策略,注意要设置存储桶的访问权限为公有. **example.com**
  修改成自己存储桶的名称。下面是示例代码：
```
{
    "Version": "2012-10-17",
    "Id": "Policy1553876871392",
    "Statement": [
        {
            "Sid": "Stmt1553876863732",
            "Effect": "Allow",
            "Principal": "*",
            "Action": "s3:GetObject",
            "Resource": "arn:aws:s3:::[example.com]/*"
        }
    ]
}
```
![image](https://github.com/dikers/serverless/blob/master/doc/picture/11.jpg?raw=true) 


*  设置完成以后，需要用Route53 做流量转发

![image](https://github.com/dikers/serverless/blob/master/doc/picture/22.jpg?raw=true) 



### 4. 申请RDS 数据库

[申请RDS mysql数据库 - 官方教程](https://aws.amazon.com/cn/getting-started/tutorials/create-mysql-db/)

* 可以设置多可用区域部署， 提供系统可用写，
  RDS会从一个可用区，同步复制到另外一个可用区里，当主库不用时，可以自动切换到从库。
  见下图中配置：
  ![image](https://github.com/dikers/serverless/blob/master/doc/picture/27.jpg?raw=true)

* 在生产环境中需要设置安全组，只有Lambda服务所在的安全组，才有权限读写数据库。

* 为了提供访问性能， 可以设置只读副本， 写示例可以异步备份数据到读示例上 。

* 设置AWS IAM 用户和角色来管理数据库用户凭证。

* 设置访问策略， 只有Lambda服务所在的网段 和堡垒机所在的网段 可以访问数据库，
  详细配置见下图：
  ![image](https://github.com/dikers/serverless/blob/master/doc/picture/26.jpg?raw=true)


 

### 5. 部署Lambda服务

[Lambda入门指南 - 官方教程](https://docs.aws.amazon.com/zh_cn/lambda/latest/dg/getting-started.html)

*  需要编写lambda的接口函数, 代码示例如下： 
```
package example;

import com.amazonaws.services.lambda.runtime.Context; 
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Hello implements RequestHandler<Integer, String>{
    public String myHandler(int myCount, Context context) {
        return String.valueOf(myCount);
    }
}

```  
* 服务端会调用  **example.Hello::myHandler**  进行调用


* 给lambda 函数添加触发器 

   这里选择 Api Gateway进行关联， 把来自S3托管网页的ajax 请求，转发给Lambda
   进行处理。
   
![image](https://github.com/dikers/serverless/blob/master/doc/picture/25.jpg?raw=true)

* 环境变量的配置

将AWS 上的数据库配置信息加密保存到AWS上， 与代码分离。 通过代码读取。
同时需要选择加密的方式,将环境变量加密后保存到AWS上。
![image](https://github.com/dikers/serverless/blob/master/doc/picture/28.jpg?raw=true)

* 网络安全设置

本项目的lambda函数需要外部 Internet 访问，确保安全组允许出站连接并且 VPC 具有
NAT 网关, 能让外部用户访问到。详细配置见下图：
![image](https://github.com/dikers/serverless/blob/master/doc/picture/23.jpg?raw=true)




### 6. 部署Api Gateway, 接入到lambda 接口上。

### 7. 配置VPC， 设置安全组和安全策略


### 8. TODO 添加 DevOps 管理开发流程

    使用 CodeBuild , CodeDeploy, CodePipeline 等工具，打造DevOps 工作模式。
    
### 9. TODO 使用CloudFormation 生成基础实施的模板文件

    基础设施及代码 (Infrastructure as Code), 可以代码的方式对基础设施进行管理， 
    可以很方便的自动化构建多种基础环境，方便开发，测试，以及灾后恢复。 


















