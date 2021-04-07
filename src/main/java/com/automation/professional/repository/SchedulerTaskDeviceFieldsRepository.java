package com.automation.professional.repository;

import com.automation.professional.domain.SchedulerTaskDeviceFields;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SchedulerTaskDeviceFields entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchedulerTaskDeviceFieldsRepository extends JpaRepository<SchedulerTaskDeviceFields, Long> {}
