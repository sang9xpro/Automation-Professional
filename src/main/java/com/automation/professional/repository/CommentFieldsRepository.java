package com.automation.professional.repository;

import com.automation.professional.domain.CommentFields;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CommentFields entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentFieldsRepository extends JpaRepository<CommentFields, Long> {}
