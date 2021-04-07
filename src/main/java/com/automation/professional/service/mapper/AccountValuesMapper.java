package com.automation.professional.service.mapper;

import com.automation.professional.domain.*;
import com.automation.professional.service.dto.AccountValuesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AccountValues} and its DTO {@link AccountValuesDTO}.
 */
@Mapper(componentModel = "spring", uses = { AccountsMapper.class, AccountFieldsMapper.class })
public interface AccountValuesMapper extends EntityMapper<AccountValuesDTO, AccountValues> {
    @Mapping(target = "accounts", source = "accounts", qualifiedByName = "id")
    @Mapping(target = "accountFields", source = "accountFields", qualifiedByName = "id")
    AccountValuesDTO toDto(AccountValues s);
}
