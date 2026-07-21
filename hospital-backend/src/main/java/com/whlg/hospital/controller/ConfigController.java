package com.whlg.hospital.controller;

import com.whlg.hospital.service.ConfigService;
import com.whlg.hospital.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @GetMapping("/value")
    public R<String> getValue(@RequestParam String key) {
        String value = configService.getValueByKey(key);
        return R.createSuccess(value);
    }

    @GetMapping("/all")
    public R<Map<String, String>> getAll() {
        Map<String, String> configs = configService.getAllConfigs();
        return R.createSuccess(configs);
    }
}