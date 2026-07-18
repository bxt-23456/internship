package com.whlg.hospital.vo;

import java.util.List;

/**
 * 科室树形结构VO
 */
public class DepartmentTreeVo {
    private Long id;
    private String name;
    private String description;
    private List<DepartmentTreeVo> children;

    public DepartmentTreeVo() {}

    public DepartmentTreeVo(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<DepartmentTreeVo> getChildren() { return children; }
    public void setChildren(List<DepartmentTreeVo> children) { this.children = children; }
}