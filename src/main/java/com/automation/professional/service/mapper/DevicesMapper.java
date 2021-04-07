package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.DevicesDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Devices} and its DTO {@link DevicesDTO}.
 */
@Mapper(componentModel = "spring", uses = { CountryMapper.class, AccountsMapper.class, SchedulerTaskDeviceMapper.class })
public interface DevicesMapper extends EntityMapper<DevicesDTO, Devices> {
    @Mapping(target = "country", source = "country", qualifiedByName = "id")
    @Mapping(target = "accounts", source = "accounts", qualifiedByName = "idSet")
    @Mapping(target = "schedulerTaskDevices", source = "schedulerTaskDevices", qualifiedByName = "idSet")
    DevicesDTO toDto(Devices s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DevicesDTO toDtoId(Devices devices);

    @Mapping(target = "removeAccounts", ignore = true)
    @Mapping(target = "removeSchedulerTaskDevice", ignore = true)
    Devices toEntity(DevicesDTO devicesDTO);
}
