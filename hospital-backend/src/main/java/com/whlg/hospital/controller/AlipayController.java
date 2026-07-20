package com.whlg.hospital.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.whlg.hospital.config.AlipayConfig;
import com.whlg.hospital.dto.AlipayDto;
import com.whlg.hospital.service.AppointmentService;
import com.whlg.hospital.service.ConsultService;
import com.whlg.hospital.util.R;
import com.whlg.hospital.vo.AppointmentVo;
import com.whlg.hospital.vo.ConsultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Author: wanjianhong
 * @Version: 1.0
 * @Description:
 */
@RestController
@RequestMapping("/alipay")
public class AlipayController {

    private static final String FRONTEND_BASE_URL = "http://127.0.0.1:5500/";

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ConsultService consultService;

    /**
     * 注意：前端请求的参数需要小写
     * @description: 支付
     * @param: WIDout_trade_no 商户订单号，商户网站订单系统中唯一订单号，必填
     * @return: com.hp.yihu.util.R
     **/
    @PostMapping("/pay")
    public R pay(@RequestBody AlipayDto alipayDto){
        System.out.println("*******pay*******");
        try {
            //获得初始化的AlipayClient
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);

            //设置请求参数
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
            alipayRequest.setReturnUrl(AlipayConfig.return_url);
            alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

            //商户订单号，商户网站订单系统中唯一订单号，必填
            String out_trade_no = alipayDto.getWidout_trade_no();
            //付款金额，必填
            String total_amount = alipayDto.getWidtotal_amount().toString();
            //订单名称，必填
            String subject = alipayDto.getWidsubject();
            //商品描述，可空
            String body = alipayDto.getWidbody();

            System.out.println("out_trade_no = " + out_trade_no);
            System.out.println("total_amount = " + total_amount);
            System.out.println("subject = "+ subject);
            System.out.println("body = "+ body);
            alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                    + "\"total_amount\":\""+ total_amount +"\","
                    + "\"subject\":\""+ subject +"\","
                    + "\"body\":\""+ body +"\","
                    + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
            //请求
            String result = alipayClient.pageExecute(alipayRequest).getBody();
            System.out.println(result);//响应的JS脚本,用于跳转到支付宝收银台界面
            //输出
            return R.createSuccess(result);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return R.createError();
    }

    /* *
     * 功能：支付宝服务器异步通知页面
     * 说明：
     * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
     * 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
     *************************功能说明*************************
     * 该页面不能在本机电脑测试，请到服务器上做测试。请确保外部可以访问该页面。
     * 如果没有收到该页面返回的 success
     * 建议该页面只做支付成功的业务逻辑处理，退款的处理请以调用退款查询接口的结果为准。
     */
    @GetMapping("/notifyUrl") //设置请求白名单
    public void notifyUrl(HttpServletRequest request,HttpServletResponse response) throws IOException, AlipayApiException {
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

        //——请在这里编写您的程序（以下代码仅作参考）——

	/* 实际验证过程建议商户务必添加以下校验：
	1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
	2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
	3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
	4、验证app_id是否为该商户本身。
	*/
        if(signVerified) {//验证成功
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

            if(trade_status.equals("TRADE_FINISHED")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            }else if (trade_status.equals("TRADE_SUCCESS")){
                handlePaySuccess(out_trade_no, trade_no, params.toString());
            }

            response.getWriter().println("success");

        }else {//验证失败
            response.getWriter().println("fail");

            //调试用，写文本函数记录程序运行情况是否正常
            //String sWord = AlipaySignature.getSignCheckContentV1(params);
            //AlipayConfig.logResult(sWord);
        }
    }

    /* *
     * 功能：支付宝服务器同步通知页面
     * 说明：
     * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
     * 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
     */
    @GetMapping("/returnUrl")  //设置请求白名单
    public void returnUrl(HttpServletRequest request,HttpServletResponse response) throws IOException, AlipayApiException {
        //获取支付宝GET过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        System.out.println("params = " + params);
        boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名

        //——请在这里编写您的程序（以下代码仅作参考）——
        if(signVerified) {
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");

            handlePaySuccess(out_trade_no, trade_no, params.toString());

            if (isAppointmentOrder(out_trade_no)) {
                response.sendRedirect(FRONTEND_BASE_URL + "reservation-success.html?orderNo=" + URLEncoder.encode(out_trade_no, "UTF-8"));
            } else if (isConsultOrder(out_trade_no)) {
                response.sendRedirect(FRONTEND_BASE_URL + "consult-success.html?orderNo=" + URLEncoder.encode(out_trade_no, "UTF-8"));
            } else {
                response.getWriter().println("trade_no:"+trade_no+"<br/>out_trade_no:"+out_trade_no+"<br/>total_amount:"+total_amount);
            }


        }else {
            response.getWriter().println("验签失败");
        }
    }

    private void handlePaySuccess(String orderNo, String tradeNo, String callbackContent) {
        if (orderNo == null || orderNo.isEmpty()) {
            return;
        }
        if (isAppointmentOrder(orderNo)) {
            appointmentService.paySuccess(orderNo, tradeNo, callbackContent);
        } else if (isConsultOrder(orderNo)) {
            consultService.paySuccess(orderNo, tradeNo, callbackContent);
        }
    }

    private boolean isAppointmentOrder(String orderNo) {
        if (orderNo == null || orderNo.isEmpty()) {
            return false;
        }
        if (orderNo.startsWith("APPOINTMENT") || orderNo.startsWith("DD")) {
            return true;
        }
        AppointmentVo appointment = appointmentService.getDetail(orderNo);
        return appointment != null;
    }

    private boolean isConsultOrder(String orderNo) {
        if (orderNo == null || orderNo.isEmpty()) {
            return false;
        }
        if (orderNo.startsWith("CONSULT") || orderNo.startsWith("DH")) {
            return true;
        }
        ConsultVo consult = consultService.getDetail(orderNo);
        return consult != null;
    }
}
