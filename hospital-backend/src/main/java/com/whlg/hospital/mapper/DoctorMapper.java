package com.whlg.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.whlg.hospital.entity.Doctor;
import com.whlg.hospital.vo.DoctorDetailVo;
import com.whlg.hospital.vo.DoctorQueryVo;
import com.whlg.hospital.vo.DoctorVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DoctorMapper extends BaseMapper<Doctor> {

    //查询医生及对应的医院名称、科室名称
    public List<DoctorVo> selectDoctorVoBy(@Param("limit") int limit);

    //根据医院ID查询医生列表（可选按科室筛选）
    public List<DoctorVo> selectDoctorsByHospital(@Param("hospitalId") Long hospitalId, @Param("departmentId") Long departmentId);

    //分页查询医生（支持多条件筛选）
    public IPage<DoctorVo> selectDoctorPage(Page<DoctorVo> page, @Param("query") DoctorQueryVo query);

    //根据ID查询医生详情
    public DoctorDetailVo selectDoctorDetailById(@Param("id") Long id);
}