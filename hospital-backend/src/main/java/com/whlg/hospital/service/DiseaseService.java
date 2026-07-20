package com.whlg.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whlg.hospital.entity.Disease;

import java.util.List;

public interface DiseaseService extends IService<Disease> {

    List<Disease> searchDiseases(String keyword);
}