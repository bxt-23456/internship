package com.whlg.hospital.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 科室VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentVo {

    private Long id;
    private String name;
    private String description;
    private Long parentId;
    private Integer sortOrder;
    private Integer status;
    private List<DepartmentVo> children;
}
