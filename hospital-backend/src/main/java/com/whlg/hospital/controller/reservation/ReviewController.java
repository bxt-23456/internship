package com.whlg.hospital.controller.reservation;

import com.whlg.hospital.dto.CreateReviewDto;
import com.whlg.hospital.service.ReviewService;
import com.whlg.hospital.util.R;
import com.whlg.hospital.vo.ReviewVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody CreateReviewDto dto) {
        try {
            reviewService.submitReview(dto);
            return R.createSuccess("评价提交成功");
        } catch (RuntimeException e) {
            return R.createError(e.getMessage());
        }
    }

    @GetMapping("/list")
    public R<List<ReviewVo>> list(@RequestParam Long userId) {
        return R.createSuccess(reviewService.listByUserId(userId));
    }
}
