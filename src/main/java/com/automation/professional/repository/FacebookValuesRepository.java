package com.automation.professional.repository;

import com.automation.professional.domain.FacebookValues;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FacebookValues entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacebookValuesRepository extends JpaRepository<FacebookValues, Long> {}
