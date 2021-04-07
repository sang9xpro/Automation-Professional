package com.automation.professional.repository;

import com.automation.professional.domain.Schedulers;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Schedulers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchedulersRepository extends JpaRepository<Schedulers, Long> {}
