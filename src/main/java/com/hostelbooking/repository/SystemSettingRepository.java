package com.hostelbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.hostelbooking.entity.SystemSetting;

@Repository
public interface SystemSettingRepository extends JpaRepository<SystemSetting, Long> {
    @Query("SELECT s.allowRegistration FROM SystemSetting s WHERE s.id = 1")
    Boolean isRegistrationAllowed();  // Use Boolean instead of boolean
}

