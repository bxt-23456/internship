package com.whlg.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whlg.hospital.entity.Department;
import org.apache.ibatis.annotations.Mapper;

/**
 * 科室Mapper
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
}
