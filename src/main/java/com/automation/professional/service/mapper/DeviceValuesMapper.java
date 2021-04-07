package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.DeviceValuesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeviceValues} and its DTO {@link DeviceValuesDTO}.
 */
@Mapper(componentModel = "spring", uses = { DevicesMapper.class, DevicesFieldsMapper.class })
public interface DeviceValuesMapper extends EntityMapper<DeviceValuesDTO, DeviceValues> {
    @Mapping(target = "devices", source = "devices", qualifiedByName = "id")
    @Mapping(target = "devicesFields", source = "devicesFields", qualifiedByName = "id")
    DeviceValuesDTO toDto(DeviceValues s);
}
