package com.whlg.hospital.controller;

import com.whlg.hospital.entity.Feedback;
import com.whlg.hospital.service.FeedbackService;
import com.whlg.hospital.util.R;
import com.whlg.hospital.vo.FeedbackVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);

    @Autowired
    private FeedbackService feedbackService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private String getUploadRootDir() {
        String userDir = System.getProperty("user.dir");
        return userDir + File.separator + "uploads" + File.separator + "feedback" + File.separator;
    }

    @PostMapping("/submit")
    public R<Feedback> submitFeedback(
            @RequestParam("type") String type,
            @RequestParam("content") String content,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            HttpServletRequest request) {
        
        Long userId = (Long) request.getAttribute("userId");
        
        Integer feedbackType = null;
        try {
            feedbackType = Integer.parseInt(type);
        } catch (NumberFormatException e) {
            return R.createError("请选择正确的问题类型");
        }
        
        if (feedbackType == null) {
            return R.createError("请选择问题类型");
        }
        if (content == null || content.trim().isEmpty()) {
            return R.createError("请输入反馈内容");
        }
        
        logger.info("收到反馈提交请求, userId={}, type={}, content={}, imagesCount={}", 
                userId, feedbackType, content.length(), images != null ? images.length : 0);
        
        List<String> imagePaths = new ArrayList<>();
        if (images != null && images.length > 0) {
            for (int i = 0; i < images.length; i++) {
                MultipartFile file = images[i];
                if (!file.isEmpty()) {
                    String filePath = saveImage(file);
                    if (filePath != null) {
                        imagePaths.add(filePath);
                        logger.info("图片保存成功: {}", filePath);
                    } else {
                        logger.error("图片保存失败, index={}", i);
                    }
                } else {
                    logger.info("图片文件为空, index={}", i);
                }
            }
        }
        
        String imagesStr = imagePaths.isEmpty() ? null : String.join(",", imagePaths);
        logger.info("最终图片路径: {}", imagesStr);
        
        Feedback feedback = feedbackService.createFeedback(userId, feedbackType, content.trim(), imagesStr);
        return R.createSuccess(feedback);
    }

    private String saveImage(MultipartFile file) {
        try {
            String dateDir = LocalDateTime.now().format(DATE_FORMATTER);
            String uploadRoot = getUploadRootDir();
            File uploadDir = new File(uploadRoot + dateDir);
            logger.info("上传目录: {}", uploadDir.getAbsolutePath());
            
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                logger.info("目录创建结果: {}", created);
            }
            
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            String newFilename = UUID.randomUUID().toString() + extension;
            File destFile = new File(uploadDir, newFilename);
            logger.info("目标文件: {}", destFile.getAbsolutePath());
            
            file.transferTo(destFile);
            logger.info("文件保存成功");
            
            return "/upload/feedback/" + dateDir + "/" + newFilename;
        } catch (IOException e) {
            logger.error("文件保存失败", e);
            return null;
        }
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