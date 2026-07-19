package com.whlg.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whlg.hospital.entity.Hospital;
import com.whlg.hospital.vo.HospitalVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 医院Mapper接口
 */
@Mapper
public interface HospitalMapper extends BaseMapper<Hospital> {

    /**
     * 查询医院列表（支持分页和科室筛选）
     * @param departmentId 科室ID（可选）
     * @param parentDepartmentId 父科室ID（可选，按一级科室筛选）
     * @param offset 偏移量
     * @param limit 每页数量
     * @return 医院列表
     */
    List<HospitalVo> selectHospitals(@Param("departmentId") Long departmentId,
                                     @Param("parentDepartmentId") Long parentDepartmentId,
                                     @Param("offset") int offset,
                                     @Param("limit") int limit);

    /**
     * 查询医院总数
     * @param departmentId 科室ID（可选）
     * @param parentDepartmentId 父科室ID（可选，按一级科室筛选）
     * @return 医院总数
     */
    int countHospitals(@Param("departmentId") Long departmentId,
                       @Param("parentDepartmentId") Long parentDepartmentId);

    /**
     * 查询医院详情
     * @param id 医院ID
     * @return 医院详情
     */
    HospitalVo selectHospitalDetail(@Param("id") Long id);

    /**
     * 查询推荐医院列表
     * @param limit 数量限制
     * @return 推荐医院列表
     */
    List<HospitalVo> selectTopHospitals(@Param("limit") int limit);

    /**
     * 查询医院的科室ID列表
     * @param hospitalId 医院ID
     * @return 科室ID列表
     */
    List<Long> selectHospitalDepartmentIds(@Param("hospitalId") Long hospitalId);
}
