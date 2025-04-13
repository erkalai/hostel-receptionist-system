package com.hostelbooking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hostelbooking.entity.SystemSetting;
import com.hostelbooking.repository.SystemSettingRepository;

@Service
public class SystemSettingService {

    @Autowired
    private SystemSettingRepository systemSettingRepository;

    public boolean isRegistrationAllowed() {
        Boolean allowRegistration = systemSettingRepository.isRegistrationAllowed();
        return allowRegistration != null && allowRegistration;  // Return true if it's not null and true, otherwise false
    }

    public void updateRegistrationStatus(boolean status) {
        SystemSetting setting = systemSettingRepository.findById(1L).orElseThrow();
        setting.setAllowRegistration(status);
        systemSettingRepository.save(setting);
    }
}
