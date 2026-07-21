package com.whlg.hospital.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.whlg.hospital.entity.Config;
import com.whlg.hospital.mapper.ConfigMapper;
import com.whlg.hospital.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

    @Autowired
    private ConfigMapper configMapper;

    @Override
    public String getValueByKey(String configKey) {
        Config config = configMapper.selectByConfigKey(configKey);
        return config != null ? config.getConfigValue() : null;
    }

    @Override
    public Map<String, String> getAllConfigs() {
        Map<String, String> configMap = new HashMap<>();
        List<Config> configs = configMapper.selectList(new QueryWrapper<>());
        for (Config config : configs) {
            configMap.put(config.getConfigKey(), config.getConfigValue());
        }
        return configMap;
    }
}