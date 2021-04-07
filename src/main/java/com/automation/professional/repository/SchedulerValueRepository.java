package com.automation.professional.repository;

import com.automation.professional.domain.SchedulerValue;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SchedulerValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchedulerValueRepository extends JpaRepository<SchedulerValue, Long> {}
