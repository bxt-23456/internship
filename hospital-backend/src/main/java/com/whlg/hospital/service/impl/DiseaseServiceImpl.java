package com.whlg.hospital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whlg.hospital.entity.Disease;
import com.whlg.hospital.mapper.DepartmentMapper;
import com.whlg.hospital.mapper.DoctorMapper;
import com.whlg.hospital.mapper.DiseaseMapper;
import com.whlg.hospital.service.DiseaseService;
import com.whlg.hospital.vo.DepartmentVo;
import com.whlg.hospital.vo.DiseaseRecommendVo;
import com.whlg.hospital.vo.DiseaseVo;
import com.whlg.hospital.vo.DoctorVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiseaseServiceImpl extends ServiceImpl<DiseaseMapper, Disease> implements DiseaseService {

    @Autowired
    private DiseaseMapper diseaseMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Override
    public List<Disease> searchDiseases(String keyword) {
        LambdaQueryWrapper<Disease> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.and(w -> w.like(Disease::getName, keyword.trim())
                              .or()
                              .like(Disease::getAlias, keyword.trim())
                              .or()
                              .like(Disease::getSymptoms, keyword.trim()));
        }
        return this.list(wrapper);
    }

    @Override
    public IPage<DiseaseVo> pageDisease(Integer pageNum, Integer pageSize, Long departmentId) {
        Page<DiseaseVo> page = new Page<>(pageNum, pageSize);
        return diseaseMapper.selectDiseasePage(page, departmentId);
    }

    @Override
    public DiseaseVo getDiseaseDetail(Long id) {
        return diseaseMapper.selectDiseaseDetail(id);
    }

    @Override
    public DiseaseRecommendVo getDiseaseRecommend(Long diseaseId) {
        Disease disease = diseaseMapper.selectById(diseaseId);
        if (disease == null) {
            return new DiseaseRecommendVo();
        }
        Long departmentId = disease.getDepartmentId();
        List<DepartmentVo> relatedDepartments = departmentMapper.selectSiblingDepartments(departmentId);
        List<DoctorVo> doctors = doctorMapper.selectDoctorsByDepartmentId(departmentId, 6);
        DiseaseRecommendVo recommend = new DiseaseRecommendVo();
        recommend.setRelatedDepartments(relatedDepartments);
        recommend.setDoctors(doctors);
        return recommend;
    }
}