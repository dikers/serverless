### web 前端代码


* 使用jquery 实现ajax 异步提交请求

* 将文件部署到S3存储桶中


[AWS ACL 命令行使用教程](https://aws.amazon.com/cn/getting-started/tutorials/backup-to-s3-cli/) 

使用命令接口上传文件

```
aws s3 cp  ./index.html s3://example.com/index.html
```

后面可以加到 CodeBuild 的脚本文件里面， 自动上传和发布。 

