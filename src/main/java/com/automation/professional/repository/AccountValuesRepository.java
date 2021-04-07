package com.automation.professional.repository;

import com.automation.professional.domain.AccountValues;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AccountValues entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountValuesRepository extends JpaRepository<AccountValues, Long> {}
