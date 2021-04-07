package com.automation.professional.repository;

import com.automation.professional.domain.AccountFields;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AccountFields entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountFieldsRepository extends JpaRepository<AccountFields, Long> {}
