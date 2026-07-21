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

## 启动前准备

### 1. 启动 Cpolar 隧道

项目需要通过 Cpolar 进行内网穿透，请先登录 Cpolar 并创建两条 HTTP 协议隧道：

- **端口 8080**：后端服务
- **端口 5500**：前端服务

> Cpolar 官网：[https://www.cpolar.com](https://www.cpolar.com)

创建隧道后，将 Cpolar 分配的公网地址填入 `application.yml` 对应配置项中（`app.frontend-base-url`、`app.alipay.notify-url`、`app.alipay.return-url`、`app.cors.allowed-origins`）。

### 2. 启动前端

项目启动后，进入 `frontend` 目录，使用 Python 启动本地服务器：

```bash
cd frontend
python -m http.server 5500
```

前端运行地址：[http://localhost:5500](http://localhost:5500)

