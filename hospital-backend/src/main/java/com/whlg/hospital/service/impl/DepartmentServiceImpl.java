package com.whlg.hospital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whlg.hospital.entity.Department;
import com.whlg.hospital.mapper.DepartmentMapper;
import com.whlg.hospital.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 科室服务实现类
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Override
    public List<Department> listByIds(List<Long> ids) {
        return baseMapper.selectBatchIds(ids);
    }

    @Override
    public List<Department> listByParentId(Long parentId) {
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        if (parentId != null) {
            wrapper.eq(Department::getParentId, parentId);
        }
        wrapper.eq(Department::getStatus, 1);
        wrapper.orderByAsc(Department::getSortOrder);
        return baseMapper.selectList(wrapper);
    }
}
