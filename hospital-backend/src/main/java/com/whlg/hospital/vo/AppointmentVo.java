package com.whlg.hospital.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 挂号订单VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentVo {

    private Long id;
    private String orderNo;
    private Long userId;
    private Long doctorId;
    private String doctorName;
    private String doctorAvatar;
    private Long hospitalId;
    private String hospitalName;
    private String departmentName;
    private String patientName;
    private String patientPhone;
    private String patientIdCard;
    private Integer patientGender;
    private Integer patientAge;
    private LocalDate appointmentDate;
    private String appointmentTime;
    private String diseaseDesc;
    private BigDecimal amount;
    private Integer status;
    private LocalDateTime payTime;
    private LocalDateTime createTime;
}
