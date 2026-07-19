package com.whlg.hospital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whlg.hospital.entity.Disease;
import com.whlg.hospital.mapper.DiseaseMapper;
import com.whlg.hospital.service.DiseaseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiseaseServiceImpl extends ServiceImpl<DiseaseMapper, Disease> implements DiseaseService {

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
}