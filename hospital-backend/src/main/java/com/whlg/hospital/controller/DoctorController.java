package com.whlg.hospital.controller;

import com.whlg.hospital.service.DoctorService;
import com.whlg.hospital.util.R;
import com.whlg.hospital.vo.DoctorDetailVo;
import com.whlg.hospital.vo.DoctorVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 医生控制器
 */

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    /**
     * 获取医院的医生列表（支持科室筛选）
     * @param hospitalId 医院ID
     * @param departmentId 科室ID（可选）
     * @return 医生列表
     */
    @GetMapping("/list")
    public R<List<DoctorVo>> listDoctors(
            @RequestParam Long hospitalId,
            @RequestParam(required = false) Long departmentId) {
        List<DoctorVo> doctors = doctorService.listDoctorsByHospital(hospitalId, departmentId);
        return R.createSuccess(doctors);
    }

    /**
     * 获取所有医生列表（支持科室筛选）
     * @param departmentId 科室ID（可选）
     * @return 医生列表
     */
    @GetMapping("/all")
    public R<List<DoctorVo>> listAllDoctors(
            @RequestParam(required = false) Long departmentId) {
        List<DoctorVo> doctors = doctorService.listAllDoctors(departmentId);
        return R.createSuccess(doctors);
    }

    /**
     * 根据一级科室获取医生列表（包括子科室的医生）
     * @param parentDepartmentId 一级科室ID
     * @return 医生列表
     */
    @GetMapping("/byParentDepartment")
    public R<List<DoctorVo>> listDoctorsByParentDepartment(
            @RequestParam Long parentDepartmentId) {
        List<DoctorVo> doctors = doctorService.listDoctorsByParentDepartment(parentDepartmentId);
        return R.createSuccess(doctors);
    }

    /**
     * 获取医生详情
     * @param id 医生ID
     * @return 医生详情
     */
    @GetMapping("/detail")
    public R<DoctorDetailVo> getDoctorDetail(@RequestParam Long id) {
        DoctorDetailVo doctor = doctorService.getDoctorDetailById(id);
        if (doctor == null) {
            return R.createError("医生不存在");
        }
        return R.createSuccess(doctor);
    }
}
