# AWS 无服务架构 demo项目


##  一、AWS无服务架构

### AWS无服务器计算

无服务器计算是云原生架构，使您能够将更多的运营职责转移到 AWS，从而提高灵活性和创新能力。无服务器计算让您可以在不考虑服务器的情况下构建并运行应用程序和服务。它消除了基础设施管理任务，例如服务器或集群配置、修补、操作系统维护和容量预置。


### 无服务器计算优势

* 无服务器管理
> 无需预置或维护任何服务器。无需安装、维护或管理任何软件或运行时。 

* 灵活扩展
> 应用程序可自动扩展，或通过切换占用资源（如吞吐量、内存）的单位数（而不是切换单个服务器的单位数）来调整容量，从而实现扩展。

* 按价值付费
> 为一致的吞吐量或执行持续时间（而不是服务器单元）付费。

* 自动化的高可用性
> 无服务器应用程序提供内置可用性和容错功能。您无需构建这些功能，因为运行此应用程序的服务在默认情况下会提供这些功能。


### AWS Lambda 计算服务

AWS Lambda 是一项计算服务，可使您无需预配置或管理服务器即可运行代码。AWS Lambda 只在需要时执行您的代码并自动缩放，从每天几个请求到每秒数千个请求。您只需按消耗的计算时间付费 – 代码未运行时不产生费用。借助 AWS Lambda，您几乎可以为任何类型的应用程序或后端服务运行代码，并且不必进行任何管理。AWS Lambda 在可用性高的计算基础设施上运行您的代码，执行计算资源的所有管理工作，其中包括服务器和操作系统维护、容量预置和自动扩展、代码监控和记录。


### AWS Lambda 利用容器重用来提高函数性能

确保您的代码检索到的外部化配置或依赖关系在初次执行后在本地存储和引用。限制变量/对象在每次调用时的重新初始化，而是使用静态初始化/构造函数、全局/静态变量以及单例。保持运行并重复使用连接 (HTTP，数据库等)，它们在上次调用时建立。



![image](http://)
图1




## 二、 项目功能和技术架构介绍。

###项目功能介绍

![image](http://)  
图2

项目本身的功能比较简单， 通过用户输入的关键字，实时返回查询结果， 并显示在页面上。 



### 项目架构设计

![image](http://)  
图2



####  S3 托管静态网站

前端用Vue 做静态页面


####  Api Gateway 做数据转发


####  Lambda 做数据技术
Service 层用java 实现数据计算和处理


####  RDS 做数据存储

使用AWS RDS mysql 作为数据存储单元， 数据库需要在多可用区做备份， 以及做读写分离

####  AWS Glacier 做数据定期的归档


## 三、 项目实施步骤


### 1. 开通AWS 账号

直接到[亚马逊注册中心](https://portal.aws.amazon.com/billing/signup?redirect_url=https%3A%2F%2Faws.amazon.com%2Fregistration-confirmation&language=zh_cn#/start)开通账号， 包含12个月的免费套餐，可以用来学习。
需要绑定信用卡， **注意不需要使用服务的时候，及时关闭资源，避免产生不必要的费用。**
建议设置计费警告， 当超过免费套餐额度的时候， 会收到邮件。 
 

### 2. 申请域名， 通过Route53解析
首先需要注册域名，**需要一些费用**

(例如 example.com) 和子域 (例如 www.example.com)  


### 3. 开通 S3, 进行静态内容托管

[托管静态网站 -- 官方教程](https://aws.amazon.com/cn/getting-started/projects/host-static-website/?c_1)

![image](https://d1.awsstatic.com/Projects/v1/AWS_StaticWebsiteHosting_Architecture_4b.da7f28eb4f76da574c98a8b2898af8f5d3150e48.png)

*   需要创建两个存储桶（s3）。一个存储桶包含内容。另一个存储桶用来重定向请求。
>  在Route53 中添加两个record
   example.com  存储桶包含内容
   www.example.com   用来重定向请求

![image]()
图 3


*  此存储桶的公有访问设置，需要能修改访问策略，否则会修改不成功，见下图。 


![image]()
图 3

* 设置访问策略,注意要设置存储桶的访问权限为公有.
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
![image]()
图 3


*  设置完成以后，需要用Route53 做流量转发

![image]()
图 22


### 4. 申请RDS 数据库

[申请RDS mysql数据库 - 官方教程](https://aws.amazon.com/cn/getting-started/tutorials/create-mysql-db/)

* 可以设置多可用区域部署， 提供系统可用写， RDS会从一个可用区，同步复制到另外一个可用区里，当主库不用时，可以自动切换到从库。

* 在生产环境中需要设置安全组，只有Lambda服务所在的安全组，才有权限读写数据库。

* 为了提供访问性能， 可以设置只读副本， 写示例可以异步备份数据到读示例上 。

* 设置AWS IAM 用户和角色来管理数据库用户凭证。

 

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


* 给lambda 函数添加触发器， 

   这里我们选用 Api Gateway进行关联， 把来自S3托管网页的ajax 请求，转发给Lambda
   进行处理。 
![image]()
图 22

* 环境变量的配置

将AWS 上的数据库配置信息加密保存到AWS上， 与代码分离。 通过代码读取。
同时需要选择加密的方式。 
 
 ![image]() 图 22

* 网络安全设置

本项目的lambda函数需要外部 Internet 访问，确保安全组允许出站连接并且 VPC 具有
NAT 网关, 能让外部用户访问到。 





### 6. 部署Api Gateway, 接入到lambda 接口上。

### 7. 配置VPC， 设置安全组和安全策略


















