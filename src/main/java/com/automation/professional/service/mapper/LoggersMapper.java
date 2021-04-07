package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.LoggersDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Loggers} and its DTO {@link LoggersDTO}.
 */
@Mapper(componentModel = "spring", uses = { DevicesMapper.class })
public interface LoggersMapper extends EntityMapper<LoggersDTO, Loggers> {
    @Mapping(target = "devices", source = "devices", qualifiedByName = "id")
    LoggersDTO toDto(Loggers s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LoggersDTO toDtoId(Loggers loggers);
}
