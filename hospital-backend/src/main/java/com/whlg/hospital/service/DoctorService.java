package com.whlg.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whlg.hospital.entity.Doctor;
import com.whlg.hospital.vo.DoctorVo;

import java.util.List;

public interface DoctorService extends IService<Doctor> {

    //查询医生及对应的医院名称、科室名称
    public List<DoctorVo> listTopDoctorVo(int limit);

}
