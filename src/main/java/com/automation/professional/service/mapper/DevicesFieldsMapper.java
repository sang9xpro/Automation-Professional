package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.DevicesFieldsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DevicesFields} and its DTO {@link DevicesFieldsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DevicesFieldsMapper extends EntityMapper<DevicesFieldsDTO, DevicesFields> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DevicesFieldsDTO toDtoId(DevicesFields devicesFields);
}
