# 项目说明

由于阿里云密钥写进配置将无法推送，请先将以下信息写进 `application.yml` 再运行项目：

```yaml
# 阿里云短信配置
aliyun:
  sms:
    access-key-id: 填写你的id
    access-key-secret: 填写你的密码
    endpoint: dypnsapi.aliyuncs.com
```

> **注意：** 请先启动 Redis，再启动项目。项目使用 Redis 存储用户登录 Token，未启动 Redis 会导致登录功能异常。

