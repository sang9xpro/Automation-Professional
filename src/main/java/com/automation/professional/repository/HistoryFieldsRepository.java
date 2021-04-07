package com.automation.professional.repository;

import com.automation.professional.domain.HistoryFields;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the HistoryFields entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoryFieldsRepository extends JpaRepository<HistoryFields, Long> {}
