package com.whlg.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whlg.hospital.entity.FamilyMember;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FamilyMemberMapper extends BaseMapper<FamilyMember> {
    List<FamilyMember> selectListByUserId(Long userId);
}
