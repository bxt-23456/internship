package com.whlg.hospital.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
