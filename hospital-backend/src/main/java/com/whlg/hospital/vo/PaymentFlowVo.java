package com.whlg.hospital.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 支付流水VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentFlowVo {

    private Long id;
    private String businessOrderNo;
    private Integer businessType;
    private Integer payMethod;
    private String thirdPartyTradeNo;
    private BigDecimal actualAmount;
    private Integer payStatus;
    private LocalDateTime paySuccessTime;
    private LocalDateTime createTime;
}
