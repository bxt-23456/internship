package com.whlg.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whlg.hospital.entity.Follow;
import com.whlg.hospital.vo.DoctorVo;
import com.whlg.hospital.vo.HospitalVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 关注Mapper接口
 */
@Mapper
public interface FollowMapper extends BaseMapper<Follow> {

    /**
     * 查询用户是否关注了某个对象
     * @param userId 用户ID
     * @param followType 关注类型：1医院 2医生 3疾病
     * @param followId 关注对象ID
     * @return 关注记录，如果不存在返回null
     */
    Follow selectByUserAndTarget(@Param("userId") Long userId, 
                                 @Param("followType") Integer followType, 
                                 @Param("followId") Long followId);

    /**
     * 查询用户关注的医院列表
     * @param userId 用户ID
     * @return 医院列表
     */
    List<HospitalVo> selectFollowedHospitals(@Param("userId") Long userId);

    /**
     * 查询用户关注的医生列表
     * @param userId 用户ID
     * @return 医生列表
     */
    List<DoctorVo> selectFollowedDoctors(@Param("userId") Long userId);

    /**
     * 统计对象被关注的数量
     * @param followType 关注类型
     * @param followId 关注对象ID
     * @return 关注数量
     */
    int countByTarget(@Param("followType") Integer followType, 
                      @Param("followId") Long followId);
}
