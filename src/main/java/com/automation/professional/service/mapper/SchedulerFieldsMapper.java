package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.SchedulerFieldsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SchedulerFields} and its DTO {@link SchedulerFieldsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SchedulerFieldsMapper extends EntityMapper<SchedulerFieldsDTO, SchedulerFields> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SchedulerFieldsDTO toDtoId(SchedulerFields schedulerFields);
}
