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
    public static String app_id = "9021000165674887";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC5e/FYt8hpq7XzP+M4MpEOTUCahTHrtMymK3WwAZqIaomD6WKHTKFxqXNEUwrxmmv1ZqiYpq55JAntlS1iqXhYwj5Yb79h9inVdcNiMM3efSg8tplqpvd5n3uxbUoRyWiDfrg0uUn3D5RR49OTWEUNu6lw0U61F7vttNM4hp85ajNFa0/8ryi8GEnnodQKiihrVJq/TXrV76I9PhbL6d6FcDNEvvMYNiXdtIkhxkXWXkxAC/bjmf2QcWMx8SC+OIyvkcG8jaeIRBgwabj7TapmYS5NaFnlXRj6My9Nl7iZuvc0zBlT6F5Az0gLUcEoQUdA0MOvt2pKVHpNTDHxk8wJAgMBAAECggEAPblqIjzee5PBs9YR1hS1ws2gbwteBnMfGzkhK9YMx9K0OmqC+EVfAEUEb7s2zBm4TWzBNDNbokKE70QSW1/MBbV2K9XcGUxitX8/6sAU8jIyHqohOaWgex/AeNXGrU6Z8hBMWndCju3nTK0FLmZUbHKDu2Bb0dW5GyxukcPy3pbQKALt06YgiB5/9k0eURn8NFiZdNtDNIcOSf/QMSvxDiYtPI7PtYavUnwbI1JgvxBDINFS6SQBMrk1Hm55B1sSihzyp1DGwiR2rF72AFl+Mpdn4fppFv5icIFrYooXLBVJY5xYIt3BySt3D6jByT56rEp7o0iptPx0thIqLXnQxQKBgQDuMskyng2EyeSNYcBgoDQJ/DFPHu8CZYSIR7k55UYyb4GU90wiS8kfd5nHbtjRAn7AmoKOKnILpK6VNhP1Baf+7nPlv+jMY7BRilXBx+AOLRbnBQCw8jyb0ln1qsc0o5d6547x6qnr5Mes2LsZqJ1VMPmEd/Z7Nbp+X3+Om3TtYwKBgQDHWKCpPHM5pDNuJhtuP3n2x0JndtqjHzi/EaeJFv/cqFdCsz4DhAICSdhwmBx9pk4eJq0JtU6PWJrueshvgvFrT+UwjtzSwD9kRbAFh5j3CrSiJPCuFhn1EzzfZIOQD8UFqgh2vGMrCeHgcLKlFJuTv1+Ec+6nkzVnAs/1APiiowKBgBibInGaJidbe8KnIGpa4kVNygI0XdhPiFtTnWy/pHq9ThOHYFQCpCod/sK9TqR4r4NR8r6g39sYDfjX90POa8ZH96z6ICBNA//IXIeCEEEmo0EGIUYmH+Cw4B4ioCVt6M4HcZrH9PmrTn0qflluM6KDRqNKiSm9KSC42MJwiKmlAoGAJsUb6g4xssI2pnOO3jBGPjKevZSmDRejZ7W9SbJJbNbosbY+l1xzL5LBH5TV5bIUe1S2Tq8Oal6nMz56AQYMgfx17K28UNjG+296PnkSzOkuHGLm1FTEE5/8NM5NsBUBEorxPnaBKvuvDIGenPCXGXMc1pvdjIXa2v/3BgV8VdUCgYEAwEqphFkzoR81WADOM5H9j76PCEJAn1qpgXqOXJMFugp2aXSRfFBD2cRGc5ZoLM5f3Uv+rFkYqFlELYFh/88cDWY/2Xu7HqAZRPGzjuPdh/ZoyqlgrrBDJdeGPOE2dzoXzroYiHBOlW6i+GtQfHMQsqbQcUDjYlbxLZ3T1ikcOcY=";
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAg0BW0sElspkMPrrVxZUyJiYr3HvLGt/89aenvs7NlbN5x3i+XE6OtfbtmSqF4KYa77T3Qhg1lZU0z9UBWF85nPHyJ/PUA8tngFCyV5DX6u6/3UePvYAwnaHEY+d1JuFd7n8fw/YjHONMiyVidn9LKIIbc6baQJFjX9eHiTM5OluRkotzA+dYzlEkKSXPA0QCd0ZO3BlGpHPj/YOlPwi0J+ffisE1miDoT1HGbepe1Q8bdoUVXSlksbzGE4U+jkhI8yMzm85tfLcJ6l75G1x/9yedEOY6orLeRVYpt6YR0z4g3wP8GYsTYUWL9gEleUb2+C6ZYwlO0HIRjSBniEH0qwIDAQAB";
    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "https://629809a8.r18.cpolar.top/alipay/notifyUrl";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "https://629809a8.r18.cpolar.top/alipay/returnUrl";

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

