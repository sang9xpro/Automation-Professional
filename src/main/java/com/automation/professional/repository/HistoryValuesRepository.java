package com.automation.professional.repository;

import com.automation.professional.domain.HistoryValues;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the HistoryValues entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoryValuesRepository extends JpaRepository<HistoryValues, Long> {}
