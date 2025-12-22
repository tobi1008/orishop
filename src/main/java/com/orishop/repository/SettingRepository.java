package com.orishop.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.orishop.model.Setting;

public interface SettingRepository extends JpaRepository<Setting, Long> {
    Optional<Setting> findBySettingKey(String settingKey);
}
