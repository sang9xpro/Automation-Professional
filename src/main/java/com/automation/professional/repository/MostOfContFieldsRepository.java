package com.automation.professional.repository;

import com.automation.professional.domain.MostOfContFields;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MostOfContFields entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MostOfContFieldsRepository extends JpaRepository<MostOfContFields, Long> {}
