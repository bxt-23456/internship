package com.whlg.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whlg.hospital.entity.Disease;
import com.whlg.hospital.vo.DiseaseVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 疾病Mapper
 */
@Mapper
public interface DiseaseMapper extends BaseMapper<Disease> {

    IPage<DiseaseVo> selectDiseasePage(Page<DiseaseVo> page, @Param("departmentId") Long departmentId);

    DiseaseVo selectDiseaseDetail(@Param("id") Long id);
}
