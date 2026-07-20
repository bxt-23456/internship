package com.whlg.hospital.dto;

import lombok.Data;

/**
 * 创建挂号订单请求
 */
@Data
public class CreateAppointmentDto {

    private Long userId;
    private Long doctorId;
    private String patientName;
    private String patientPhone;
    private String patientIdCard;
    private Integer patientGender;
    private String patientBirthday;
    private String appointmentDate;
    private String appointmentTime;
    private String diseaseDesc;
}
