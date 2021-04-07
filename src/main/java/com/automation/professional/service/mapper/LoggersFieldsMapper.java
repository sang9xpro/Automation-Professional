package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.LoggersFieldsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LoggersFields} and its DTO {@link LoggersFieldsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LoggersFieldsMapper extends EntityMapper<LoggersFieldsDTO, LoggersFields> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LoggersFieldsDTO toDtoId(LoggersFields loggersFields);
}
