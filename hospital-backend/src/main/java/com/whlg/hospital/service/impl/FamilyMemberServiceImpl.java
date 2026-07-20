package com.whlg.hospital.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whlg.hospital.entity.FamilyMember;
import com.whlg.hospital.mapper.FamilyMemberMapper;
import com.whlg.hospital.service.FamilyMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class FamilyMemberServiceImpl extends ServiceImpl<FamilyMemberMapper, FamilyMember> implements FamilyMemberService {

    @Autowired
    private FamilyMemberMapper familyMemberMapper;

    @Override
    public List<FamilyMember> listFamilyMembersByUserId(Long userId) {
        return familyMemberMapper.selectListByUserId(userId);
    }
}
