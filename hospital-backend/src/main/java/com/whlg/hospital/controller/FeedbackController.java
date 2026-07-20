package com.whlg.hospital.controller;

import com.whlg.hospital.entity.Feedback;
import com.whlg.hospital.service.FeedbackService;
import com.whlg.hospital.util.R;
import com.whlg.hospital.vo.FeedbackVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/submit")
    public R<Feedback> submitFeedback(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        
        Integer feedbackType = null;
        if (params.containsKey("type")) {
            if (params.get("type") instanceof Number) {
                feedbackType = ((Number) params.get("type")).intValue();
            } else {
                feedbackType = Integer.parseInt(params.get("type").toString());
            }
        }
        
        String content = (String) params.get("content");
        List<String> images = (List<String>) params.get("images");
        
        String imagesStr = images != null ? String.join(",", images) : null;
        
        if (feedbackType == null) {
            return R.createError("请选择问题类型");
        }
        if (content == null || content.trim().isEmpty()) {
            return R.createError("请输入反馈内容");
        }
        
        Feedback feedback = feedbackService.createFeedback(userId, feedbackType, content.trim(), imagesStr);
        return R.createSuccess(feedback);
    }

    @GetMapping("/list")
    public R<List<FeedbackVo>> getFeedbackList(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<FeedbackVo> feedbacks = feedbackService.getFeedbacksByUserId(userId);
        return R.createSuccess(feedbacks);
    }

    @PostMapping("/reply")
    public R<Feedback> replyFeedback(@RequestBody Map<String, Object> params) {
        Long id = ((Number) params.get("id")).longValue();
        
        Feedback feedback = feedbackService.getFeedbackById(id);
        if (feedback == null) {
            return R.createError("反馈不存在");
        }
        
        Map<Integer, String> replyMap = new HashMap<>();
        replyMap.put(1, "感谢您的反馈！我们已经收到您的问题，技术部门正在处理中。");
        replyMap.put(2, "感谢您的反馈！我们会认真对待您提出的服务问题。");
        replyMap.put(3, "感谢您的反馈！我们会将您的意见转达给相关医生。");
        replyMap.put(4, "感谢您的反馈！我们会认真考虑您的建议。");
        
        String reply = replyMap.getOrDefault(feedback.getFeedbackType(), 
                "感谢您的反馈！我们会认真处理您的问题。");
        
        feedback.setReplyContent(reply);
        feedback.setStatus(2);
        feedback.setReplyTime(LocalDateTime.now());
        feedback.setUpdateTime(LocalDateTime.now());
        
        feedbackService.updateById(feedback);
        return R.createSuccess(feedback);
    }
}