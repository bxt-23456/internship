package com.whlg.hospital.service;

import com.whlg.hospital.vo.DoctorVo;
import com.whlg.hospital.vo.HospitalVo;
import com.whlg.hospital.vo.DiseaseVo;

import java.util.List;

/**
 * 关注服务接口
 */
public interface FollowService {

    /**
     * 切换关注状态（关注/取消关注）
     * @param userId 用户ID
     * @param followType 关注类型：1医院 2医生 3疾病
     * @param followId 关注对象ID
     * @return true:已关注，false:已取消关注
     */
    boolean toggleFollow(Long userId, Integer followType, Long followId);

    /**
     * 检查用户是否已关注某个对象
     * @param userId 用户ID
     * @param followType 关注类型
     * @param followId 关注对象ID
     * @return true:已关注，false:未关注
     */
    boolean isFollowed(Long userId, Integer followType, Long followId);

    /**
     * 获取用户关注的医院列表
     * @param userId 用户ID
     * @return 医院列表
     */
    List<HospitalVo> getFollowedHospitals(Long userId);

    /**
     * 获取用户关注的医生列表
     * @param userId 用户ID
     * @return 医生列表
     */
    List<DoctorVo> getFollowedDoctors(Long userId);

    /**
     * 获取用户关注的疾病列表
     * @param userId 用户ID
     * @return 疾病列表
     */
    List<DiseaseVo> getFollowedDiseases(Long userId);

    /**
     * 获取对象被关注的数量
     * @param followType 关注类型
     * @param followId 关注对象ID
     * @return 关注数量
     */
    int getFollowCount(Integer followType, Long followId);
}
