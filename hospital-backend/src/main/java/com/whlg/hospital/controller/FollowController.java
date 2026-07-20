package com.whlg.hospital.controller;

import com.whlg.hospital.service.FollowService;
import com.whlg.hospital.util.R;
import com.whlg.hospital.vo.DoctorVo;
import com.whlg.hospital.vo.HospitalVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 关注控制器
 */
@RestController
@RequestMapping("/follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    /**
     * 切换关注状态（关注/取消关注）
     */
    @PostMapping("/toggle")
    public R<Boolean> toggleFollow(
            @RequestParam Integer followType,
            @RequestParam Long followId,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.createError(20001, "请先登录");
        }

        boolean followed = followService.toggleFollow(userId, followType, followId);
        return R.createSuccess(followed);
    }

    /**
     * 检查是否已关注
     */
    @GetMapping("/check")
    public R<Boolean> checkFollow(
            @RequestParam Integer followType,
            @RequestParam Long followId,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.createSuccess(false);
        }

        boolean followed = followService.isFollowed(userId, followType, followId);
        return R.createSuccess(followed);
    }

    /**
     * 获取关注的医院列表
     */
    @GetMapping("/hospitals")
    public R<List<HospitalVo>> getFollowedHospitals(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.createError(20001, "请先登录");
        }

        List<HospitalVo> hospitals = followService.getFollowedHospitals(userId);
        return R.createSuccess(hospitals);
    }

    /**
     * 获取关注的医生列表
     */
    @GetMapping("/doctors")
    public R<List<DoctorVo>> getFollowedDoctors(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.createError(20001, "请先登录");
        }

        List<DoctorVo> doctors = followService.getFollowedDoctors(userId);
        return R.createSuccess(doctors);
    }

    /**
     * 获取关注数量
     */
    @GetMapping("/count")
    public R<Integer> getFollowCount(
            @RequestParam Integer followType,
            @RequestParam Long followId) {
        int count = followService.getFollowCount(followType, followId);
        return R.createSuccess(count);
    }
}