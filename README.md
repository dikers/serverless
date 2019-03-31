# AWS 无服务架构 -Demo项目


##  一、AWS无服务架构介绍

### AWS无服务器计算

无服务器计算是云原生架构，能够将更多的运营职责转移到AWS，从而提高灵活性和创新能力。无服务器计算可以在不考虑服务器的情况下构建并运行应用程序和服务。它消除了基础设施管理任务，例如服务器或集群配置、修补、操作系统维护和容量预置。


### 无服务器计算优势

* **无服务器管理** 

  无需预置或维护任何服务器。无需安装、维护或管理任何软件或系统。

* **灵活扩展 ** 

  应用程序可自动扩展，或通过切换占用资源（如吞吐量、内存）的单位数（而不是切换单个服务器的单位数）来调整容量，从而实现扩展。

* **按使用量付费** 

  为实际的吞吐量或执行持续时间付费，而不是服务器数量付费。

* **自动化的高可用性**

  无服务器应用程序提供内置可用性和容错功能。无需构建这些功能，因为运行此应用程序的服务在默认情况下会提供这些功能。


### AWS Lambda 计算服务

利用Lambda，不必预配置自己的实例；Lambda会代执行所有的运行和管理活动，包括容量预配置、监控服务器队运行状况、向底层计算资源应用安全补丁、部署代码、在前端运行Web服务以及监控和记录代码。AWSLambda为代码提供轻松的扩展和高可用性，从而无需做额外努力。

### AWS Lambda 运行机制

利用容器重用来提高函数性能当请求达到一定峰值后，才会启动新的Lambda实例进行响应，如下图所示：

![image](https://github.com/dikers/serverless/blob/master/doc/picture/9.jpg?raw=true)


所以在代码设计要整体考虑。限制变量/对象在每次调用时的重新初始化，而是使用静态初始化/构造函数、全局/静态变量以及单例。保持运行并重复使用连接(HTTP，数据库等)，它们在上次调用时建立。



## 二、 项目功能和技术架构介绍。

### 项目功能介绍

![image](https://github.com/dikers/serverless/blob/master/doc/picture/8.jpg?raw=true)


项目本身的功能比较简单， 通过用户输入的AWS产品的英文关键字，实时返回查询结果，
并显示在页面上。 

[项目演示地址](http://dikers.de)



### 项目架构设计


####  官方示例

![image](https://d1.awsstatic-china.com/product-marketing/Lambda/Diagrams/product-page-diagram_Lambda-WebApplications%202.c7f8cf38e12cb1daae9965ca048e10d676094dc1.png)  

*  用AWS S3 托管静态内容
*  用户点访问网站，会把请求发送到Api Gateway上
*  Api Gateway 会触发Lambda服务
*  Lambda服务在访问数据库， 进行数据处理


#### 本项目架构图


![image](https://github.com/dikers/serverless/blob/master/doc/picture/7.png?raw=true)

#### Route 53 做路由转发

##### 设置了以下三个路由规则
*  example.com
*  www.example.com
*  lambda.example.com    

####  用AWS S3托管静态网站

前端用jquery 做静态页面 ， 将静态页面上次到AWS S3上， 设置S3 为托管网站，
再使用AWS CloudFront 做CDN内容分发， 提高网站访问速度。 

##### 设置了三个储存桶
* 放网站静态资源
* 做转发
* 存放log日志和一些项目资源



####  Api Gateway 做数据转发

Amazon API Gateway
是一种完全托管的服务，可以帮助开发者轻松创建、发布、维护、监控和保护任意规模的
API。只需在 AWS 管理控制台中轻点几次鼠标，就能创建用作应用程序“前门”的 REST 和
WebSocket API，以便访问数据、业务逻辑或后端服务的功能，如在 Amazon Elastic
Compute Cloud (Amazon EC2) 上运行的工作负载、在 AWS Lambda 上运行的代码、任意
Web 应用程序或实时通信应用程序。

[官方介绍文档](https://docs.aws.amazon.com/zh_cn/apigateway/latest/developerguide/getting-started.html)

![image](https://d1.awsstatic-china.com/serverless/New-API-GW-Diagram.c9fc9835d2a9aa00ef90d0ddc4c6402a2536de0d.png)



本项目中， 从静态页面发出的动态请求，会通过Api Gateway转发给Lambda服务器,
需要配置安全组，以及访问策略， 只有来自Api Gateway的请求能访问Lambda服务。
同时可以配置 AWS WAF （web application firewall ） AWS Shield 和 AWS
Firewall Manager 来保护系统遭受网络攻击。
[WAF Shield官网介绍](https://console.aws.amazon.com/waf/home?region=us-east-1#/intro)



####  Lambda 做数据处理

Lambda 服务用java实现， 做数据计算和处理， 实现AWS Lambda接口函数，
接受用户输入参数，然后进行数据库查询，并将数据返回。

[Lambda 入门指南](https://docs.aws.amazon.com/zh_cn/lambda/latest/dg/getting-started.html)
---[本项目JAVA代码说明](https://github.com/dikers/serverless/tree/master/src/main/java/example)


####  RDS 做数据存储

使用AWS RDS mysql 作为数据存储单元， 数据库需要在多可用区做备份，
同时可以根据负载情况新加只读实例，以提高吞吐量。 或者可以使用 AWS Aurora
来替换Mysql数据库， Aurora兼容mysql， 同时访问速度达到了mysql 的5倍，
可以用更低的费用，达到更高的吞吐量和更好的稳定性。

[Aurora 官方介绍](https://aws.amazon.com/cn/rds/aurora/?nc2=h_m1)---[初始化sql脚本](https://github.com/dikers/serverless/tree/master/db)


####  AWS Glacier 做数据定期的归档
Amazon S3 Glacier
是一款安全、持久且成本极低的云存储服务，适用于数据存档和长期备份。它能够提供
99.999999999% 的持久性以及全面的安全与合规功能，可以帮助满足最严格的监管要求。
在项目中， 会用Glacier来保存数据库快照和日志记录。
[AWS Glacier官方介绍](https://aws.amazon.com/cn/glacier/?nc2=h_m1)





## 三、 项目实施步骤


### 1. 开通AWS 账号

访问[亚马逊注册中心](https://portal.aws.amazon.com/billing/signup?redirect_url=https%3A%2F%2Faws.amazon.com%2Fregistration-confirmation&language=zh_cn#/start)开通账号，
包含12个月的免费套餐，可以用来学习。 需要绑定信用卡，
**注意不需要使用服务的时候，及时关闭资源，避免产生不必要的费用。**
建议设置计费警告， 当超过免费套餐额度的时候， 会收到邮件通知。
 

### 2. 申请域名， 使用Route53解析


Amazon Route 53 是一种可用性高、可扩展性强的云域名系统 (DNS) Web 服务。

首先需要[注册域名](https://docs.aws.amazon.com/zh_cn/Route53/latest/DeveloperGuide/domain-register.html)
**需要一些费用** 

申请的域名如下：例如 example.com 和子域 www.example.com



### 3. 开通 S3, 进行静态内容托管

[托管静态网站 -- 官方教程](https://aws.amazon.com/cn/getting-started/projects/host-static-website/?c_1)

![image](http://d1.awsstatic.com/Projects/v1/AWS_StaticWebsiteHosting_Architecture_4b.da7f28eb4f76da574c98a8b2898af8f5d3150e48.png)

*   需要创建三个存储桶（s3） 

      一个存储桶包含内容 
      一个存储桶用来重定向请求 
      一个用来保存日志文件

*   创建CloudFront 做内容分发， 加快各地区的访问速度。 

*   在Route53 中添加两个record， 用来做路由转发。 

     example.com 存储桶包含内容 
     www.example.com 用来重定向请求


*   在下图所在页面对S3 进行路由的配置

![image](https://github.com/dikers/serverless/blob/master/doc/picture/10.jpg?raw=true)



*  存储桶的公有访问设置，需要能修改访问策略，否则修改公有访问权限修改不会成功，见下图：



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


*  设置完成以后，需要用Route53 设置转发规则


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

//用户输入一个Integer 做为请求参数， 函数返回一个String值。 

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



#### 第一步 创建Api

主要是设置api 名称和类型， 如下图所示：

![image](https://github.com/dikers/serverless/blob/master/doc/picture/34.jpg?raw=true)

####  第二步 创建 Method

这一步主要是新建 Method 和Lambda 函数关联起来, 如下图所示: 


![image](https://github.com/dikers/serverless/blob/master/doc/picture/35.jpg?raw=true)


 因为网页要通过AJAX跨越访问，**需要启用CORS** 如下图所示:  


![image](https://github.com/dikers/serverless/blob/master/doc/picture/36.jpg?raw=true)


#### 第三步 部署api


经过部署以后 api 就生效了， 可以通过接口进行测试。 

![image](https://github.com/dikers/serverless/blob/master/doc/picture/37.jpg?raw=true)




### 7. 配置VPC， 设置安全组和安全策略

####  添加和修改安全组

[安全组的官方文档](https://docs.aws.amazon.com/zh_cn/vpc/latest/userguide/VPC_SecurityGroups.html)

安全组充当实例的虚拟防火墙以控制入站和出站流量。安全组在实例级别运行，而不是子网级别。因此，在VPC的子网中的每项实例都归属于不同的安全组集合。如果在启动时没有指定具体的安全组，实例会自动归属到VPC的默认安全组。对于每个安全组，可以添加规则以控制到实例的入站数据流，以及另外一套单独规则以控制出站数据流。

下图是在VPC中创建和管理 安全组。 

![image](https://github.com/dikers/serverless/blob/master/doc/picture/29.jpg?raw=true)

下图中是给安全组添加访问策略， 可以指定CIDR , IP , 安全组和前缀列表，
能访问的出站和入站的端口或者端口范围， 保证数据访问的安全性。 

![image](https://github.com/dikers/serverless/blob/master/doc/picture/30.jpg?raw=true)



### 8. TODO 添加 DevOps 管理开发流程

    
  使用 CodeBuild , CodeDeploy, CodePipeline 等工具，打造DevOps 工作模式。
    
    
    
### 9. TODO 使用CloudFormation 生成基础实施的模板文件


AWS CloudFormation
提供了一种通用语言来描述和预配置云环境中的所有基础设施资源。CloudFormation可以跨所有地区和账户使用简单的文本文件以自动化的安全方式为应用程序需要的所有资源建模并对其进行预配置。

![image](http://d1.awsstatic-china.com/CloudFormation%20Assets/howitworks.c316d3856638c6c9786e49011bad660d57687259.png)

基础设施及代码 (Infrastructure as Code), 可以用代码的方式对基础设施进行管理，可以很方便的自动化构建多种基础环境，方便开发，测试，以及灾后恢复。


下图是AWS CloudFormation 图形化设计界面,
可以通过拖拽控件或者直接编码的方式，很方便的设计template文件。 

![image](https://github.com/dikers/serverless/blob/master/doc/picture/32.jpg?raw=true)









