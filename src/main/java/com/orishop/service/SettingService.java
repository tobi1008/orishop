package com.orishop.service;

import java.util.Map;

public interface SettingService {
    Map<String, String> getAllSettings();

    void saveSettings(Map<String, String> settings);

    String getSetting(String key);
}
