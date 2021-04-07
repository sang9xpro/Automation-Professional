package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.FacebookValuesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FacebookValues} and its DTO {@link FacebookValuesDTO}.
 */
@Mapper(componentModel = "spring", uses = { FacebookMapper.class, FacebookFieldsMapper.class })
public interface FacebookValuesMapper extends EntityMapper<FacebookValuesDTO, FacebookValues> {
    @Mapping(target = "facebook", source = "facebook", qualifiedByName = "id")
    @Mapping(target = "facebookFields", source = "facebookFields", qualifiedByName = "id")
    FacebookValuesDTO toDto(FacebookValues s);
}
