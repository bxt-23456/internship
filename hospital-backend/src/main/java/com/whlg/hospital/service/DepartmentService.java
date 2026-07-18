package com.whlg.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whlg.hospital.entity.Department;

import java.util.List;

/**
 * 科室服务接口
 */
public interface DepartmentService extends IService<Department> {

    /**
     * 根据ID列表批量查询科室
     * @param ids 科室ID列表
     * @return 科室列表
     */
    List<Department> listByIds(List<Long> ids);

    /**
     * 根据父级ID查询子科室列表
     * @param parentId 父级科室ID，传0或null查一级科室
     * @return 科室列表
     */
    List<Department> listByParentId(Long parentId);
}
