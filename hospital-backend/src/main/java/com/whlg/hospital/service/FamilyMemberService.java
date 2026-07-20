package com.whlg.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whlg.hospital.entity.FamilyMember;

import java.util.List;

public interface FamilyMemberService extends IService<FamilyMember> {
    // 根据用户ID查询家庭成员列表
    public List<FamilyMember> listFamilyMembersByUserId(Long userId);
}
