package com.whlg.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whlg.hospital.entity.Doctor;
import com.whlg.hospital.vo.DoctorVo;

import java.util.List;

public interface DoctorMapper extends BaseMapper<Doctor> {

    //查询医生及对应的医院名称、科室名称
    public List<DoctorVo> selectDoctorVoBy(int limit);
}
