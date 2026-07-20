package com.whlg.hospital.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.whlg.hospital.entity.Consult;
import com.whlg.hospital.vo.ConsultVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ConsultMapper extends BaseMapper<Consult> {

    List<ConsultVo> selectConsultList(@Param("userId") Long userId, @Param("status") Integer status);

    ConsultVo selectConsultDetailByOrderNo(@Param("orderNo") String orderNo);
}
