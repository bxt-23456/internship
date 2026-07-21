package com.whlg.hospital.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.whlg.hospital.entity.Config;

import java.util.Map;

public interface ConfigService extends IService<Config> {

    String getValueByKey(String configKey);

    Map<String, String> getAllConfigs();
}