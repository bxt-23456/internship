package com.whlg.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whlg.hospital.entity.Doctor;
import com.whlg.hospital.entity.Follow;
import com.whlg.hospital.entity.Hospital;
import com.whlg.hospital.entity.Disease;
import com.whlg.hospital.mapper.DoctorMapper;
import com.whlg.hospital.mapper.FollowMapper;
import com.whlg.hospital.mapper.HospitalMapper;
import com.whlg.hospital.mapper.DiseaseMapper;
import com.whlg.hospital.service.FollowService;
import com.whlg.hospital.vo.DoctorVo;
import com.whlg.hospital.vo.HospitalVo;
import com.whlg.hospital.vo.DiseaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 关注服务实现类
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {

    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private HospitalMapper hospitalMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private DiseaseMapper diseaseMapper;

    @Override
    @Transactional
    public boolean toggleFollow(Long userId, Integer followType, Long followId) {
        // 检查是否已关注
        Follow existingFollow = followMapper.selectByUserAndTarget(userId, followType, followId);
        
        if (existingFollow != null) {
            // 已关注，执行取消关注
            followMapper.deleteById(existingFollow.getId());
            
            // 更新关注数量（减少）
            updateFollowCount(followType, followId, -1);
            
            return false; // 返回false表示已取消关注
        } else {
            // 未关注，执行关注
            Follow follow = new Follow();
            follow.setUserId(userId);
            follow.setFollowType(followType);
            follow.setFollowId(followId);
            follow.setCreateTime(LocalDateTime.now());
            followMapper.insert(follow);
            
            // 更新关注数量（增加）
            updateFollowCount(followType, followId, 1);
            
            return true; // 返回true表示已关注
        }
    }

    @Override
    public boolean isFollowed(Long userId, Integer followType, Long followId) {
        Follow existingFollow = followMapper.selectByUserAndTarget(userId, followType, followId);
        return existingFollow != null;
    }

    @Override
    public List<HospitalVo> getFollowedHospitals(Long userId) {
        return followMapper.selectFollowedHospitals(userId);
    }

    @Override
    public List<DoctorVo> getFollowedDoctors(Long userId) {
        return followMapper.selectFollowedDoctors(userId);
    }

    @Override
    public List<DiseaseVo> getFollowedDiseases(Long userId) {
        return followMapper.selectFollowedDiseases(userId);
    }

    @Override
    public int getFollowCount(Integer followType, Long followId) {
        return followMapper.countByTarget(followType, followId);
    }
    
    /**
     * 更新关注数量
     * @param followType 关注类型
     * @param followId 关注对象ID
     * @param delta 变化量（+1或-1）
     */
    private void updateFollowCount(Integer followType, Long followId, int delta) {
        if (followType == 1) { // 医院
            Hospital hospital = hospitalMapper.selectById(followId);
            if (hospital != null) {
                int newCount = hospital.getFollowCount() + delta;
                if (newCount < 0) newCount = 0;
                hospital.setFollowCount(newCount);
                hospitalMapper.updateById(hospital);
            }
        } else if (followType == 2) { // 医生
            Doctor doctor = doctorMapper.selectById(followId);
            if (doctor != null) {
                int newCount = doctor.getFollowCount() + delta;
                if (newCount < 0) newCount = 0;
                doctor.setFollowCount(newCount);
                doctorMapper.updateById(doctor);
            }
        } else if (followType == 3) { // 疾病
            Disease disease = diseaseMapper.selectById(followId);
            if (disease != null) {
                int newCount = (disease.getFollowCount() != null ? disease.getFollowCount() : 0) + delta;
                if (newCount < 0) newCount = 0;
                disease.setFollowCount(newCount);
                diseaseMapper.updateById(disease);
            }
        }
    }
}
