package com.whlg.hospital.service.impl;

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
}
