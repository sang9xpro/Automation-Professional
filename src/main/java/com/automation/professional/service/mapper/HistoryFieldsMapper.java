package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.HistoryFieldsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HistoryFields} and its DTO {@link HistoryFieldsDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HistoryFieldsMapper extends EntityMapper<HistoryFieldsDTO, HistoryFields> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    HistoryFieldsDTO toDtoId(HistoryFields historyFields);
}
