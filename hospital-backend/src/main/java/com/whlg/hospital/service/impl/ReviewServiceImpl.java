package com.whlg.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whlg.hospital.entity.Review;
import com.whlg.hospital.mapper.ReviewMapper;
import com.whlg.hospital.service.ReviewService;
import com.whlg.hospital.vo.ReviewVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Override
    public List<ReviewVo> listByDoctorId(Long doctorId) {
        return reviewMapper.selectReviewVoByDoctorId(doctorId);
    }
}