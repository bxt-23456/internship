package com.whlg.hospital.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 疾病推荐VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseRecommendVo {

    private List<DepartmentVo> relatedDepartments;

    private List<DoctorVo> doctors;
}
