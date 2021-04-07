package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.CommentValuesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CommentValues} and its DTO {@link CommentValuesDTO}.
 */
@Mapper(componentModel = "spring", uses = { CommentMapper.class, CommentFieldsMapper.class })
public interface CommentValuesMapper extends EntityMapper<CommentValuesDTO, CommentValues> {
    @Mapping(target = "comment", source = "comment", qualifiedByName = "id")
    @Mapping(target = "commentFields", source = "commentFields", qualifiedByName = "id")
    CommentValuesDTO toDto(CommentValues s);
}
