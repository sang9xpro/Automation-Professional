package com.automation.professional.repository;

import com.automation.professional.domain.MostOfContValues;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MostOfContValues entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MostOfContValuesRepository extends JpaRepository<MostOfContValues, Long> {}
