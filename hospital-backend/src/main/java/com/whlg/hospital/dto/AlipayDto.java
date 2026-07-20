package com.whlg.hospital.dto;

import lombok.Data;

//接收支付宝支付的参数DTO类
@Data
public class AlipayDto {

    //商户订单号，商户网站订单系统中唯一订单号，必填
    private String widout_trade_no;
    //付款金额，必填
    private String widtotal_amount;
    //订单名称，必填
    private String widsubject;
    //商品描述，可空
    private String widbody;

}
