package com.automation.professional.repository;

import com.automation.professional.domain.SchedulerFields;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SchedulerFields entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchedulerFieldsRepository extends JpaRepository<SchedulerFields, Long> {}
