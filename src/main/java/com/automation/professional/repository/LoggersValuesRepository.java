package com.automation.professional.repository;

import com.automation.professional.domain.LoggersValues;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LoggersValues entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoggersValuesRepository extends JpaRepository<LoggersValues, Long> {}
