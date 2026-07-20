package com.whlg.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whlg.hospital.entity.PaymentFlow;

import java.math.BigDecimal;

public interface PaymentFlowService extends IService<PaymentFlow> {

    void recordPaymentSuccess(String businessOrderNo,
                              Integer businessType,
                              BigDecimal actualAmount,
                              String tradeNo,
                              String callbackContent);
}
