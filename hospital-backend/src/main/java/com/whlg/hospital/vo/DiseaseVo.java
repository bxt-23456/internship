package com.whlg.hospital.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 疾病VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseVo {

    private Long id;
    private Long departmentId;
    private String departmentName;
    private String name;
    private String description;
    private String alias;
    private String location;
    private String treatment;
    private String symptoms;
    private String treatmentPeriod;
    private String cureRate;
    private String examinations;
    private Integer followCount;
}
