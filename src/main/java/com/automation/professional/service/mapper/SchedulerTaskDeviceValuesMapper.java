package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.SchedulerTaskDeviceValuesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SchedulerTaskDeviceValues} and its DTO {@link SchedulerTaskDeviceValuesDTO}.
 */
@Mapper(componentModel = "spring", uses = { SchedulerTaskDeviceMapper.class, SchedulerTaskDeviceFieldsMapper.class })
public interface SchedulerTaskDeviceValuesMapper extends EntityMapper<SchedulerTaskDeviceValuesDTO, SchedulerTaskDeviceValues> {
    @Mapping(target = "schedulerTaskDevice", source = "schedulerTaskDevice", qualifiedByName = "id")
    @Mapping(target = "schedulerTaskDeviceFields", source = "schedulerTaskDeviceFields", qualifiedByName = "id")
    SchedulerTaskDeviceValuesDTO toDto(SchedulerTaskDeviceValues s);
}
