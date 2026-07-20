package com.whlg.hospital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whlg.hospital.entity.Hospital;
import com.whlg.hospital.mapper.HospitalMapper;
import com.whlg.hospital.service.HospitalService;
import com.whlg.hospital.vo.HospitalVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whlg.hospital.entity.Department;
import com.whlg.hospital.service.DepartmentService;
import com.whlg.hospital.vo.DepartmentTreeVo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 医院服务实现类
 */
@Service
public class HospitalServiceImpl extends ServiceImpl<HospitalMapper, Hospital> implements HospitalService {

    @Autowired
    private HospitalMapper hospitalMapper;
    
    @Autowired
    private DepartmentService departmentService;

    @Override
    public List<HospitalVo> listHospitals(Long departmentId, Long parentDepartmentId, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        return hospitalMapper.selectHospitals(departmentId, parentDepartmentId, offset, pageSize);
    }

    @Override
    public int countHospitals(Long departmentId, Long parentDepartmentId) {
        return hospitalMapper.countHospitals(departmentId, parentDepartmentId);
    }

    @Override
    public HospitalVo getHospitalDetail(Long id) {
        return hospitalMapper.selectHospitalDetail(id);
    }

    @Override
    public List<HospitalVo> listTopHospitals(int limit) {
        return hospitalMapper.selectTopHospitals(limit);
    }

    @Override
    public List<Long> getHospitalDepartmentIds(Long hospitalId) {
        return hospitalMapper.selectHospitalDepartmentIds(hospitalId);
    }

    @Override
    public List<DepartmentTreeVo> getHospitalDepartmentTree(Long hospitalId) {
        // 1. 获取医院关联的科室ID列表
        List<Long> departmentIds = hospitalMapper.selectHospitalDepartmentIds(hospitalId);
        if (departmentIds == null || departmentIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. 获取所有相关科室信息
        List<Department> departments = departmentService.listByIds(departmentIds);
        if (departments == null || departments.isEmpty()) {
            return new ArrayList<>();
        }

        // 3. 获取所有父级科室ID
        Set<Long> parentIds = new HashSet<>();
        for (Department dept : departments) {
            if (dept.getParentId() != null && dept.getParentId() != 0) {
                parentIds.add(dept.getParentId());
            }
        }

        // 4. 获取父级科室信息
        List<Department> parentDepts = new ArrayList<>();
        if (!parentIds.isEmpty()) {
            parentDepts = departmentService.listByIds(new ArrayList<>(parentIds));
        }

        // 5. 构建树形结构
        List<DepartmentTreeVo> tree = new ArrayList<>();
        
        // 添加父级科室
        for (Department parent : parentDepts) {
            DepartmentTreeVo parentVo = new DepartmentTreeVo(parent.getId(), parent.getName(), parent.getDescription());
            List<DepartmentTreeVo> children = new ArrayList<>();
            
            // 添加子科室
            for (Department child : departments) {
                if (child.getParentId() != null && child.getParentId().equals(parent.getId())) {
                    children.add(new DepartmentTreeVo(child.getId(), child.getName(), child.getDescription()));
                }
            }
            
            parentVo.setChildren(children);
            tree.add(parentVo);
        }

        return tree;
    }

    @Override
    public List<Hospital> searchHospitals(String keyword) {
        LambdaQueryWrapper<Hospital> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.like(Hospital::getName, keyword.trim());
        }
        wrapper.eq(Hospital::getStatus, 1);
        return this.list(wrapper);
    }
}
