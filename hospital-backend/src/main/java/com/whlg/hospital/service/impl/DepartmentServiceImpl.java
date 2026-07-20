package com.whlg.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whlg.hospital.entity.Department;
import com.whlg.hospital.mapper.DepartmentMapper;
import com.whlg.hospital.service.DepartmentService;
import com.whlg.hospital.vo.DepartmentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 科室服务实现类
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> listByIds(List<Long> ids) {
        return baseMapper.selectBatchIds(ids);
    }

    @Override
    public List<DepartmentVo> getDepartmentTree() {
        return departmentMapper.selectDepartmentTree();
    }
}
