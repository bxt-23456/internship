package com.whlg.hospital.controller;

import com.whlg.hospital.entity.Hospital;
import com.whlg.hospital.service.HospitalService;
import com.whlg.hospital.util.R;
import com.whlg.hospital.vo.HospitalVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.whlg.hospital.vo.DepartmentTreeVo;
import java.util.List;

/**
 * 医院控制器
 */

@RestController
@RequestMapping("/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    /**
     * 获取医院列表（支持分页和科室筛选）
     * @param departmentId 科室ID（可选）
     * @param page 页码
     * @param pageSize 每页数量
     * @return 医院列表
     */
    @GetMapping("/list")
    public R<List<HospitalVo>> listHospitals(
            @RequestParam(required = false) Long departmentId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        List<HospitalVo> hospitals = hospitalService.listHospitals(departmentId, page, pageSize);
        return R.createSuccess(hospitals);
    }

    /**
     * 获取医院总数
     * @param departmentId 科室ID（可选）
     * @return 医院总数
     */
    @GetMapping("/count")
    public R<Integer> countHospitals(@RequestParam(required = false) Long departmentId) {
        int count = hospitalService.countHospitals(departmentId);
        return R.createSuccess(count);
    }

    /**
     * 根据ID获取医院详情
     * @param id 医院ID
     * @return 医院详情
     */
    @GetMapping("/detail/{id}")
    public R<HospitalVo> getHospitalDetail(@PathVariable Long id) {
        HospitalVo hospital = hospitalService.getHospitalDetail(id);
        return R.createSuccess(hospital);
    }

    /**
     * 获取推荐医院列表
     * @param limit 数量限制
     * @return 推荐医院列表
     */
    @GetMapping("/listTop")
    public R<List<HospitalVo>> listTopHospitals(@RequestParam(defaultValue = "4") int limit) {
        List<HospitalVo> hospitals = hospitalService.listTopHospitals(limit);
        return R.createSuccess(hospitals);
    }

    /**
     * 根据医院ID获取医院的科室列表
     * @param hospitalId 医院ID
     * @return 科室列表
     */
    @GetMapping("/departments/{hospitalId}")
    public R<List<Long>> getHospitalDepartments(@PathVariable Long hospitalId) {
        List<Long> departmentIds = hospitalService.getHospitalDepartmentIds(hospitalId);
        return R.createSuccess(departmentIds);
    }

    /**
     * 获取医院的科室树形结构
     * @param hospitalId 医院ID
     * @return 科室树形结构
     */
    @GetMapping("/departments/tree/{hospitalId}")
    public R<List<DepartmentTreeVo>> getHospitalDepartmentTree(@PathVariable Long hospitalId) {
        List<DepartmentTreeVo> tree = hospitalService.getHospitalDepartmentTree(hospitalId);
        return R.createSuccess(tree);
    }
}
