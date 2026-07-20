package com.whlg.hospital.dto;

import lombok.Data;

/**
 * 创建咨询订单请求
 */
@Data
public class CreateConsultDto {

    private Long userId;
    private Long doctorId;
    private String patientName;
    private String patientPhone;
    private String diseaseDesc;
}
