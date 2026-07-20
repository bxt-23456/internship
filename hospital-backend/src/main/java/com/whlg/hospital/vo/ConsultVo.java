package com.whlg.hospital.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 电话咨询订单VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultVo {

    private Long id;
    private String orderNo;
    private Long userId;
    private Long doctorId;
    private String doctorName;
    private String doctorAvatar;
    private String doctorTitle;
    private String departmentName;
    private String hospitalName;
    private String patientName;
    private String patientPhone;
    private String diseaseDesc;
    private LocalDateTime appointmentTime;
    private Integer duration;
    private BigDecimal amount;
    private Integer status;
    private Integer hasReviewed;
    private LocalDateTime payTime;
    private LocalDateTime createTime;
}
