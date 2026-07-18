package com.whlg.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whlg.hospital.entity.Hospital;
import com.whlg.hospital.vo.HospitalVo;

import com.whlg.hospital.vo.DepartmentTreeVo;
import java.util.List;

/**
 * 医院服务接口
 */
public interface HospitalService extends IService<Hospital> {

    /**
     * 获取医院列表（支持分页和科室筛选）
     * @param departmentId 科室ID（可选）
     * @param page 页码
     * @param pageSize 每页数量
     * @return 医院列表
     */
    List<HospitalVo> listHospitals(Long departmentId, int page, int pageSize);

    /**
     * 获取医院总数
     * @param departmentId 科室ID（可选）
     * @return 医院总数
     */
    int countHospitals(Long departmentId);

    /**
     * 根据ID获取医院详情
     * @param id 医院ID
     * @return 医院详情
     */
    HospitalVo getHospitalDetail(Long id);

    /**
     * 获取推荐医院列表
     * @param limit 数量限制
     * @return 推荐医院列表
     */
    List<HospitalVo> listTopHospitals(int limit);

    /**
     * 根据医院ID获取医院的科室ID列表
     * @param hospitalId 医院ID
     * @return 科室ID列表
     */
    List<Long> getHospitalDepartmentIds(Long hospitalId);

    /**
     * 获取医院的科室树形结构
     * @param hospitalId 医院ID
     * @return 科室树形结构
     */
    List<DepartmentTreeVo> getHospitalDepartmentTree(Long hospitalId);
}
