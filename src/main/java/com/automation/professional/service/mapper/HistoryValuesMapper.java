package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.HistoryValuesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HistoryValues} and its DTO {@link HistoryValuesDTO}.
 */
@Mapper(componentModel = "spring", uses = { HistoryMapper.class, HistoryFieldsMapper.class })
public interface HistoryValuesMapper extends EntityMapper<HistoryValuesDTO, HistoryValues> {
    @Mapping(target = "history", source = "history", qualifiedByName = "id")
    @Mapping(target = "historyFields", source = "historyFields", qualifiedByName = "id")
    HistoryValuesDTO toDto(HistoryValues s);
}
