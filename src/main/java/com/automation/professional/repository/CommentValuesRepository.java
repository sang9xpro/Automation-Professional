package com.automation.professional.repository;

import com.automation.professional.domain.CommentValues;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CommentValues entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentValuesRepository extends JpaRepository<CommentValues, Long> {}
