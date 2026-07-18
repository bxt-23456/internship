package com.whlg.hospital.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 就诊成员VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FamilyMemberVo {

    private Long id;
    private Long userId;
    private String name;
    private Integer gender;
    private LocalDate birthday;
    private String phone;
    private String idCard;
    private String relation;
    private Integer isDefault;
}
