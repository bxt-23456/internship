package com.whlg.hospital.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 *
 * 沙箱环境不稳定
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "9021000165679603";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDujOvu3Rk5CemftT86grT64DKos3PkZK/kiVY42cVfi9K1Rxb9tAUWCByYZBZ6qY4bSGPKr77h9UAAp/b9roduhFnubP/nh60xacnCqHA2EtYYsHEb+1C2Xofb5rJlZFmNXQ1T3wrmOISyImHob/o7+YtCPg9gC4C0asGENa78KJeFjl+O2FRE3+wyNkHa8gszZ5HsRE0nOykT/r76intd0JpgLG9viYcHIe/pOyb5LT0THYvIzT1nybtPI3PIstM31nqCtBSvKla069sOxem9eckM39ReWqVWcuk3rH8ekpzoJadzCuigvaUxZiMrfJQrUjdO5tz7hHw9TjXVCvRjAgMBAAECggEAPHo/qlwlc99+ej3yHLxcc6n5TenI/ONF8JOc52bWciW5srmmK4XDMFW4Ii5lwI1R+Lq+iflKVHDf3Aq5RJFFxpAJWLgLnMA5+WgPfalEYYEfBD1fP6/UQ5ftq6NuIORzC7LGmTfXxIwZoCu1VL9m2mOmmZGwRjJEW/kgvIr7pPOMOpkN6kub+zf6b9t4EEx7vwAw7EENkDU7A+YUS60ead6aVcybn4ED2eT9Q08mu4p15qLEBeJfDF2q8eEWMsCcih/6IyAb+/1vnOdDRcq5ym3h/jx+2vbY6j8tGsJzNPE4AV9QCwfo5EhGdh1GgK7ByGyicA8ucSec311jAY/CMQKBgQD8ImNxYo9yXO79Z7iOf+/1696N1zjaQYH19lmb1+mTtnQABTRNah4uv5ni1+oti1p1LnjUC1FIEu9UzXdlyubZJ/owON00zuerGxwCI5fWpr6RgYuNqBXKOrNeFaVNmiU8TZkN9Zey4vPhLIXbaqdy5OZjVPJDnfHaDabeGk+7CwKBgQDyNTemNp1/ZDSUAs7O7G7/+nthmbiqbERnxw1OX4tg/XXGrWLjBGCNzrDZL2LI+fBHpyAXzC1ZVPchkFMK9WMXdsq0tVHzNhNAV5dcGXr3OqrRDMsJsK43TpeprA7GSeL4YGFILY+/DWmzxfRwhdj9euTEBMcPuQhNXeNtYMzDCQKBgFsxtPYVx/x9UJOnKEGiVJCGN4DmD1Ihz3e/GrobZghfPaWkPD6j2zbdZjN+FN1UQeuQW3OWhFelQZtSlZTjxotUwlEewCORkol4zLCZXxnsRZErYeZinT7/7FBjU961mjItfuZlpfC11a/kAqZR9S7BS/Mxjk6WmhtcRLagnaXxAoGAQHu6nR3dQJVFCkLVHHIF+1CMmKd8aqL4c0ijWkJFsUTJnmN6+EWqDxcALlJXUnp4ZhswiCVrzWS5hs4NzdUW1KBjx0ie0MmMxoyLV5HRExEjeRSW2bWYLFFYP2v3YwM40jeWrsiyhILlqV8CfRKlepbFYQqElMMGWtKsQeqLmUECgYEA6DtNhA4zi4bXwnob5p2IsquhM8ZhqpAQpX6DWzQExiLtb5svz4JBC0StUsbbXq6uBrw5crPO65zHhB7eZJSrJbj6AAaza6ylyG6g+m5c0gzehw2hS1FGW1A8nfH/PDhXCJGG07tYa6MVCsepJ4BlymzJaDhDJGRfaVswVrSLtBE=";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqP898eF5ZrHDBTE1wAQrMlyUHT9tsTd1GuuLeK7r+DGGI7zfWJfVICP7O2nByJju84VRPf2F5gycDZAbrRAyqXnCW717O79FFM4cZt786I86R18hkZFQsgbf3ytvqmpF/o9Wg5U7aeGpdAZQA2j3VMSo2Ot1G4tdOCJ6iGnstKRe5GUTBWjcbQfp7r1UuhtJiq2wE7iak45FElBbeBy81JB/GbxqKKFRmPibVGKOblGHj1zR7NkTJ3hYFPP0vWQsCgoFu05HwJhR2AaEgz5mcUJXYaQQANq6JiU92dj/ZifMRhrdiQT9A3R8vjoI68+03qJuED8ZEUZoh8zNolNQxwIDAQAB";
    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://6a8c7eb.r2.cpolar.top/alipay/notifyUrl";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://6a8c7eb.r2.cpolar.top/alipay/returnUrl";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关 - 更改
    public static String gatewayUrl = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

