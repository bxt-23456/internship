package com.whlg.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whlg.hospital.entity.Department;
import com.whlg.hospital.vo.DepartmentVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 科室Mapper
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {

    List<DepartmentVo> selectDepartmentTree();

    List<DepartmentVo> selectSiblingDepartments(@Param("departmentId") Long departmentId);
}
