package com.automation.professional.repository;

import com.automation.professional.domain.Facebook;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Facebook entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacebookRepository extends JpaRepository<Facebook, Long> {}
