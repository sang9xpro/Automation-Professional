package com.automation.professional.repository;

import com.automation.professional.domain.LoggersFields;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LoggersFields entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoggersFieldsRepository extends JpaRepository<LoggersFields, Long> {}
