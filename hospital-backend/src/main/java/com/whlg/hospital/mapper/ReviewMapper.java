package com.whlg.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whlg.hospital.entity.Review;
import com.whlg.hospital.vo.ReviewVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReviewMapper extends BaseMapper<Review> {

    /**
     * 查询指定医生的评价列表（关联用户表获取用户名和头像）
     */
    List<ReviewVo> selectReviewVoByDoctorId(@Param("doctorId") Long doctorId);
}