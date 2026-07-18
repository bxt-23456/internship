package com.whlg.hospital.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whlg.hospital.entity.Doctor;
import com.whlg.hospital.mapper.DoctorMapper;
import com.whlg.hospital.service.DoctorService;
import com.whlg.hospital.vo.DoctorDetailVo;
import com.whlg.hospital.vo.DoctorQueryVo;
import com.whlg.hospital.vo.DoctorVo;
import com.whlg.hospital.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorServiceImpl extends ServiceImpl<DoctorMapper, Doctor> implements DoctorService {

    @Autowired
    private DoctorMapper doctorMapper;

    @Override
    public List<DoctorVo> listTopDoctorVo(int limit) {
        return doctorMapper.selectDoctorVoBy(limit);
    }

    @Override
    public List<DoctorVo> listDoctorsByHospital(Long hospitalId, Long departmentId) {
        return doctorMapper.selectDoctorsByHospital(hospitalId, departmentId);
    }

    @Override
    public PageResult<DoctorVo> pageDoctor(DoctorQueryVo query) {
        Page<DoctorVo> page = new Page<>(query.getPageNum(), query.getPageSize());
        IPage<DoctorVo> result = doctorMapper.selectDoctorPage(page, query);
        return new PageResult<>(result.getTotal(), result.getRecords(),
                result.getCurrent(), result.getSize(), result.getPages());
    }

    @Override
    public DoctorDetailVo getDoctorDetailById(Long id) {
        return doctorMapper.selectDoctorDetailById(id);
    }
}