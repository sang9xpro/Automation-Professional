package com.automation.professional.repository;

import com.automation.professional.domain.SchedulerTaskDevice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SchedulerTaskDevice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchedulerTaskDeviceRepository extends JpaRepository<SchedulerTaskDevice, Long> {}
