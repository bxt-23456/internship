package com.whlg.hospital.controller;

import com.whlg.hospital.service.DoctorService;
import com.whlg.hospital.service.ReviewService;
import com.whlg.hospital.util.R;
import com.whlg.hospital.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 医生控制器
 */
@RestController
@RequestMapping("/doctor")
public class DoctorController {

    private static final Logger log = LoggerFactory.getLogger(DoctorController.class);

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ReviewService reviewService;

    /**
     * 获取医院的医生列表（支持科室筛选）
     */
    @GetMapping("/list")
    public R<List<DoctorVo>> listDoctors(
            @RequestParam Long hospitalId,
            @RequestParam(required = false) Long departmentId) {
        List<DoctorVo> doctors = doctorService.listDoctorsByHospital(hospitalId, departmentId);
        return R.createSuccess(doctors);
    }

    /**
     * 分页查询医生（支持多条件筛选）
     */
    @GetMapping("/page")
    public R<PageResult<DoctorVo>> pageDoctors(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long hospitalId,
            @RequestParam(required = false) List<Long> departmentIds,
            @RequestParam(required = false) Integer onlineStatus,
            @RequestParam(required = false) String expertise) {

        log.info("分页查询: hospitalId={}, departmentIds={}, onlineStatus={}, expertise={}",
                hospitalId, departmentIds, onlineStatus, expertise);

        DoctorQueryVo query = new DoctorQueryVo();
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);
        query.setHospitalId(hospitalId);
        query.setDepartmentIds(departmentIds);
        query.setOnlineStatus(onlineStatus);
        query.setExpertise(expertise);

        PageResult<DoctorVo> result = doctorService.pageDoctor(query);
        log.info("查询结果: total={}", result.getTotal());
        return R.createSuccess(result);
    }

    /**
     * 获取医生详情
     */
    @GetMapping("/detail/{id}")
    public R<DoctorDetailVo> getDoctorDetail(@PathVariable Long id) {
        DoctorDetailVo doctor = doctorService.getDoctorDetailById(id);
        return R.createSuccess(doctor);
    }

    /**
     * 获取医生的评价列表
     */
    @GetMapping("/reviews/{doctorId}")
    public R<List<ReviewVo>> getDoctorReviews(@PathVariable Long doctorId) {
        List<ReviewVo> reviews = reviewService.listByDoctorId(doctorId);
        return R.createSuccess(reviews);
    }
}
