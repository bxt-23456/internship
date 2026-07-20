package com.whlg.hospital.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.whlg.hospital.entity.Disease;
import com.whlg.hospital.vo.DiseaseRecommendVo;
import com.whlg.hospital.vo.DiseaseVo;

import java.util.List;

public interface DiseaseService extends IService<Disease> {

    List<Disease> searchDiseases(String keyword);

    IPage<DiseaseVo> pageDisease(Integer pageNum, Integer pageSize, Long departmentId);

    DiseaseVo getDiseaseDetail(Long id);

    DiseaseRecommendVo getDiseaseRecommend(Long diseaseId);
}