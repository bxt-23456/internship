package com.whlg.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whlg.hospital.entity.PaymentFlow;
import com.whlg.hospital.mapper.PaymentFlowMapper;
import com.whlg.hospital.service.PaymentFlowService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentFlowServiceImpl extends ServiceImpl<PaymentFlowMapper, PaymentFlow> implements PaymentFlowService {

    @Override
    public void recordPaymentSuccess(String businessOrderNo,
                                     Integer businessType,
                                     BigDecimal actualAmount,
                                     String tradeNo,
                                     String callbackContent) {
        PaymentFlow existed = this.lambdaQuery()
                .eq(PaymentFlow::getBusinessOrderNo, businessOrderNo)
                .eq(PaymentFlow::getPayStatus, 1)
                .one();
        if (existed != null) {
            return;
        }

        PaymentFlow paymentFlow = new PaymentFlow();
        paymentFlow.setBusinessOrderNo(businessOrderNo);
        paymentFlow.setBusinessType(businessType);
        paymentFlow.setPayMethod(1);
        paymentFlow.setThirdPartyTradeNo(tradeNo);
        paymentFlow.setActualAmount(actualAmount);
        paymentFlow.setPayStatus(1);
        paymentFlow.setPaySuccessTime(LocalDateTime.now());
        paymentFlow.setOriginalCallback(callbackContent);
        paymentFlow.setCreateTime(LocalDateTime.now());
        paymentFlow.setUpdateTime(LocalDateTime.now());
        this.save(paymentFlow);
    }
}
