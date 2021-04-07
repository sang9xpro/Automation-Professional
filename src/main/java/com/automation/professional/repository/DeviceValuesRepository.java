package com.automation.professional.repository;

import com.automation.professional.domain.DeviceValues;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DeviceValues entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeviceValuesRepository extends JpaRepository<DeviceValues, Long> {}
