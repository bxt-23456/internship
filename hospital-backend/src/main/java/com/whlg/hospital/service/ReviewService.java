package com.whlg.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whlg.hospital.dto.CreateReviewDto;
import com.whlg.hospital.entity.Review;
import com.whlg.hospital.vo.ReviewVo;

import java.util.List;

public interface ReviewService extends IService<Review> {

    /**
     * 查询指定医生的评价列表
     * @param doctorId 医生ID
     * @return 评价列表（含用户信息）
     */
    List<ReviewVo> listByDoctorId(Long doctorId);

    boolean submitReview(CreateReviewDto dto);

    List<ReviewVo> listByUserId(Long userId);
}
