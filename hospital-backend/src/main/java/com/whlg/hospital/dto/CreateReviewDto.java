package com.whlg.hospital.dto;

import lombok.Data;

/**
 * 提交评价请求
 */
@Data
public class CreateReviewDto {

    /**
     * 1-挂号订单 2-咨询订单
     */
    private Integer orderType;
    private Long orderId;
    private String orderNo;
    private Long userId;
    private Long doctorId;
    private Integer rating;
    private String content;
}
