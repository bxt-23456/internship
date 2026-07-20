package com.whlg.hospital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whlg.hospital.entity.Feedback;
import com.whlg.hospital.mapper.FeedbackMapper;
import com.whlg.hospital.service.FeedbackService;
import com.whlg.hospital.vo.FeedbackVo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final Map<Integer, String> TYPE_MAP = new HashMap<>();
    static {
        TYPE_MAP.put(1, "系统问题");
        TYPE_MAP.put(2, "服务问题");
        TYPE_MAP.put(3, "医生问题");
        TYPE_MAP.put(4, "其他问题");
    }

    private static final Map<Integer, String> STATUS_MAP = new HashMap<>();
    static {
        STATUS_MAP.put(1, "待处理");
        STATUS_MAP.put(2, "已回复");
    }

    @Override
    public Feedback createFeedback(Long userId, Integer feedbackType, String content, String images) {
        Feedback feedback = new Feedback();
        feedback.setUserId(userId);
        feedback.setFeedbackType(feedbackType);
        feedback.setContent(content);
        feedback.setImages(images);
        feedback.setStatus(1);
        feedback.setCreateTime(LocalDateTime.now());
        feedback.setUpdateTime(LocalDateTime.now());
        
        this.save(feedback);
        return feedback;
    }

    @Override
    public List<FeedbackVo> getFeedbacksByUserId(Long userId) {
        LambdaQueryWrapper<Feedback> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Feedback::getUserId, userId);
        wrapper.orderByDesc(Feedback::getCreateTime);
        
        List<Feedback> feedbacks = this.list(wrapper);
        
        return feedbacks.stream().map(this::convertToVo).collect(Collectors.toList());
    }

    @Override
    public Feedback getFeedbackById(Long id) {
        return this.getById(id);
    }

    private FeedbackVo convertToVo(Feedback feedback) {
        FeedbackVo vo = new FeedbackVo();
        vo.setId(feedback.getId());
        vo.setFeedbackType(feedback.getFeedbackType());
        vo.setTypeText(TYPE_MAP.getOrDefault(feedback.getFeedbackType(), "其他问题"));
        vo.setContent(feedback.getContent());
        vo.setImages(feedback.getImages());
        
        if (feedback.getImages() != null && !feedback.getImages().isEmpty()) {
            vo.setImagesList(Arrays.asList(feedback.getImages().split(",")));
        } else {
            vo.setImagesList(new ArrayList<>());
        }
        
        vo.setStatus(feedback.getStatus());
        vo.setStatusText(STATUS_MAP.getOrDefault(feedback.getStatus(), "待处理"));
        vo.setReply(feedback.getReplyContent());
        vo.setReplyTime(feedback.getReplyTime());
        
        if (feedback.getCreateTime() != null) {
            vo.setCreateTime(feedback.getCreateTime());
            vo.setCreateTimeText(feedback.getCreateTime().format(FORMATTER));
        }
        
        if (feedback.getReplyTime() != null) {
            vo.setReplyTimeText(feedback.getReplyTime().format(FORMATTER));
        }
        
        return vo;
    }
}