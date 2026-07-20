package com.whlg.hospital.util;

import com.aliyun.dypnsapi20170525.Client;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 阿里云短信验证码发送工具类
 * 基于云通信号码认证服务 SendSmsVerifyCode 接口
 *
 * 配置项（在 application.yml 中）：
 * aliyun.sms.access-key-id: 阿里云 AccessKey ID
 * aliyun.sms.access-key-secret: 阿里云 AccessKey Secret
 * aliyun.sms.endpoint: 阿里云短信服务端点
 */
@Component
public class AliyunSmsUtil {

    @Value("${aliyun.sms.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.sms.access-key-secret}")
    private String accessKeySecret;

    @Value("${aliyun.sms.endpoint}")
    private String endpoint;

    private Client client;

    /**
     * 使用 AccessKey 初始化客户端
     *
     * @throws Exception 初始化失败时抛出
     */
    @PostConstruct
    public void init() throws Exception {
        Config config = new Config()
                //阿里云 AccessKey ID
                .setAccessKeyId(accessKeyId)
                //阿里云 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // Endpoint 请参考 https://api.aliyun.com/product/Dypnsapi
        config.endpoint = endpoint;
        this.client = new Client(config);
    }

    /**
     * 发送短信验证码
     *
     * @param phoneNumber    接收短信的手机号，示例：130****0000[reference:1]
     * @param signName       短信签名名称，建议使用系统赠送签名[reference:2]
     * @param templateCode   短信模板CODE[reference:3]
     * @param templateParam  短信模板参数，JSON字符串格式[reference:4]
     * @param outId          外部流水号（可选）
     * @return API 响应的 JSON 字符串
     * @throws Exception 调用失败时抛出
     */
    public String sendSmsVerifyCode(String phoneNumber, String signName,
                                    String templateCode, String templateParam,
                                    String outId) throws Exception {
        // 构建请求对象
        SendSmsVerifyCodeRequest request = new SendSmsVerifyCodeRequest()
                .setPhoneNumber(phoneNumber)
                .setSignName(signName)
                .setTemplateCode(templateCode)
                .setTemplateParam(templateParam);

        // 设置外部流水号（可选）
        if (outId != null && !outId.isEmpty()) {
            request.setOutId(outId);
        }

        // 运行时配置
        RuntimeOptions runtime = new RuntimeOptions();

        // 调用接口
        SendSmsVerifyCodeResponse response = client.sendSmsVerifyCodeWithOptions(request, runtime);

        // 返回 JSON 格式结果
        return new Gson().toJson(response);
    }

    /**
     * 简化版：发送短信验证码（不含外部流水号）
     */
    public String sendSmsVerifyCode(String phoneNumber, String signName,
                                    String templateCode, String templateParam) throws Exception {
        return sendSmsVerifyCode(phoneNumber, signName, templateCode, templateParam, null);
    }

    /**
     * 验证码随机六位
     */
    public static String generateVerificationCode() {
        // Math.random()返回带正号的 double 值，该值大于等于 0.0 且小于 1.0。 加1是为了避免随机数是0的情况生成的是0。
        // 但是加1又会使得生成的数都是1开头，所以乘以9，避免开头都是1。
        int code = (int)((Math.random()*9+1)*100000);
        return String.format("%06d", code); // 保证生成的是四位数字，不足四位时前面补0
    }

    public static void main(String[] args) throws Exception {
        AliyunSmsUtil util = new AliyunSmsUtil();
        String phoneNumber = "18986713236";
        String signName = "速通互联验证码";
        String templateCode= "100001";
        String code  = generateVerificationCode();
        String templateParam = "{\"code\":\""+code+"\",\"min\":\"5\"}";
        String result = util.sendSmsVerifyCode(phoneNumber, signName, templateCode, templateParam);
        System.out.println(result);
    }
}
