package com.whlg.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whlg.hospital.entity.FamilyMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FamilyMemberMapper extends BaseMapper<FamilyMember> {
    List<FamilyMember> selectListByUserId(Long userId);

    @Select("SELECT * FROM t_family_member WHERE id = #{id} AND user_id = #{userId}")
    FamilyMember selectByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
}
