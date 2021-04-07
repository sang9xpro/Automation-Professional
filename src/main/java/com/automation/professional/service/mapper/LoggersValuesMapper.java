package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.LoggersValuesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LoggersValues} and its DTO {@link LoggersValuesDTO}.
 */
@Mapper(componentModel = "spring", uses = { LoggersMapper.class, LoggersFieldsMapper.class })
public interface LoggersValuesMapper extends EntityMapper<LoggersValuesDTO, LoggersValues> {
    @Mapping(target = "loggers", source = "loggers", qualifiedByName = "id")
    @Mapping(target = "loggersFields", source = "loggersFields", qualifiedByName = "id")
    LoggersValuesDTO toDto(LoggersValues s);
}
