package com.whlg.hospital.vo.reservation;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDoctorVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String name;
    private Integer gender;
    private String title;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long departmentId;
    private String departmentName;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long hospitalId;
    private String hospitalName;
    private String avatar;
    private String phone;
    private String intro;
    private String expertise;
    private Integer consultCount;
    private BigDecimal rating;
    private Integer followCount;
    private Integer onlineStatus;
    private BigDecimal price;
    private BigDecimal registrationPrice;
    private Integer status;
}
