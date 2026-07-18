package com.whlg.hospital.controller;

import com.whlg.hospital.entity.User;
import com.whlg.hospital.service.FollowService;
import com.whlg.hospital.util.R;
import com.whlg.hospital.vo.HospitalVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 鍏虫敞鎺у埗鍣? */
@RestController
@RequestMapping("/follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    /**
     * 鍒囨崲鍏虫敞鐘舵€侊紙鍏虫敞/鍙栨秷鍏虫敞锛?     */
    @PostMapping("/toggle")
    
    public R<Boolean> toggleFollow(
            @RequestParam Integer followType,
            @RequestParam Long followId,
            HttpServletRequest request) {
        // 浠巗ession鑾峰彇鐢ㄦ埛淇℃伅
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return R.createError(20001, "璇峰厛鐧诲綍");
        }
        
        boolean followed = followService.toggleFollow(user.getId(), followType, followId);
        return R.createSuccess(followed);
    }

    /**
     * 妫€鏌ユ槸鍚﹀凡鍏虫敞
     */
    @GetMapping("/check")
    
    public R<Boolean> checkFollow(
            @RequestParam Integer followType,
            @RequestParam Long followId,
            HttpServletRequest request) {
        // 浠巗ession鑾峰彇鐢ㄦ埛淇℃伅
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return R.createSuccess(false);
        }
        
        boolean followed = followService.isFollowed(user.getId(), followType, followId);
        return R.createSuccess(followed);
    }

    /**
     * 鑾峰彇鍏虫敞鐨勫尰闄㈠垪琛?     */
    @GetMapping("/hospitals")
    
    public R<List<HospitalVo>> getFollowedHospitals(HttpServletRequest request) {
        // 浠巗ession鑾峰彇鐢ㄦ埛淇℃伅
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return R.createError(20001, "璇峰厛鐧诲綍");
        }
        
        List<HospitalVo> hospitals = followService.getFollowedHospitals(user.getId());
        return R.createSuccess(hospitals);
    }

    /**
     * 鑾峰彇鍏虫敞鏁伴噺
     */
    @GetMapping("/count")
    public R<Integer> getFollowCount(
            @RequestParam Integer followType,
            @RequestParam Long followId) {
        int count = followService.getFollowCount(followType, followId);
        return R.createSuccess(count);
    }
}

