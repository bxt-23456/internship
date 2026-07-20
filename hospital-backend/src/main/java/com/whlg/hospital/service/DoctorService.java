package com.whlg.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whlg.hospital.entity.Doctor;
import com.whlg.hospital.vo.DoctorDetailVo;
import com.whlg.hospital.vo.DoctorVo;
import com.whlg.hospital.vo.reservation.ReservationDoctorVo;
import com.whlg.hospital.vo.reservation.ReservationSlotVo;

import java.util.List;

public interface DoctorService extends IService<Doctor> {

    //查询医生及对应的医院名称、科室名称
    public List<DoctorVo> listTopDoctorVo(int limit);

    //根据医院ID查询医生列表（可选按科室筛选）
    public List<DoctorVo> listDoctorsByHospital(Long hospitalId, Long departmentId);

    //查询所有医生（可选按科室筛选）
    public List<DoctorVo> listAllDoctors(Long departmentId);

    //根据一级科室ID查询医生（包括子科室的医生）
    public List<DoctorVo> listDoctorsByParentDepartment(Long parentDepartmentId);

    //根据ID查询医生详情
    public DoctorDetailVo getDoctorDetailById(Long id);

    //搜索医生
    public List<Doctor> searchDoctors(String keyword);

    public ReservationDoctorVo getReservationDoctorInfo(Long doctorId);

    public List<ReservationSlotVo> getReservationSlots(Long doctorId, String scheduleDate);
}
