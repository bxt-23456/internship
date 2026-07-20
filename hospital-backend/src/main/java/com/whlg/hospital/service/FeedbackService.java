package com.whlg.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whlg.hospital.entity.Feedback;
import com.whlg.hospital.vo.FeedbackVo;

import java.util.List;

public interface FeedbackService extends IService<Feedback> {

    Feedback createFeedback(Long userId, Integer feedbackType, String content, String images);

    List<FeedbackVo> getFeedbacksByUserId(Long userId);

    Feedback getFeedbackById(Long id);
}