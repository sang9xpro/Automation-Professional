package com.automation.professional.repository;

import com.automation.professional.domain.DevicesFields;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DevicesFields entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DevicesFieldsRepository extends JpaRepository<DevicesFields, Long> {}
