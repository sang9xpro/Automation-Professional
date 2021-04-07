package com.automation.professional.repository;

import com.automation.professional.domain.Devices;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Devices entity.
 */
@Repository
public interface DevicesRepository extends JpaRepository<Devices, Long> {
    @Query(
        value = "select distinct devices from Devices devices left join fetch devices.accounts left join fetch devices.schedulerTaskDevices",
        countQuery = "select count(distinct devices) from Devices devices"
    )
    Page<Devices> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct devices from Devices devices left join fetch devices.accounts left join fetch devices.schedulerTaskDevices")
    List<Devices> findAllWithEagerRelationships();

    @Query(
        "select devices from Devices devices left join fetch devices.accounts left join fetch devices.schedulerTaskDevices where devices.id =:id"
    )
    Optional<Devices> findOneWithEagerRelationships(@Param("id") Long id);
}
