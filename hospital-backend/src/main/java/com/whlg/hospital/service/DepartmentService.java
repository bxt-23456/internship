package com.whlg.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whlg.hospital.entity.Department;
import com.whlg.hospital.vo.DepartmentVo;

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
     * 获取科室树
     * @return 科室树列表（一级科室包含子科室）
     */
    List<DepartmentVo> getDepartmentTree();
}
