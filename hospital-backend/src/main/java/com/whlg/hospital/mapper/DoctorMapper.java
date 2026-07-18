package com.whlg.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whlg.hospital.entity.Doctor;
import com.whlg.hospital.vo.DoctorVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DoctorMapper extends BaseMapper<Doctor> {

    //查询医生及对应的医院名称、科室名称
    public List<DoctorVo> selectDoctorVoBy(@Param("limit") int limit);

    //根据医院ID查询医生列表（可选按科室筛选）
    public List<DoctorVo> selectDoctorsByHospital(@Param("hospitalId") Long hospitalId, @Param("departmentId") Long departmentId);
}
