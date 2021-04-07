package com.automation.professional.repository;

import com.automation.professional.domain.SchedulerTaskDeviceValues;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SchedulerTaskDeviceValues entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchedulerTaskDeviceValuesRepository extends JpaRepository<SchedulerTaskDeviceValues, Long> {}
