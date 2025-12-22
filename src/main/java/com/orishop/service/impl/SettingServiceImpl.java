package com.orishop.service.impl;

import com.orishop.model.Setting;
import com.orishop.repository.SettingRepository;
import com.orishop.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {

    private final SettingRepository settingRepository;

    @Override
    public Map<String, String> getAllSettings() {
        List<Setting> settings = settingRepository.findAll();
        return settings.stream()
                .collect(Collectors.toMap(Setting::getSettingKey, Setting::getValue));
    }

    @Override
    @Transactional
    public void saveSettings(Map<String, String> settings) {
        for (Map.Entry<String, String> entry : settings.entrySet()) {
            Setting setting = settingRepository.findBySettingKey(entry.getKey())
                    .orElseGet(() -> {
                        Setting newSetting = new Setting();
                        newSetting.setSettingKey(entry.getKey());
                        return newSetting;
                    });
            setting.setValue(entry.getValue());
            settingRepository.save(setting);
        }
    }

    @Override
    public String getSetting(String key) {
        return settingRepository.findBySettingKey(key)
                .map(Setting::getValue)
                .orElse(null);
    }
}
